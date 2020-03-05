package com.talos.excel;

import java.io.FileOutputStream;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * The Class CreateExcelTemplate.
 * @author Sachin
 */
public class CreateExcelTemplate {
	
	/** The Constant logger. */
	final static Logger logger = LogManager.getLogger(CreateExcelTemplate.class);

	/**
	 * Creates the headers.
	 *
	 * @return the workbook
	 */
	public static Workbook createHeaders() {

		Workbook workbook = null;
		try {
			workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet();
			Row row0 = sheet.createRow(0);
			Row row1 = sheet.createRow(1);
			Row row2 = sheet.createRow(2);
			Cell row2Cell0 = row2.createCell(0);
			Cell row2Cell1 = row2.createCell(1);
			Cell row2Cell2 = row2.createCell(2);
			Cell row2Cell3 = row2.createCell(3);
			Cell row0Cell3 = row0.createCell(3);
			Cell row1Cell3 = row1.createCell(3);
			Cell row2Cell4 = row2.createCell(4);
			Cell row0Cell = row0.createCell(4);
			Cell row1Cell = row1.createCell(4);
			Cell row0Cell0 = row0.createCell(0);
			Cell row0Cell1 = row0.createCell(1);
			row0Cell0.setCellValue("ScriptType");
			row0Cell1.setCellValue("UI");
			row2Cell0.setCellValue("TCID");
			row2Cell1.setCellValue("TestCase");
			row2Cell2.setCellValue("TAG");
			row2Cell3.setCellValue("DependsOn");
			row0Cell3.setCellValue("Component");
			row1Cell3.setCellValue("Xpath");

			row2Cell4.setCellValue("URL");
			row0Cell.setCellValue("URL");
			row1Cell.setCellValue("URL");

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

	/**
	 * Write field details.
	 *
	 * @param sheet the sheet
	 * @param cellNo the cell no
	 * @param component the component
	 * @param xpath the xpath
	 * @param label the label
	 */
	public static void writeFieldDetails(Sheet sheet, int cellNo, String component, String xpath, String label) {
		try {
			Row row0 = sheet.getRow(0);
			Row row1 = sheet.getRow(1);
			Row row2 = sheet.getRow(2);
			Cell row0Cell = row0.createCell(cellNo);
			Cell row1Cell = row1.createCell(cellNo);
			Cell row2Cell = row2.createCell(cellNo);
			row0Cell.setCellValue(component);
			row1Cell.setCellValue(xpath);
			row2Cell.setCellValue(label);

		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Write end field details.
	 *
	 * @param sheet the sheet
	 * @param cellNo the cell no
	 */
	public static void writeEndFieldDetails(Sheet sheet, int cellNo) {
		try {
			Row row0 = sheet.getRow(0);
			Row row1 = sheet.getRow(1);
			Row row2 = sheet.getRow(2);
			Cell row0Cell = row0.createCell(cellNo);
			Cell row1Cell = row1.createCell(cellNo);
			Cell row2Cell = row2.createCell(cellNo);

			Cell row0Cell1 = row0.createCell(cellNo + 1);
			Cell row1Cell1 = row1.createCell(cellNo + 1);
			Cell row2Cell1 = row2.createCell(cellNo + 1);
			Cell row0Cell2 = row0.createCell(cellNo + 2);
			Cell row1Cell2 = row1.createCell(cellNo + 2);
			Cell row2Cell2 = row2.createCell(cellNo + 2);
			row0Cell.setCellValue("alert");
			row1Cell.setCellValue("alert");
			row2Cell.setCellValue("alert");
			row0Cell1.setCellValue("confirm");
			row1Cell1.setCellValue("confirm");
			row2Cell1.setCellValue("confirm");
			row0Cell2.setCellValue("prompt");
			row1Cell2.setCellValue("prompt");
			row2Cell2.setCellValue("prompt");
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
