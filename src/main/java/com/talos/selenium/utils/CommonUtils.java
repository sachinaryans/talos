package com.talos.selenium.utils;

import java.awt.Graphics2D;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.LinkedHashMultimap;
import com.talos.Init;
import com.talos.constants.StringConstants;
import com.talos.pojo.ComponentDetails;
import com.talos.pojo.StepDetail;
import com.talos.pojo.SuiteDetail;
import com.talos.pojo.TestCaseDetail;
import com.talos.pojo.ComponentDetails.Component;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;

/**
 * KeywordUtils.
 * 
 * @author Sachin
 */
public class CommonUtils extends Init implements StringConstants {

	/** The Constant logger. */
	final static Logger logger = LogManager.getLogger(CommonUtils.class);

	/**
	 * Sets the step start details.
	 *
	 * @param stepDetail  the step detail
	 * @param keyword     the keyword
	 * @param description the description
	 */
	public static void setStepStartDetails(StepDetail stepDetail, String keyword, String description) {
		stepDetail.setKeyword(keyword);
		stepDetail.setStepDescription(description + HYPHEN + SPACE + stepDetail.getLabel());
		stepDetail.setStepStartTime(dateFormat.format(new Date()));
		stepDetail.setStatus(PASS);
	}

	/**
	 * Sets the step end details.
	 *
	 * @param stepDetail the new step end details
	 */
	public static void setStepEndDetails(StepDetail stepDetail) {
		stepDetail.setStepEndTime(dateFormat.format(new Date()));
		stepDetail.setStepTime(getTime(stepDetail.getStepStartTime(), stepDetail.getStepEndTime()));

	}

	/**
	 * Sets the fail details.
	 *
	 * @param stepDetail the step detail
	 * @param error      the error
	 * @param driver     the driver
	 */
	public static void setFailDetails(StepDetail stepDetail, String error, WebDriver driver) {
		stepDetail.setStatus(FAIL);
		stepDetail.setScreenShotPath(CommonUtils.takeScreenShot(driver));
		stepDetail.setInfo(DOUBLEQUOTE.concat(error).concat(DOUBLEQUOTE));
	}

	/**
	 * Sets the fail details.
	 *
	 * @param stepDetail the step detail
	 * @param error      the error
	 * @param driver     the driver
	 * @param alert      the alert
	 */
	public static void setFailDetails(StepDetail stepDetail, String error, WebDriver driver, Alert alert) {
		stepDetail.setStatus(FAIL);
		stepDetail.setScreenShotPath(CommonUtils.takeScreenShot(driver, alert));
		stepDetail.setInfo(DOUBLEQUOTE.concat(error).concat(DOUBLEQUOTE));
	}

	/**
	 * Sets the fail details.
	 *
	 * @param stepDetail the step detail
	 * @param error      the error
	 */
	public static void setFailDetails(StepDetail stepDetail, String error) {
		stepDetail.setStatus(FAIL);
		stepDetail.setInfo(DOUBLEQUOTE.concat(error).concat(DOUBLEQUOTE));
	}

	/**
	 * Gets the time.
	 *
	 * @param startDate the start date
	 * @param endDate   the end date
	 * @return the time
	 */
	public static String getTime(String startDate, String endDate) {
		String time = BLANK;
		long[] result = new long[5];
		try {
			Date d1 = null;
			Date d2 = null;
			final int ONE_DAY = 1000 * 60 * 60 * 24;
			final int ONE_HOUR = ONE_DAY / 24;
			final int ONE_MINUTE = ONE_HOUR / 60;
			final int ONE_SECOND = ONE_MINUTE / 60;
			d1 = dateFormat.parse(startDate);
			d2 = dateFormat.parse(endDate);
			long diff = d2.getTime() - d1.getTime();
			long d = diff / ONE_DAY;
			diff %= ONE_DAY;
			long h = diff / ONE_HOUR;
			diff %= ONE_HOUR;
			long m = diff / ONE_MINUTE;
			diff %= ONE_MINUTE;
			long s = diff / ONE_SECOND;
			long ms = diff % ONE_SECOND;
			result[0] = d;
			result[1] = h;
			result[2] = m;
			result[3] = s;
			result[3] = ms;
			time = result[0] + " Day " + result[1] + " Hr " + result[2] + " m " + result[3] + " s " + result[4] + " ms";

		} catch (Exception e) {
			logger.error(e);
		}
		return time;
	}

