package com.talos;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import com.talos.constants.StringConstants;
import com.talos.excel.ExcelUtils;
import com.talos.pojo.StepDetail;

/**
 * ReadStepDetail.
 * @author Sachin
 */
public class ReadScriptDetail extends Init {
	
	/** The Constant logger. */
	final static Logger logger = LogManager.getLogger(ReadScriptDetail.class);

	/**
	 * Read details.
	 *
	 * @param thread the thread
	 * @param file the file
	 * @param tagName the tag name
	 * @param scriptDetailsMap the script details map
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String readDetails(String thread, String file, String tagName,
			Multimap<String, StepDetail> scriptDetailsMap) throws IOException {
		String scriptType = UI;
		FileInputStream fis = null;
		XSSFWorkbook wb = null;
		try {
			fis = new FileInputStream(new File(testScriptsDir.concat(File.separator).concat(file)));
			String suiteName = Files.getNameWithoutExtension(file);
			wb = new XSSFWorkbook(fis);
			Sheet sheet = wb.getSheetAt(0);
			if (ExcelUtils.getCellValue(sheet.getRow(0).getCell(1)).equalsIgnoreCase(UI)
					|| ExcelUtils.getCellValue(sheet.getRow(0).getCell(1)).isEmpty()) {
				scriptType = UI;
				String lastTcId = StringConstants.BLANK;
				String lastTestCase = StringConstants.BLANK;
				String lastTag = StringConstants.BLANK;
				for (int i = 3; i < sheet.getPhysicalNumberOfRows(); i++) {
					Row row = sheet.getRow(i);

					if (row != null) {
						String testCaseId = ExcelUtils.getCellValue(row.getCell(0));
						String testCase = ExcelUtils.getCellValue(row.getCell(1));
						String tag = ExcelUtils.getCellValue(row.getCell(2));
						String dependsOn = ExcelUtils.getCellValue(row.getCell(3));
						if (lastTcId.equalsIgnoreCase(testCaseId)) {
							testCase = lastTestCase;
							tag = lastTag;
						}
						if ((tagName.isEmpty() || tagName.equalsIgnoreCase(tag))) {
							for (int j = 4; j <= row.getLastCellNum(); j++) {
								StepDetail stepDetail = new StepDetail();
								stepDetail.setDependsOn(dependsOn);
								stepDetail.setThread(thread);
								stepDetail.setSuiteName(suiteName);
								stepDetail.setTcId(testCaseId);
								stepDetail.setTestCase(testCase);
								stepDetail.setTag(tag);
								stepDetail.setComponent(ExcelUtils.getCellValue(sheet.getRow(0).getCell(j)));
								stepDetail.setXpath(ExcelUtils.getCellValue(sheet.getRow(1).getCell(j)));
								stepDetail.setLabel(ExcelUtils.getCellValue(sheet.getRow(2).getCell(j)));
								stepDetail.setData(ExcelUtils.getCellValue(row.getCell(j)));
								ExcelUtils.getActualData(stepDetail);
								if (stepDetail.getActualData() != null && !stepDetail.getActualData().isEmpty()) {
									scriptDetailsMap.put(thread.concat(StringConstants.UNDERSCORE).concat(suiteName)
											.concat(StringConstants.UNDERSCORE).concat(testCaseId), stepDetail);
								}
							}

						}
						lastTcId = testCaseId;
						lastTag = tag;
						lastTestCase = testCase;
					}
				}
			} else if (ExcelUtils.getCellValue(sheet.getRow(0).getCell(1)).equalsIgnoreCase(REST)) {
				scriptType = REST;
				String lastTcId = StringConstants.BLANK;
				String lastTestCase = StringConstants.BLANK;
				String lastTag = StringConstants.BLANK;
				for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
					Row row = sheet.getRow(i);

					if (row != null) {
						String testCaseId = ExcelUtils.getCellValue(row.getCell(0));
						String testCase = ExcelUtils.getCellValue(row.getCell(1));
						String tag = ExcelUtils.getCellValue(row.getCell(2));
						String dependsOn = ExcelUtils.getCellValue(row.getCell(3));
						if (lastTcId.equalsIgnoreCase(testCaseId)) {
							testCase = lastTestCase;
							tag = lastTag;
						}
						if ((tagName.isEmpty() || tagName.equalsIgnoreCase(tag))) {
							for (int j = 4; j <= row.getLastCellNum(); j++) {
								StepDetail stepDetail = new StepDetail();
								stepDetail.setDependsOn(dependsOn);
								stepDetail.setThread(thread);
								stepDetail.setSuiteName(suiteName);
								stepDetail.setTcId(testCaseId);
								stepDetail.setTestCase(testCase);
								stepDetail.setTag(tag);
								// stepDetail.setComponent(ExcelUtils.getCellValue(sheet.getRow(0).getCell(j)));
								// stepDetail.setXpath(ExcelUtils.getCellValue(sheet.getRow(1).getCell(j)));
								stepDetail.setLabel(ExcelUtils.getCellValue(sheet.getRow(1).getCell(j)));
								stepDetail.setData(ExcelUtils.getCellValue(row.getCell(j)));
								ExcelUtils.getActualData(stepDetail);
								if (stepDetail.getActualData() != null && !stepDetail.getActualData().isEmpty()) {
									scriptDetailsMap.put(thread.concat(StringConstants.UNDERSCORE).concat(suiteName)
											.concat(StringConstants.UNDERSCORE).concat(testCaseId), stepDetail);
								}
							}

						}
						lastTcId = testCaseId;
						lastTag = tag;
						lastTestCase = testCase;
					}
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
		return scriptType;
	}

}