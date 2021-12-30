package com.talos;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.talos.constants.StringConstants;
import com.talos.excel.ExcelUtils;
import com.talos.pojo.ControllerDetail;
import com.talos.pojo.ExecutionThreadDetail;
import com.talos.pojo.StepDetail;
import com.talos.utils.Utils;

/**
 * ExecutorService.
 * @author Sachin
 * This class reads the conroller details and thread details and calls the ui/rest thread for execution
 * 
 */
public class ExecutorService extends Init implements Runnable, StringConstants {
	
	/** The Constant logger. */
	final static Logger logger = LogManager.getLogger(ExecutorService.class);

	/**
	 * Thread run method
	 */
	@Override
	public void run() {
		try {
			readControllerDetails();
			String browser = StringConstants.BLANK;
			Thread executionThread = null;
			for (String thread : controllerDetails.keySet()) {
				ExecutionThreadDetail executionThreadDetails = new ExecutionThreadDetail();
				Multimap<String, StepDetail> threadscriptDetailsMap = LinkedListMultimap.create();
				String threadMachineName = StringConstants.BLANK;
				String threadDependsOn = StringConstants.BLANK;
				String scriptType = UI;
				for (ControllerDetail controllerDetail : controllerDetails.get(thread)) {
					scriptType = ReadScriptDetail.readDetails(thread, controllerDetail.getTestScript(),
							controllerDetail.getTag(), threadscriptDetailsMap);
					browser = controllerDetail.getBrowser().isEmpty() ? browser : controllerDetail.getBrowser();
					threadMachineName = controllerDetail.getMachine().isEmpty() ? threadMachineName
							: controllerDetail.getMachine();
					threadDependsOn = controllerDetail.getDependsOn().isEmpty() ? threadDependsOn
							: controllerDetail.getDependsOn();
				}
				scriptDetailsMap.putAll(threadscriptDetailsMap);
				if(threadMachineName.equalsIgnoreCase("Android")){
					MobileExecutorService mobileExecutorService = new MobileExecutorService(threadscriptDetailsMap, browser,
					thread);
			executionThread = new Thread(mobileExecutorService);
				}
				else if (scriptType.equalsIgnoreCase(UI)&&threadMachineName.equalsIgnoreCase("")) {
					UiExecutorService uiExecutorService = new UiExecutorService(threadscriptDetailsMap, browser,
							thread);
					executionThread = new Thread(uiExecutorService);
				} else {
					RestExecutorService restExecutorService = new RestExecutorService(threadscriptDetailsMap, browser,
							thread);
							browser=REST;
					executionThread = new Thread(restExecutorService);
				}

				executionThreadDetails.setThread(executionThread);
				executionThreadDetails.setBrowser(browser);
				executionThreadDetails.setThreadName(thread);
				executionThreadDetails.setMachineName(threadMachineName);
				executionThreadDetails.setExecutionStatus(START);
				executionThreadDetails.setDependsOn(threadDependsOn);
				if (threadscriptDetailsMap.size() != 0) {
					executionThreadDetailsMap.put(thread, executionThreadDetails);
				}
			}
			threadExecutor(executionThreadDetailsMap);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	/**
	 * Thread executor.
	 *This method executes execution threads in order
	 * @param executionThreadDetailsMap the execution thread details map
	 */
	private void threadExecutor(Map<String, ExecutionThreadDetail> executionThreadDetailsMap) {
		boolean threadStatus = true;
		boolean forceStop = false;
		try {
			logger.info("\n Execution Started");
			createExecutionResultDir();
			Utils.loadComponentDetails();
			if (jscoverage) {
				setUpOnce();
			}
			while (threadStatus && !forceStop && !executionThreadDetailsMap.isEmpty()) {
				for (String thread : executionThreadDetailsMap.keySet()) {
					ExecutionThreadDetail threadDetails = executionThreadDetailsMap.get(thread);
					if (threadDetails.getExecutionStatus().equalsIgnoreCase(StringConstants.START)
							&& threadDetails.getDependsOn().isEmpty()) {
						threadDetails.setExecutionStatus(StringConstants.INPROGRESS);
						listOfThread.add(threadDetails.getThread());
						threadDetails.getThread().start();
						TimeUnit.SECONDS.sleep(5);
						threadStatus = true;
					} else if (threadDetails.getExecutionStatus().equalsIgnoreCase(StringConstants.START)
							&& !threadDetails.getDependsOn().isEmpty()) {
						if ((executionThreadDetailsMap.get(threadDetails.getDependsOn()) != null
								&& executionThreadDetailsMap.get(threadDetails.getDependsOn()).getExecutionStatus()
										.equalsIgnoreCase(StringConstants.FINISHED))
								|| executionThreadDetailsMap.get(threadDetails.getDependsOn()) == null) {
							threadDetails.setExecutionStatus(StringConstants.INPROGRESS);
							listOfThread.add(threadDetails.getThread());
							threadDetails.getThread().start();
							TimeUnit.SECONDS.sleep(5);
							threadStatus = true;
						}
					}
				}
				for (String thread : executionThreadDetailsMap.keySet()) {
					ExecutionThreadDetail threadDetails = executionThreadDetailsMap.get(thread);
					if (threadDetails.getExecutionStatus().equalsIgnoreCase(StringConstants.FINISHED)
							&& threadDetails.getDriver() != null
							&& threadDetails.getDriver().toString().contains("(null)")) {
						threadStatus = false;
					} else if (threadDetails.getExecutionStatus().equalsIgnoreCase(StringConstants.FINISHED)) {
						threadStatus = false;
					} else {
						threadStatus = true;
						break;
					}
				}
				TimeUnit.SECONDS.sleep(1);
			}
			tearDownOnce();
		} catch (Exception e) {
			logger.error("Error while thread executor" + e);
		}
		executionCompleted = true;
	}

	/**
	 * Read controller details.
	 *This method reads the controller details
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void readControllerDetails() throws IOException {
		FileInputStream fis = null;
		XSSFWorkbook wb = null;
		try {
			fis = new FileInputStream(new File(confDir.concat(File.separator).concat("Controller.xlsx")));
			wb = new XSSFWorkbook(fis);
			Sheet sheet = wb.getSheet("Sheet1");
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				if (sheet.getRow(i) != null) {
					ControllerDetail controllerDetail = new ControllerDetail();
					controllerDetail.setSlNo(ExcelUtils.getCellValue(sheet.getRow(i).getCell(0)));
					controllerDetail.setTestScript(ExcelUtils.getCellValue(sheet.getRow(i).getCell(1)));
					controllerDetail.setTag(ExcelUtils.getCellValue(sheet.getRow(i).getCell(2)));
					controllerDetail.setThread(ExcelUtils.getCellValue(sheet.getRow(i).getCell(3)));
					controllerDetail.setDependsOn(ExcelUtils.getCellValue(sheet.getRow(i).getCell(5)));
					controllerDetail.setMachine(ExcelUtils.getCellValue(sheet.getRow(i).getCell(4)));
					controllerDetail.setBrowser(ExcelUtils.getCellValue(sheet.getRow(i).getCell(6)));
					controllerDetails.put(controllerDetail.getThread(), controllerDetail);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (wb != null) {
				wb.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
	}

	/**
	 * Creates the execution result dir.
	 */
	public void createExecutionResultDir() {
		try {
			String currentTime = dateFormat.format(new Date());
			resultFolderName = currentTime.replace(SPACE, UNDERSCORE).replace(HYPHEN, BLANK).replace(DOT, UNDERSCORE)
					.replace(COLON, UNDERSCORE);
			executionresultDir = resultDir.concat(File.separator).concat(resultFolderName);
			if (!Files.exists(Paths.get(executionresultDir))) {
				new File(executionresultDir).mkdir();
			}

		} catch (Exception e) {
			logger.error(e);
		}
	}
}