	/**
	 * Gets the web element.
	 *
	 * @param driver the driver
	 * @param xpath  the xpath
	 * @return the web element
	 * @throws InterruptedException
	 */
	public static WebElement getWebElement(WebDriver driver, String xpath) throws InterruptedException {
		WebElement el = null;
		for (int i = 0; i < noOfRetires; i++) {
			try {
				el = driver.findElement(By.xpath(xpath));
			} catch (Exception e) {
				logger.error(e);
			}
			if (el != null) {
				break;
			}
			TimeUnit.SECONDS.sleep(1);
		}
		return el;
	}

	/**
	 * Gets the localization report data.
	 *
	 * @param stepDetail    the step detail
	 * @param reportLangKey the report lang key
	 * @return the localization report data
	 */
	public static String getLocalizationReportData(StepDetail stepDetail, String reportLangKey) {
		String langValue = BLANK;
		if (reportLocalizationMap != null && reportLocalizationMap.get(reportLangKey) != null) {
			langValue = reportLocalizationMap.get(reportLangKey);
		} else {
			logger.info("Report Localization data not found for" + reportLangKey);
		}
		if (langValue.isEmpty() && stepDetail.getStepAction().equals(VERIFY)) {
			langValue = "Verify value in field";
		} else if (langValue.isEmpty() && stepDetail.getStepAction().equals(BLANK)) {
			langValue = "Set value in field";
		}
		return langValue;
	}

