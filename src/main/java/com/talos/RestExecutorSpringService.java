package com.talos;

import java.io.BufferedReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.jayway.jsonpath.JsonPath;
import com.microsoft.schemas.office.x2006.encryption.CTKeyEncryptor.Uri;
import com.talos.constants.StringConstants;
import com.talos.pojo.StepDetail;
import com.talos.pojo.SuiteDetail;
import com.talos.pojo.TestCaseDetail;
import com.talos.selenium.utils.CommonUtils;
import com.talos.selenium.utils.FileGenerator;

/**
 * RestExecutorService.
 * 
 * @author Sachin
 * 
 */
@RestController
public class RestExecutorSpringService extends Init implements Runnable {

    /** The Constant logger. */
    final static Logger logger = LogManager.getLogger(RestExecutorSpringService.class);

    /** The script details map. */
    Multimap<String, StepDetail> scriptDetailsMap;

    /** The thread browser. */
    String threadBrowser;

    /** The thread group name. */
    String threadGroupName;

    /** The test case start dt time. */
    String testCaseStartDtTime;

    /** The test case end dt time. */
    String testCaseEndDtTime;

    /** The thread total tc. */
    int threadTotalTc = 0;

    /** The thread total failed tc. */
    int threadTotalFailedTc = 0;

    /** The thread total skipped tc. */
    int threadTotalSkippedTc = 0;

    /** The thread total pass tc. */
    int threadTotalPassTc = 0;

    /** The thread total steps. */
    int threadTotalSteps = 0;

    /** The thread total pass steps. */
    int threadTotalPassSteps = 0;

    /** The thread total fail steps. */
    int threadTotalFailSteps = 0;

    /** The thread total skipped steps. */
    int threadTotalSkippedSteps = 0;

    /** The body. */
    String body = BLANK;

    /** The replace body. */
    boolean replaceBody = false;

    /** The set header. */
    boolean setHeader = false;;

    /** The httpmethod. */
    String httpmethod = BLANK;

    /** The response body. */
    String responseBody = BLANK;

    /** The verify reponse. */
    boolean verifyReponse = false;

    /** The url. */
    String url;

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> requestEntity;

    /**
     * Instantiates a new rest executor service.
     *
     * @param threadscriptDetailsMap the threadscript details map
     * @param browser                the browser
     * @param threadGroup            the thread group
     */
    public RestExecutorSpringService(Multimap<String, StepDetail> threadscriptDetailsMap, String browser,
            String threadGroup) {
        scriptDetailsMap = threadscriptDetailsMap;
        threadBrowser = browser;
        threadGroupName = threadGroup;

    }

