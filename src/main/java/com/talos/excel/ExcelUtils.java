package com.talos.excel;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import com.talos.Init;
import com.talos.constants.StringConstants;
import com.talos.pojo.StepDetail;

/**
 * ExcelUtils.
 * @author Sachin
 */
public class ExcelUtils extends Init {
	
	/** The Constant logger. */
	final static Logger logger = Logger.getLogger(ExcelUtils.class);

	/**
	 * Gets the cell value.
	 *
	 * @param cell the cell
	 * @return the cell value
	 */
	public static String getCellValue(Cell cell) {
		String value = StringConstants.BLANK;
		try {
			if (cell != null) {
				if (cell.getCellType().equals(CellType.NUMERIC)) {
					value = String.valueOf(cell.getNumericCellValue());
				} else {
					value = cell.getStringCellValue();
				}

			}

		} catch (Exception e) {
			logger.error(e);
		}
		return value;
	}

	/**
	 * Gets the actual data.
	 *
	 * @param stepDetail the step detail
	 * @return the actual data
	 */
	public static void getActualData(StepDetail stepDetail) {
		stepDetail.setData(stepDetail.getData().replace(UNIQUEDATA, uniqueData));
		if (stepDetail.getData().startsWith(StringConstants.VTILD)) {
			stepDetail.setActualData(stepDetail.getData().replace(StringConstants.VTILD, StringConstants.BLANK));
			getLocalizationData(stepDetail);
			stepDetail.setStepAction(StringConstants.VERIFY);
		} else if (stepDetail.getData().startsWith(DOLLAR)) {
			stepDetail.setStepAction("Store");
			getLocalizationData(stepDetail);
			stepDetail.setActualData(stepDetail.getData());
		} else {
			stepDetail.setActualData(stepDetail.getData());
			getLocalizationData(stepDetail);
			stepDetail.setStepAction(StringConstants.BLANK);
		}
	}

	/**
	 * Gets the localization data.
	 *
	 * @param stepDetail the step detail
	 * @return the localization data
	 */
	public static void getLocalizationData(StepDetail stepDetail) {
		if (dataLocalizationMap != null && dataLocalizationMap.get(stepDetail.getActualData()) != null) {
			stepDetail.setActualData(dataLocalizationMap.get(stepDetail.getActualData()));
		} else {
			logger.info("Localization data not found for" + stepDetail.getActualData());
		}
	}
}