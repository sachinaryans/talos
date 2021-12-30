package com.talos;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.talos.constants.StringConstants;
import com.talos.pojo.StepDetail;
import com.talos.pojo.SuiteDetail;
import com.talos.pojo.TestCaseDetail;
import com.talos.selenium.Keywords;
import com.talos.selenium.utils.CommonUtils;
import com.talos.selenium.utils.FileGenerator;
import com.talos.utils.Utils;

/**
 * ThreadExecutorService.
 * 
 * @author Sachin
 */
public class MobileExecutorService extends Init implements Runnable, StringConstants {

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

	/** The driver. */
	AndroidDriver<MobileElement> driver = null;

	/** The wait. */
	FluentWait<WebDriver> wait;

	/** The jse. */
	JavascriptExecutor jse;

	/** The Constant logger. */
	final static Logger logger = LogManager.getLogger(MobileExecutorService.class);

	/**
	 * Instantiates a new ui executor service.
	 *
	 * @param threadscriptDetailsMap the threadscript details map
	 * @param browser                the browser
	 * @param threadGroup            the thread group
	 */
	public MobileExecutorService(Multimap<String, StepDetail> threadscriptDetailsMap, String browser, String threadGroup) {
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
			driver = getWebdriver();
			listOfWebdriver.add(driver);
			executionThreadDetailsMap.get(threadGroupName).setDriver(driver);
			wait = new WebDriverWait(driver, Long.valueOf(explicitWait)).ignoring(NoSuchElementException.class);
			jse = (JavascriptExecutor) driver;
			if (!appUrl.isEmpty()) {
				driver.navigate().to(appUrl);
				CommonUtils.waitForAjax(driver);
			}

			LinkedHashMultimap<String, TestCaseDetail> testCaseList = LinkedHashMultimap.create();
			LinkedHashMultimap<String, SuiteDetail> threadSuiteDetails = LinkedHashMultimap.create();
			SuiteDetail suiteDetail = new SuiteDetail();
			suiteDetail.setStatus(PASS);
			String threadStatus = PASS;
			int suiteTotalTc = 0;
			int suiteTotalPassTc = 0;
			int suiteTotalFailTc = 0;
			int suiteTotalSkippedTc = 0;
			String suiteName = BLANK;
			String lastSuiteName = BLANK;
			String dependsOn = BLANK;
			for (String key : scriptDetailsMap.keySet()) {
				logger.info("Executing test case-" + key);
				TestCaseDetail testCaseDetail = new TestCaseDetail();
				totalTestCase++;
				threadTotalTc++;
				suiteTotalTc++;
				testCaseDetail.setTestCaseId(key);
				testCaseDetail.setStartTime(dateFormat.format(new Date()));
				String testCaseStatus = StringConstants.PASS;
				testCaseDetail.setTestCaseStatus(StringConstants.PASS);
				int testCasetotalSteps = 0;
				int testCasetotalPassSteps = 0;
				int testCasetotalFailSteps = 0;
				int testCaseSkippedSteps = 0;
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
							 stepDetail.setComponentKeyword(Utils.findComponentMethod(stepDetail.getComponent()));
							if (stepDetail.getComponentKeyword() != null
									&& !stepDetail.getComponentKeyword().isEmpty()) {
								CommonUtils.executeCustomMethod(stepDetail, driver);
							} else if (stepDetail.getStepAction().isEmpty()) {
								setStepActionConditions(stepDetail);
							} else if (stepDetail.getStepAction().equals(VERIFY)) {
								verifyStepActionConditions(stepDetail);
							}
						} else {
							dependsOn = stepDetail.getDependsOn();
							stepDetail.setStatus(SKIPPED);
						}

					} catch (Exception e) {
						logger.error("Error while steps execution:-" + e);
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
					TimeUnit.MILLISECONDS.sleep(stepTime);
					driver.hideKeyboard();
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
			if (driver != null && !isStopped) {
				if (jscoverage) {
					generateJavaScriptCodeCoverageReport(driver);
				}
				listOfWebdriver.remove(driver);
				Alert alert = CommonUtils.findAlert(driver);
				if (alert != null) {
					alert.accept();
				}
				driver.close();
				driver.quit();
				executionThreadDetailsMap.get(threadGroupName).setExecutionStatus(FINISHED);
				executionThreadDetailsMap.get(threadGroupName).setThreadEndTime(dateFormat.format(new Date()));
				executionThreadDetailsMap.get(threadGroupName).setThreadTime(
						CommonUtils.getTime(executionThreadDetailsMap.get(threadGroupName).getThreadStartTime(),
								executionThreadDetailsMap.get(threadGroupName).getThreadEndTime()));
				FileGenerator.generateResultFile();

			}
		}
	}

	/**
	 * Verify step action conditions.
	 *
	 * @param stepDetail the step detail
	 */
	private void verifyStepActionConditions(StepDetail stepDetail) {
		try {
			if (stepDetail.getComponent().equalsIgnoreCase("textbox")
					|| stepDetail.getComponent().equalsIgnoreCase("textarea")
					|| stepDetail.getComponent().equalsIgnoreCase("email")
					|| stepDetail.getComponent().equalsIgnoreCase("password")
					|| stepDetail.getComponent().equalsIgnoreCase("date")) {
				Keywords.verifyTextInTextBox(stepDetail, driver);
			} else if (stepDetail.getComponent().equalsIgnoreCase("radiobutton")) {
				Keywords.verifyRadioButton(stepDetail, driver);
			} else if (stepDetail.getComponent().equalsIgnoreCase("select")) {
				Keywords.verifyValueInSelect(stepDetail, driver);
			} else if (stepDetail.getComponent().equalsIgnoreCase("checkbox")) {
				Keywords.verifyCheckbox(stepDetail, driver);
			} else if (stepDetail.getComponent().equalsIgnoreCase("tableColumn")) {
				Keywords.verifyTableColumn(stepDetail, driver);
			} else {
				CommonUtils.setFailDetails(stepDetail, "keyword not found", driver);
				CommonUtils.setStepEndDetails(stepDetail);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Gets the webdriver.
	 *
	 * @return the webdriver
	 * @throws InterruptedException the interrupted exception
	 * @throws IOException          Signals that an I/O exception has occurred.
	 */
	public AndroidDriver<MobileElement> getWebdriver() throws InterruptedException, IOException {
		logger.info(threadGroupName + "-->getWebdriver --> ");
		AndroidDriver<MobileElement> driver = null;
		try {
			if (!jscoverage) {
				if (threadBrowser.equalsIgnoreCase("chrome")) {
					DesiredCapabilities capabilities = new DesiredCapabilities();
					capabilities.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
					capabilities.setCapability("deviceName", "Micromax A311");
					capabilities.setCapability("platformVersion", "11.0");
					capabilities.setCapability("platformName", "Android");
					capabilities.setCapability("chromedriverExecutable",  webDriverDir + "\\chromedriver_83.exe");
					//capabilities.setCapability("app", app.getAbsolutePath());
					//capabilities.setCapability("appPackage", "in.amazon.mShop.android.shopping");
					//capabilities.setCapability("appActivity", "com.amazon.mShop.home.HomeActivity");
					driver = new AndroidDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
					driver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
					}
			
				
			} else {
				driver = getJSDriver();
			}
			TimeUnit.SECONDS.sleep(1);
			// driver.manage().window().setSize(new Dimension(500,768));
			logger.info("<--getWebdriver <-- ");
		} catch (Exception e) {
			logger.error(threadGroupName + "Error while get Webdriver" + e);
		} finally {
			if (driver == null) {
				getWebdriver();
			}
		}
		
		return driver;
	}

	/**
	 * Gets the JS driver.
	 *
	 * @return the JS driver
	 * @throws MalformedURLException 
	 */
	private AndroidDriver<MobileElement> getJSDriver() throws MalformedURLException {
		AndroidDriver<MobileElement> driver = null;
		if (threadBrowser.equalsIgnoreCase("chrome")) {
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
			capabilities.setCapability("deviceName", "Micromax A311");
			capabilities.setCapability("platformVersion", "11.0");
			capabilities.setCapability("platformName", "Android");
			capabilities.setCapability("chromedriverExecutable",  webDriverDir + "\\chromedriver_83.exe");
			//capabilities.setCapability("app", app.getAbsolutePath());
			//capabilities.setCapability("appPackage", "in.amazon.mShop.android.shopping");
			//capabilities.setCapability("appActivity", "com.amazon.mShop.home.HomeActivity");
			driver = new AndroidDriver<MobileElement>(new URL("http://localhost:4723/wd/hub"), capabilities);
			driver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);
		}
		
		return driver;
	}

	/**
	 * Generate java script code coverage report.
	 *
	 * @param driver the driver
	 */
	private void generateJavaScriptCodeCoverageReport(WebDriver driver) {
		logger.info("-->generateJavaScriptCodeCoverageReport --> ");
		try {
			((JavascriptExecutor) driver).executeScript("window.jscoverFinished = false;");
			((JavascriptExecutor) driver)
					.executeScript("jscoverage_report('instrumented', function(){window.jscoverFinished=true;});");
			(new WebDriverWait(driver, 10000))
					.until((ExpectedCondition<Boolean>) d -> (Boolean) ((JavascriptExecutor) driver)
							.executeScript("return window.jscoverFinished;"));
		} catch (Exception e) {
			logger.error(threadGroupName + "Error while generateJavaScriptCodeCoverageReport" + e);
		}
		logger.info("<--generateJavaScriptCodeCoverageReport <-- ");
	}

	/**
	 * Sets the step action conditions.
	 *
	 * @param stepDetail the new step action conditions
	 */
	public void setStepActionConditions(StepDetail stepDetail) {
		try {

			if (stepDetail.getComponent().equalsIgnoreCase("textbox")
					|| stepDetail.getComponent().equalsIgnoreCase("textarea")
					|| stepDetail.getComponent().equalsIgnoreCase("email")
					|| stepDetail.getComponent().equalsIgnoreCase("password"))
				 {
				Keywords.setTextInTextBox(stepDetail, driver);
			}  else if (stepDetail.getComponent().equalsIgnoreCase("date")) {
				Keywords.setDateInMobile(stepDetail, driver);

			}else if (stepDetail.getComponent().equalsIgnoreCase("url")) {
				Keywords.navigateToUrl(stepDetail, driver);

			} else if (stepDetail.getComponent().equalsIgnoreCase("radiobutton")) {
				Keywords.selectMobileRadioButton(stepDetail, driver);
			} else if (stepDetail.getComponent().equalsIgnoreCase("select")) {
				Keywords.selectValueInMobileSelect(stepDetail, driver);
			}
			else if (stepDetail.getComponent().equalsIgnoreCase("checkbox")) {
				Keywords.selectCheckbox(stepDetail, driver);
			} else if (stepDetail.getComponent().equalsIgnoreCase("inputbutton")
					|| stepDetail.getComponent().equalsIgnoreCase("button")) {
				Keywords.clickOnButton(stepDetail, driver);
			} else if (stepDetail.getComponent().equalsIgnoreCase("file")) {
				Keywords.selectFile(stepDetail, driver);
			} else if (stepDetail.getComponent().equalsIgnoreCase("menu")) {
				Keywords.navigateMenu(stepDetail, driver);
			} else if (stepDetail.getComponent().equalsIgnoreCase("alert")
					|| stepDetail.getComponent().equalsIgnoreCase("confirm")
					|| stepDetail.getComponent().equalsIgnoreCase("prompt")) {
				Keywords.handleAlert(stepDetail, driver);
			} else {
				CommonUtils.setFailDetails(stepDetail, "keyword not found", driver);
				CommonUtils.setStepEndDetails(stepDetail);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Stop.
	 */
	public void stop() {
		isStopped = true;
	}

}