    /**
     * Run.
     */
    @Override
    public void run() {
        try {
            executionThreadDetailsMap.get(threadGroupName).setThreadStartTime(dateFormat.format(new Date()));
            LinkedHashMultimap<String, TestCaseDetail> testCaseList = LinkedHashMultimap.create();
            LinkedHashMultimap<String, SuiteDetail> threadSuiteDetails = LinkedHashMultimap.create();
            SuiteDetail suiteDetail = new SuiteDetail();
            String threadStatus = PASS;
            int suiteTotalTc = 0;
            int suiteTotalPassTc = 0;
            int suiteTotalFailTc = 0;
            int suiteTotalSkippedTc = 0;
            String httpmethod = BLANK;
            HttpURLConnection con = null;
            String responseCode = BLANK;
            String suiteName = BLANK;
            String lastSuiteName = BLANK;
            String dependsOn = BLANK;
            suiteDetail.setStatus(PASS);

            for (String key : scriptDetailsMap.keySet()) {
                logger.info("Executing test case-" + key);
                TestCaseDetail testCaseDetail = new TestCaseDetail();
                totalTestCase++;
                threadTotalTc++;
                suiteTotalTc++;
                testCaseDetail.setTestCaseId(key);
                testCaseDetail.setStartTime(dateFormat.format(new Date()));
                String testCaseStatus = PASS;
                testCaseDetail.setTestCaseStatus(PASS);
                int testCasetotalSteps = 0;
                int testCasetotalPassSteps = 0;
                int testCasetotalFailSteps = 0;
                int testCaseSkippedSteps = 0;
                body = BLANK;
                replaceBody = false;
                setHeader = false;
                responseBody = BLANK;
                verifyReponse = false;
                for (StepDetail stepDetail : scriptDetailsMap.get(key)) {
                    testCasetotalSteps++;
                    suiteName = stepDetail.getSuiteName();
                    if (!lastSuiteName.equals(suiteName)) {
                        suiteDetail = new SuiteDetail();
                        testCaseList = LinkedHashMultimap.create();
                        suiteDetail.setSuiteStartTime(dateFormat.format(new Date()));
                        suiteDetail.setStatus(PASS);
                        suiteTotalTc = 1;
                        suiteTotalPassTc = 0;
                        suiteTotalFailTc = 0;
                        suiteTotalSkippedTc = 0;
                    }
                    try {
                        testCaseDetail.setTestCase(stepDetail.getTestCase());
                        if (CommonUtils.checkDependentTcStatus(stepDetail.getDependsOn())) {
                            stepDetail.setStepStartTime(dateFormat.format(new Date()));
                            if (stepDetail.getStepAction().isEmpty()
                                    || stepDetail.getStepAction().equalsIgnoreCase("store")) {
                                if (stepDetail.getActualData().contains(DOLLAR)) {
                                    stepDetail.setActualData(
                                            CommonUtils.replaceValueWithRunTimeData(stepDetail.getActualData()));
                                }
                                if (stepDetail.getLabel().equalsIgnoreCase("url")) {
                                    executeSetUrl(stepDetail);
                                } else if (stepDetail.getLabel().trim().equalsIgnoreCase("httpmethod")) {
                                    httpmethod = setHttpMethod(stepDetail);
                                } else if (setHeader && !stepDetail.getLabel().equalsIgnoreCase("body")
                                        && !stepDetail.getLabel().trim().equalsIgnoreCase("responsestatuscode")) {
                                    setHeaderDetails(con, stepDetail);
                                } else if (stepDetail.getLabel().trim().equalsIgnoreCase("body")) {
                                    setBody(stepDetail);
                                } else if (replaceBody && !body.isEmpty()
                                        && !stepDetail.getLabel().trim().equalsIgnoreCase("responsestatuscode")) {
                                    replaceBodyText(body, stepDetail);
                                } else if (stepDetail.getLabel().trim().equalsIgnoreCase("responsestatuscode")) {
                                    responseCode = callRestApi(httpmethod, con, responseCode, body, stepDetail);
                                    verifyReponse = true;
                                } else if (verifyReponse) {
                                    verifyResponse(stepDetail, responseBody);
                                } else {
                                    stepDetail.setStatus(FAIL);
                                    CommonUtils.setStepEndDetails(stepDetail);
                                }
                            } else {
                                dependsOn = stepDetail.getDependsOn();
                                stepDetail.setStatus(SKIPPED);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Error while steps execution:-" + e);
                        stepDetail.setStatus(FAIL);
                    }
                    if (stepDetail.getStatus().equals(FAIL) || stepDetail.getStatus().equals(WARN)) {
                        testCaseStatus = FAIL;
                        threadTotalFailSteps++;
                        totalFailSteps++;
                        testCasetotalFailSteps++;

                    } else if (stepDetail.getStatus().equals(SKIPPED)) {
                        testCaseStatus = SKIPPED;
                        threadTotalSkippedSteps++;
                        totalSkippedSteps++;
                        testCaseSkippedSteps++;

                    } else {
                        threadTotalPassSteps++;
                        totalPassSteps++;
                        testCasetotalPassSteps++;

                    }
                    lastSuiteName = suiteName;
                    stepDetail.setStepEndTime(dateFormat.format(new Date()));
                    stepDetail.setStepTime(
                            CommonUtils.getTime(stepDetail.getStepStartTime(), stepDetail.getStepEndTime()));
                }
                if (testCaseStatus.equals(FAIL)) {
                    threadTotalFailedTc++;
                    totalFailTestCase++;
                    testCaseDetail.setTestCaseStatus(FAIL);
                    threadStatus = FAIL;
                    suiteDetail.setStatus(FAIL);
                    suiteTotalFailTc++;
                } else if (testCaseStatus.equals(SKIPPED)) {
                    threadTotalSkippedTc++;
                    threadTotalSkippedTc++;
                    suiteTotalSkippedTc++;
                    testCaseDetail.setTestCaseStatus(SKIPPED);
                    testCaseDetail.setInfo("Skipped due to dependent Tc:-" + dependsOn);
                    threadStatus = SKIPPED;
                    suiteDetail.setStatus(SKIPPED);
                } else {
                    threadTotalPassTc++;
                    totalPassTestCase++;
                    suiteTotalPassTc++;
                }
                logger.info("Executed test case-" + key);

                testCaseDetail.setEndTime(dateFormat.format(new Date()));
                testCaseDetail.setTestCaseTime(
                        CommonUtils.getTime(testCaseDetail.getStartTime(), testCaseDetail.getEndTime()));
                testCaseDetail.setTotalSteps(testCasetotalSteps);
                testCaseDetail.setPassSteps(testCasetotalPassSteps);
                testCaseDetail.setFailSteps(testCasetotalFailSteps);
                testCaseDetail.setSkippedSteps(testCaseSkippedSteps);
                testCaseList.put(suiteName, testCaseDetail);
                suiteDetail.setSuiteTotalTc(suiteTotalTc);
                suiteDetail.setSuiteTotalPassTc(suiteTotalPassTc);
                suiteDetail.setSuiteTotalFailTc(suiteTotalFailTc);
                suiteDetail.setSuiteTotalSkippedTc(suiteTotalSkippedTc);
                suiteDetail.setSuiteName(suiteName);
                suiteDetail.setTestCaseList(testCaseList);
                suiteDetail.setSuiteEndTime(dateFormat.format(new Date()));
                suiteDetail.setSuiteTime(
                        CommonUtils.getTime(suiteDetail.getSuiteStartTime(), suiteDetail.getSuiteEndTime()));
                threadSuiteDetails.put(threadGroupName, suiteDetail);
                executionThreadDetailsMap.get(threadGroupName).setStatus(threadStatus);
                executionThreadDetailsMap.get(threadGroupName).setSuiteDetails(threadSuiteDetails);
                executionThreadDetailsMap.get(threadGroupName).setThreadFailTc(threadTotalFailedTc);
                executionThreadDetailsMap.get(threadGroupName).setThreadTotalTc(threadTotalTc);
                executionThreadDetailsMap.get(threadGroupName).setThreadPassTc(threadTotalPassTc);
                FileGenerator.generateResultFile();
            }

        } catch (Exception e) {
            logger.error(e);
        } finally {
            executionThreadDetailsMap.get(threadGroupName).setExecutionStatus(FINISHED);
            executionThreadDetailsMap.get(threadGroupName).setThreadEndTime(dateFormat.format(new Date()));
            executionThreadDetailsMap.get(threadGroupName).setThreadTime(
                    CommonUtils.getTime(executionThreadDetailsMap.get(threadGroupName).getThreadStartTime(),
                            executionThreadDetailsMap.get(threadGroupName).getThreadEndTime()));
            FileGenerator.generateResultFile();
        }
    }

    /**
     * Post rest call.
     *
     * @param httpmethod   the httpmethod
     * @param con          the con
     * @param responseCode the response code
     * @param body         the body
     * @param stepDetail   the step detail
     * @return the int
     * @throws ProtocolException the protocol exception
     * @throws IOException       Signals that an I/O exception has occurred.
     */
    private String callRestApi(String httpmethod, HttpURLConnection con, String responseCode, String body,
            StepDetail stepDetail) throws ProtocolException, IOException {
        CommonUtils.setStepStartDetails(stepDetail, "ResponseCode",
                CommonUtils.getLocalizationReportData(stepDetail, "$responseCode"));
        replaceBody = false;
        setHeader = false;
        verifyReponse = true;
        if (httpmethod.equalsIgnoreCase("get")) {
            requestEntity = new HttpEntity<String>(body.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET,requestEntity,String.class);
                    responseCode = response.getStatusCode().toString().replace(" OK", BLANK);
            responseBody = response.getBody().toString();
            
        } else if (httpmethod.equalsIgnoreCase("delete")) {
            requestEntity = new HttpEntity<String>(body.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.DELETE,requestEntity,String.class);
                    responseCode = response.getStatusCode().toString().replace(" OK", BLANK);
            responseBody = response.getBody().toString();
            
        } else if (httpmethod.equalsIgnoreCase("post")) {
            requestEntity = new HttpEntity<String>(body.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST,requestEntity, String.class);
                    responseCode = response.getStatusCode().toString().replace(" OK", BLANK);
            responseBody = response.getBody().toString();

        } else if (httpmethod.equalsIgnoreCase("put")) {
            requestEntity = new HttpEntity<String>(body.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.PUT,requestEntity,String.class);
                    responseCode = response.getStatusCode().toString().replace(" OK", BLANK);
            responseBody = response.getBody().toString();
        

        } else if (httpmethod.equalsIgnoreCase("patch")) {
            requestEntity = new HttpEntity<String>(body.toString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.PATCH,requestEntity,String.class);
                    responseCode = response.getStatusCode().toString().replace(" OK", BLANK);
            responseBody = response.getBody().toString();
        }
        if (stepDetail.getActualData().contains(String.valueOf(responseCode))) {
            stepDetail.setStatus(PASS);
        } else {
            CommonUtils.setFailDetails(stepDetail,
                    "actutal data:-" + responseCode + " Expected Data:-" + stepDetail.getActualData());
        }
        CommonUtils.setStepEndDetails(stepDetail);
        logger.info("tcid:-" + stepDetail.getTcId() + "response:-" + responseBody);

        return responseCode;
    }

    /**
     * Replace body text.
     *
     * @param inputbody  the inputbody
     * @param stepDetail the step detail
     */
    private void replaceBodyText(String inputbody, StepDetail stepDetail) {
        CommonUtils.setStepStartDetails(stepDetail, "replacebody",
                CommonUtils.getLocalizationReportData(stepDetail, "$replaceBody"));
        body = inputbody.replace(stepDetail.getLabel(), stepDetail.getActualData());
        CommonUtils.setStepEndDetails(stepDetail);
    }

    /**
     * Sets the body.
     *
     * @param stepDetail the new body
     */
    private void setBody(StepDetail stepDetail) {

        CommonUtils.setStepStartDetails(stepDetail, "body",
                CommonUtils.getLocalizationReportData(stepDetail, "$setBody"));
        body = CommonUtils.replaceValueWithRunTimeData(stepDetail.getActualData());
        replaceBody = true;
        setHeader = false;
        CommonUtils.setStepEndDetails(stepDetail);
    }

    /**
     * Sets the header details.
     *
     * @param con        the con
     * @param stepDetail the step detail
     */
    private void setHeaderDetails(HttpURLConnection con, StepDetail stepDetail) {
        CommonUtils.setStepStartDetails(stepDetail, "setheader",
                CommonUtils.getLocalizationReportData(stepDetail, "$setHeader"));
        headers.set(stepDetail.getLabel(), stepDetail.getActualData());
        CommonUtils.setStepEndDetails(stepDetail);
    }

    /**
     * Sets the http method.
     *
     * @param stepDetail the step detail
     * @return the string
     */
    private String setHttpMethod(StepDetail stepDetail) {

        CommonUtils.setStepStartDetails(stepDetail, "httpmethod",
                CommonUtils.getLocalizationReportData(stepDetail, "$httpmethod"));
        httpmethod = stepDetail.getActualData();
        setHeader = true;

        CommonUtils.setStepEndDetails(stepDetail);
        return httpmethod;
    }

    /**
     * Execute set url.
     *
     * @param stepDetail the step detail
     * @return the http URL connection
     * @throws MalformedURLException the malformed URL exception
     * @throws IOException           Signals that an I/O exception has occurred.
     */
    private void executeSetUrl(StepDetail stepDetail) throws MalformedURLException, IOException {
        CommonUtils.setStepStartDetails(stepDetail, "url",
                CommonUtils.getLocalizationReportData(stepDetail, "$setUrl"));
        CommonUtils.setStepEndDetails(stepDetail);
        url = stepDetail.getActualData();
    }

    /**
     * Verify response.
     *
     * @param stepDetail   the step detail
     * @param responseBody the response body
     */
    private void verifyResponse(StepDetail stepDetail, String responseBody) {
        CommonUtils.setStepStartDetails(stepDetail, "VerifyResponse",
                CommonUtils.getLocalizationReportData(stepDetail, "$verifyResponse"));
        String actualData = BLANK;
        String expectedString = BLANK;
        try {
            if (stepDetail.getActualData().startsWith("$.") && stepDetail.getActualData().contains("=")) {
                String[] arrInput = stepDetail.getActualData().split("=");
                try {
                    actualData = JsonPath.read(responseBody, arrInput[0]).toString();
                    expectedString = CommonUtils.replaceValueWithRunTimeData(arrInput[1]);
                } catch (Exception e) {
                    logger.error("Invalid JSON Path :- " + e);
                    stepDetail.setStatus(FAIL);
                }

            } else if (stepDetail.getActualData().startsWith("$") && stepDetail.getActualData().contains("=")
                    && !stepDetail.getActualData().startsWith("$.")) {
                String[] arrInput = stepDetail.getActualData().split("=");
                try {
                    actualData = JsonPath.read(responseBody, arrInput[1]).toString();
                    expectedString = arrInput[0];
                } catch (Exception e) {
                    logger.error("Invalid JSON Path :- " + e);
                    stepDetail.setStatus(FAIL);
                }

            } else {
                expectedString = stepDetail.getActualData();
                actualData = responseBody;
            }
            if (stepDetail.getActualData().startsWith("$") && stepDetail.getActualData().contains("=")
                    && !stepDetail.getActualData().startsWith("$.")) {
                CommonUtils.setStepStartDetails(stepDetail, "Store Response Data",
                        CommonUtils.getLocalizationReportData(stepDetail, "$storeData"));
                stepDetail.setStatus(PASS);
                stepDetail.setInfo(expectedString + " :-" + actualData);
                runTimeDataMap.put(expectedString, actualData);
            } else if (actualData.equals(expectedString) && !stepDetail.getStatus().equals(FAIL)) {
                stepDetail.setStatus(PASS);

            } else {
                CommonUtils.setFailDetails(stepDetail,
                        "Actual data-" + actualData + " Expected Data - " + expectedString);
            }
        } catch (Exception e) {
            logger.error("Verify Response " + e);
            CommonUtils.setFailDetails(stepDetail, e.toString());
        }
        CommonUtils.setStepEndDetails(stepDetail);
    }
}
