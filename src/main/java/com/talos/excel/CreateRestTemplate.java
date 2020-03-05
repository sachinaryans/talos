package com.talos.excel;

import java.io.FileOutputStream;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.talos.constants.StringConstants;

/**
 * CreateRestTemplate.
 * @author Sachin
 */
public class CreateRestTemplate implements StringConstants {
	
	/** The Constant logger. */
	final static Logger logger = LogManager.getLogger(CreateRestTemplate.class);

	/**
	 * Creates the.
	 *
	 * @return the workbook
	 */
	public static Workbook create() {

		Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet();
			Row row0 = sheet.createRow(0);
			Row row1 = sheet.createRow(1);
			Cell row1Cell0 = row1.createCell(0);
			Cell row1Cell1 = row1.createCell(1);
			Cell row1Cell2 = row1.createCell(2);
			Cell row1Cell3 = row1.createCell(3);
			Cell row1Cell4 = row1.createCell(4);
			Cell row1Cell5 = row1.createCell(5);
			Cell row1Cell6 = row1.createCell(6);
			Cell row1Cell7 = row1.createCell(7);
			Cell row1Cell8 = row1.createCell(8);
			Cell row1Cell9 = row1.createCell(9);
			Cell row1Cell10 = row1.createCell(10);
			Cell row1Cell11 = row1.createCell(11);
			Cell row1Cell12 = row1.createCell(12);
			Cell row1Cell13 = row1.createCell(13);
			Cell row0Cell0 = row0.createCell(0);
			Cell row0Cell1 = row0.createCell(1);
			row0Cell0.setCellValue("ScriptType");
			row0Cell1.setCellValue("Rest");
			row1Cell0.setCellValue("TCID");
			row1Cell1.setCellValue("TestCase");
			row1Cell2.setCellValue("TAG");
			row1Cell3.setCellValue("DependsOn");
			row1Cell4.setCellValue("url");
			row1Cell5.setCellValue("HttpMethod");
			row1Cell6.setCellValue("Authentication");
			row1Cell7.setCellValue("cookie");
			row1Cell8.setCellValue("Body");
			row1Cell9.setCellValue("$var1");
			row1Cell10.setCellValue("$var2");
			row1Cell11.setCellValue("ResponseStatusCode");
			row1Cell12.setCellValue("ResponseData");
			row1Cell13.setCellValue("ResponseData2");

		} catch (Exception e) {
			logger.error(e);
		}
		return workbook;
	}

	/**
	 * Save excel.
	 *
	 * @param workbook the workbook
	 * @param fileName the file name
	 */
	public static void saveExcel(Workbook workbook, String fileName) {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(fileName);
			workbook.write(fileOut);

		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			logger.info("File Name-" + fileName);
		}
	}
}