	/**
	 * Check dependent tc status.
	 *
	 * @param dependTcId the depend tc id
	 * @return true, if successful
	 */
	public static boolean checkDependentTcStatus(String dependTcId) {
		boolean check = true;
		try {
			for (String key : executionThreadDetailsMap.keySet()) {
				LinkedHashMultimap<String, SuiteDetail> threadSuiteDetails = executionThreadDetailsMap.get(key)
						.getSuiteDetails();
				if (threadSuiteDetails != null) {
					for (String suiteName : threadSuiteDetails.keySet()) {
						for (SuiteDetail suiteDetail : threadSuiteDetails.get(suiteName)) {
							LinkedHashMultimap<String, TestCaseDetail> testCaseList = suiteDetail.getTestCaseList();
							if (testCaseList != null) {
								for (String suite : testCaseList.keySet()) {
									for (TestCaseDetail testCaseDetail : testCaseList.get(suite)) {
										if (testCaseDetail.getTestCaseId().equalsIgnoreCase(dependTcId)
												&& !testCaseDetail.getTestCaseStatus().equals(PASS)) {
											check = false;
											break;
										}
									}

								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("error while dependency check" + e);
		}
		return check;
	}

	/**
	 * Replace value with run time data.
	 *
	 * @param inputData the input data
	 * @return the string
	 */
	public static String replaceValueWithRunTimeData(String inputData) {

		for (String key : runTimeDataMap.keySet()) {
			inputData = inputData.replace(key, runTimeDataMap.get(key));
		}
		return inputData;
	}

	/**
	 * Take screen shot.
	 *
	 * @param commonUtilsDriver the common utils driver
	 * @return the string
	 */
	public static String takeScreenShot(WebDriver commonUtilsDriver) {
		File destFile = null;
		String filePath = null;
		try {
			int heightTotal = 0;
			Date now = new Date();

			String currentTimeStamp = dateFormat.format(now).replace(COLON, BLANK).replace(HYPHEN, BLANK)
					.replace(SPACE, UNDERSCORE).replace(DOT, UNDERSCORE);
			File screenShotpath = new File(executionresultDir.concat(File.separator).concat("Screenshots"));

			if (!screenShotpath.exists()) {
				screenShotpath.mkdir();
			}

			String currentWindow = commonUtilsDriver.getWindowHandle();
			commonUtilsDriver.switchTo().window(currentWindow);
			double contentHeight = ((Long) ((JavascriptExecutor) commonUtilsDriver)
					.executeScript("return window.innerHeight")).doubleValue();
			double docHeight = ((Long) ((JavascriptExecutor) commonUtilsDriver)
					.executeScript("return document.body.scrollHeight")).doubleValue();
			double initialScrollPos = ((Long) ((JavascriptExecutor) commonUtilsDriver)
					.executeScript("return window.pageYOffset;")).doubleValue();
			int scrollBackPos = (int) (docHeight - initialScrollPos);
			int scrollPos = (int) initialScrollPos;
			int noOfScroll = (int) Math.ceil((docHeight / contentHeight));
			BufferedImage[] images = new BufferedImage[noOfScroll];
			TakesScreenshot screenshottingDriver = (TakesScreenshot) commonUtilsDriver;
			for (int i = 0; i < noOfScroll; i++) {
				File file = screenshottingDriver.getScreenshotAs(OutputType.FILE);
				images[i] = ImageIO.read(file);
				FileUtils.forceDelete(file);
				scrollPos = (int) (scrollPos + contentHeight);
				((JavascriptExecutor) commonUtilsDriver).executeScript("window.scrollBy(0, " + scrollPos + ")", "");
			}
			for (int j = 0; j < images.length; j++) {
				heightTotal += images[j].getHeight();
			}
			int heightCurr = 0;
			BufferedImage concatImage = new BufferedImage(images[0].getWidth(), heightTotal,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = concatImage.createGraphics();
			for (int j = 0; j < images.length; j++) {
				g2d.drawImage(images[j], 0, heightCurr, null);
				heightCurr += images[j].getHeight();
			}
			g2d.dispose();
			((JavascriptExecutor) commonUtilsDriver).executeScript("window.scrollBy(0, -" + scrollBackPos + ")", "");

			destFile = new File(
					screenShotpath.toString().concat(File.separator).concat(currentTimeStamp).concat(".png"));
			ImageIO.write(concatImage, "png", destFile);
			filePath = File.separator.concat(resultFolderName).concat(File.separator).concat("Screenshots")
					.concat(File.separator).concat(currentTimeStamp).concat(".png");

		} catch (Exception e) {
			logger.error("Error while takePageScreenshot" + e);
		}
		return filePath;
	}

	/**
	 * Take screen shot.
	 *
	 * @param commonUtilsDriver the common utils driver
	 * @param alert             the alert
	 * @return the string
	 */
	public static String takeScreenShot(WebDriver commonUtilsDriver, Alert alert) {
		File destFile = null;
		String filePath = null;
		try {
			Date now = new Date();
			String currentTimeStamp = dateFormat.format(now).replace(COLON, BLANK).replace(HYPHEN, BLANK)
					.replace(SPACE, UNDERSCORE).replace(DOT, UNDERSCORE);
			File screenShotpath = new File(executionresultDir.concat(File.separator).concat("Screenshots"));
			if (!screenShotpath.exists()) {
				screenShotpath.mkdir();
			}
			if (alert != null) {
				Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
				BufferedImage image = new Robot().createScreenCapture(screenRect);

				destFile = new File(
						screenShotpath.toString().concat(File.separator).concat(currentTimeStamp).concat(".png"));
				ImageIO.write(image, "png", destFile);
				filePath = File.separator.concat(resultFolderName).concat(File.separator).concat("Screenshots")
						.concat(File.separator).concat(currentTimeStamp).concat(".png");
			}
		} catch (Exception e) {
			logger.error("Error while takePageScreenshot" + e);
		}
		return filePath;
	}

	/**
	 * Take element screenshot.
	 *
	 * @param commonUtilsDriver the common utils driver
	 * @param main2             the main 2
	 * @return the string
	 */
	public String takeElementScreenshot(WebDriver commonUtilsDriver, WebElement main2) {
		File destFile = null;
		try {
			WebElement main = commonUtilsDriver.findElement(By.className("home"));
			int heightTotal = 0;
			Date now = new Date();
			String currentTimeStamp = dateFormat.format(now).replace(COLON, BLANK).replace(HYPHEN, BLANK)
					.replace(SPACE, UNDERSCORE).replace(DOT, UNDERSCORE);
			File screenShotpath = new File(executionresultDir.concat(File.separator).concat("Screenshots"));
			if (!screenShotpath.exists()) {
				screenShotpath.mkdir();
			}
			// String currentWindow = commonUtilsDriver.getWindowHandle();
			// commonUtilsDriver.switchTo().window(currentWindow);
			// main.getSize().getHeight()
			// double contentHeight = ((Long) ((JavascriptExecutor) commonUtilsDriver)
			// .executeScript("return arguments[0].offsetHeight",main)).doubleValue();
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			// int width = gd.getDisplayMode().getWidth();
			int contentHeight = gd.getDisplayMode().getHeight() - 200;
			contentHeight = commonUtilsDriver.manage().window().getSize().getHeight();
			double docHeight = ((Long) ((JavascriptExecutor) commonUtilsDriver)
					.executeScript("return arguments[0].scrollHeight;", main)).doubleValue();
			int scrollPos = 0;
			int noOfScroll = (int) Math.ceil((docHeight / contentHeight));
			BufferedImage[] images = new BufferedImage[noOfScroll];
			TakesScreenshot screenshottingDriver = (TakesScreenshot) commonUtilsDriver;
			((JavascriptExecutor) commonUtilsDriver).executeScript("window.scrollBy(0, " + scrollPos + ")", "");

			for (int i = 0; i < noOfScroll; i++) {
				File file = screenshottingDriver.getScreenshotAs(OutputType.FILE);
				images[i] = ImageIO.read(file);
				FileUtils.forceDelete(file);
				scrollPos = (int) (scrollPos + contentHeight);
				((JavascriptExecutor) commonUtilsDriver).executeScript("window.scrollBy(0, " + scrollPos + ")", "");
			}
			for (int j = 0; j < images.length; j++) {
				heightTotal += images[j].getHeight();
			}
			int heightCurr = 0;
			BufferedImage concatImage = new BufferedImage(images[0].getWidth(), heightTotal,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = concatImage.createGraphics();
			for (int j = 0; j < images.length; j++) {
				g2d.drawImage(images[j], 0, heightCurr, null);
				heightCurr += images[j].getHeight();
			}
			g2d.dispose();
			destFile = new File(
					screenShotpath.toString().concat(File.separator).concat(currentTimeStamp).concat(".png"));
			ImageIO.write(concatImage, "png", destFile);
		} catch (Exception e) {
			logger.error("Error while takePageScreenshot" + e);
		}
		return destFile.toString();
	}

	/**
	 * Take element screen shot using ashot.
	 *
	 * @param commonUtilsDriver the common utils driver
	 * @param element           the element
	 * @return the string
	 */
	public String takeElementScreenShotUsingAshot(WebDriver commonUtilsDriver, WebElement element) {
		File destFile = null;
		try {
			// int heightTotal = 0;
			Date now = new Date();
			String filename = dateFormat.format(now).replace(COLON, BLANK).replace(HYPHEN, BLANK)
					.replace(SPACE, UNDERSCORE).replace(DOT, UNDERSCORE) + ".png";
			File screenShotpath = new File(executionresultDir.concat(File.separator).concat("Screenshots"));
			if (!screenShotpath.exists()) {
				screenShotpath.mkdir();
			}
			// String currentWindow = commonUtilsDriver.getWindowHandle();
			// commonUtilsDriver.switchTo().window(currentWindow);
			Screenshot screenshot = new AShot().coordsProvider(new WebDriverCoordsProvider())
					.takeScreenshot(commonUtilsDriver, element);
			destFile = new File(screenShotpath.toString().concat(File.separator).concat(filename));
			ImageIO.write(screenshot.getImage(), "png", destFile);
		} catch (Exception e) {
			logger.error("Error while takeElementScreenShotUsingAshot" + e);
		}
		return destFile.toString();
	}

	/**
	 * Wait for ajax.
	 *
	 * @param commonUtilsDriver the common utils driver
	 * @throws InterruptedException the interrupted exception
	 */
	public static void waitForAjax(WebDriver commonUtilsDriver) throws InterruptedException {
		try {

			ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver commonUtilsDriver) {
					return ((JavascriptExecutor) commonUtilsDriver).executeScript("return document.readyState")
							.toString().equals("complete");
				}
			};
			try {
				WebDriverWait wait = new WebDriverWait(commonUtilsDriver, 30);
				wait.until(expectation);
			} catch (Throwable error) {
				logger.error("Timeout waiting for Page Load Request to complete.");
			}
			while (true) {
				try {
					Boolean ajaxIsComplete = (Boolean) ((JavascriptExecutor) commonUtilsDriver)
							.executeScript("return jQuery.active == 0");
					if (ajaxIsComplete) {
						break;
					}
					Thread.sleep(150);
				} catch (Exception e) {
					logger.debug(e);
					break;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Find alert.
	 *
	 * @param commonUtilsDriver the common utils driver
	 * @return the alert
	 */
	public static Alert findAlert(WebDriver commonUtilsDriver) {
		Alert alert = null;
		try {
			alert = commonUtilsDriver.switchTo().alert();
		} catch (Exception e) {
			logger.error("No Alert found");
		}
		return alert;
	}

	/**
	 * Execute custom method.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void executeCustomMethod(StepDetail stepDetail, WebDriver driver) {
		setStepStartDetails(stepDetail, stepDetail.getComponentKeyword(),
				getLocalizationReportData(stepDetail, "$excuteCustomMethod"));
		try {
			boolean executed = false;
			String[] keywordArray = stepDetail.getComponentKeyword().split("\\.");
			String method = keywordArray[keywordArray.length - 1];
			String className = stepDetail.getComponentKeyword().replace(DOT.concat(method), BLANK);
			Class<?> c = Class.forName(className);
			Object obj = c.newInstance();
			Method methods[] = obj.getClass().getMethods();
			for (Method methodName : methods) {
				if (!methodName.toString().isEmpty()&& methodName.getName().equals(method)) {
					methodName.invoke(obj,stepDetail, driver);
					executed = true;
				}
			}
			if (!executed) {
				setFailDetails(stepDetail, stepDetail.getComponentKeyword() + ":- Method not Found",
						driver);
			}
		} catch (Exception e) {
			setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			setStepEndDetails(stepDetail);
		}

	}
}