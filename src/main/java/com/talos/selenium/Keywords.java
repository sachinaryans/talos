package com.talos.selenium;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.talos.Init;
import com.talos.pojo.StepDetail;
import com.talos.selenium.utils.CommonUtils;

/**
 * Keywords.
 * @author Sachin
 */
public class Keywords extends Init {

	/**
	 * Sets the text in text box.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void setTextInTextBox(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "setTextInTextBox",
				CommonUtils.getLocalizationReportData(stepDetail, "$setText"));
		try {
			WebElement el = CommonUtils.getWebElement(driver, stepDetail.getXpath());
			if (el != null) {
				el.clear();
				el.sendKeys(stepDetail.getActualData());
			} else {
				CommonUtils.setFailDetails(stepDetail, "Element not found", driver);
			}

		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}

	/**
	 * Select radio button.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void selectRadioButton(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "selectRadioButton",
				CommonUtils.getLocalizationReportData(stepDetail, "$selectRadioButton"));
		boolean found = false;
		try {
			WebElement el = CommonUtils.getWebElement(driver, stepDetail.getXpath());
			if (el != null) {
				for (WebElement option : el.findElements(By.tagName("input"))) {
					if (option.getText().equals(stepDetail.getActualData())
							|| option.getAttribute("value").equals(stepDetail.getActualData())) {
						option.click();
						found = true;
						break;
					}
				}
			} else {
				CommonUtils.setFailDetails(stepDetail, "Element not found", driver);
			}
			if (!found) {
				CommonUtils.setFailDetails(stepDetail, stepDetail.getActualData() + " Option not found", driver);
			}

		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}

	/**
	 * Select value in select.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void selectValueInSelect(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "selectValueInSelect",
				CommonUtils.getLocalizationReportData(stepDetail, "$selectValueInSelect"));
		try {
			WebElement el = CommonUtils.getWebElement(driver, stepDetail.getXpath());
			if (el != null) {
				el.click();
				TimeUnit.MILLISECONDS.sleep(300);
				for (WebElement option : el.findElements(By.tagName("option"))) {
					if (option.getText().equals(stepDetail.getActualData())
							|| option.getAttribute("innerText").equals(stepDetail.getActualData())) {
						option.click();
					}
				}
			} else {
				CommonUtils.setFailDetails(stepDetail, "Element not found", driver);
			}

		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}

	/**
	 * Select checkbox.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void selectCheckbox(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "selectCheckbox",
				CommonUtils.getLocalizationReportData(stepDetail, "$selectCheckbox"));
		try {
			WebElement el = CommonUtils.getWebElement(driver, stepDetail.getXpath());
			if (el != null && (stepDetail.getActualData().equalsIgnoreCase("yes")
					|| stepDetail.getActualData().equalsIgnoreCase("y"))) {
				el.click();
			} else {
				CommonUtils.setFailDetails(stepDetail, "Element not found", driver);
			}

		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}

	/**
	 * Click on button.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void clickOnButton(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "clickOnButton",
				CommonUtils.getLocalizationReportData(stepDetail, "$clickOnButton"));
		try {
			WebElement el = CommonUtils.getWebElement(driver, stepDetail.getXpath());
			if (el != null) {
				el.click();
			} else {
				CommonUtils.setFailDetails(stepDetail, "Element not found", driver);
			}

		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}

	/**
	 * Select file.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void selectFile(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "selectFile",
				CommonUtils.getLocalizationReportData(stepDetail, "$selectFile"));
		try {
			WebElement el = CommonUtils.getWebElement(driver, stepDetail.getXpath());
			if (el != null) {
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", el);
				TimeUnit.MILLISECONDS.sleep(1500);
				Robot robot = new Robot();
				StringSelection stringSelection = new StringSelection(stepDetail.getActualData());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, stringSelection);
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				TimeUnit.SECONDS.sleep(1);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
			} else {
				CommonUtils.setFailDetails(stepDetail, "Element not found", driver);
			}

		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}

	/**
	 * Navigate menu.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void navigateMenu(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "navigateMenu",
				CommonUtils.getLocalizationReportData(stepDetail, "$navigateMenu"));
		try {
			WebElement el = CommonUtils.getWebElement(driver, stepDetail.getXpath());
			boolean menuFound = false;
			if (el != null) {
				for (String menu : stepDetail.getActualData().split(delimiter)) {
					innerLoop: for (WebElement el1 : el.findElements(By.tagName("button"))) {
						if (el1.getText().equals(menu) || el1.getAttribute("innerText").equals(menu)) {
							el1.click();
							menuFound = true;
							TimeUnit.MILLISECONDS.sleep(300);
							break innerLoop;
						}
					}
				}
			}

			if (!menuFound) {
				CommonUtils.setFailDetails(stepDetail, "menu not found", driver);
			}

		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}

	/**
	 * Navigate to url.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void navigateToUrl(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "navigateToUrl",
				CommonUtils.getLocalizationReportData(stepDetail, "$navigateUrl"));
		try {
			driver.navigate().to(stepDetail.getActualData());
			CommonUtils.waitForAjax(driver);
		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}

	/**
	 * Handle alert.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void handleAlert(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "handleAlert",
				CommonUtils.getLocalizationReportData(stepDetail, "$handleAlert"));
		try {
			Alert alert = driver.switchTo().alert();
			String[] valArr = stepDetail.getActualData().split(delimiter);
			if (alert != null) {
				if (!valArr[0].equalsIgnoreCase(alert.getText())
						&& (!valArr[0].equalsIgnoreCase("ok") || !valArr[0].equalsIgnoreCase("cancel"))) {
					CommonUtils.setFailDetails(stepDetail,
							"actutal data:-" + alert.getText() + " Expected Data:-" + valArr[0], driver, alert);
				}
				if (valArr.length == 1) {
					if (valArr[0].equalsIgnoreCase("ok")) {
						alert.accept();
					}
					if (valArr[0].equalsIgnoreCase("cancel")) {
						alert.dismiss();
					}
				} else if (valArr.length == 2) {
					if (valArr[1].equalsIgnoreCase("ok")) {
						alert.accept();
					}
					if (valArr[1].equalsIgnoreCase("cancel")) {
						alert.dismiss();
					}
				} else {
					alert.accept();
				}
			}
		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}

	/**
	 * Verify text in text box.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void verifyTextInTextBox(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "verifyTextInTextBox",
				CommonUtils.getLocalizationReportData(stepDetail, "$verifyValue"));
		String actualValue = BLANK;
		try {
			WebElement el = CommonUtils.getWebElement(driver, stepDetail.getXpath());
			if (el != null) {
				actualValue = el.getText().isEmpty() ? el.getAttribute("value") : el.getText();
				if (!actualValue.equals(stepDetail.getActualData())) {
					CommonUtils.setFailDetails(stepDetail,
							"Actual Value :-" + actualValue + " Expected Data :-" + stepDetail.getActualData(), driver);
				}

			} else {
				CommonUtils.setFailDetails(stepDetail, "Element not found", driver);
			}

		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}

	/**
	 * Verify radio button.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void verifyRadioButton(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "verifyRadioButton",
				CommonUtils.getLocalizationReportData(stepDetail, "$verifyValue"));
		boolean found = false;
		try {
			WebElement el = CommonUtils.getWebElement(driver, stepDetail.getXpath());
			if (el != null) {
				for (WebElement option : el.findElements(By.tagName("input"))) {
					if ((option.getText().equals(stepDetail.getActualData())
							|| option.getAttribute("value").equals(stepDetail.getActualData()))
							&& option.isSelected()) {
						found = true;
						break;
					}
				}

			} else {
				CommonUtils.setFailDetails(stepDetail, "Element not found", driver);
			}
			if (!found) {
				CommonUtils.setFailDetails(stepDetail, "Value not checked " + stepDetail.getActualData(), driver);
			}

		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}

	/**
	 * Verify value in select.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void verifyValueInSelect(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "verifyValueInSelect",
				CommonUtils.getLocalizationReportData(stepDetail, "$verifyValue"));
		String actualValue = BLANK;
		try {
			WebElement el = CommonUtils.getWebElement(driver, stepDetail.getXpath());
			if (el != null) {
				Select select = new Select(el);
				WebElement option = select.getFirstSelectedOption();
				actualValue = option.getText();
				if (!actualValue.equals(stepDetail.getActualData())) {
					CommonUtils.setFailDetails(stepDetail,
							"Actual Value :-" + actualValue + " Expected Data :-" + stepDetail.getActualData(), driver);
				}

			} else {
				CommonUtils.setFailDetails(stepDetail, "Element not found", driver);
			}

		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}

	/**
	 * Verify checkbox.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void verifyCheckbox(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "verifyCheckbox",
				CommonUtils.getLocalizationReportData(stepDetail, "$verifyValue"));

		try {
			WebElement el = CommonUtils.getWebElement(driver, stepDetail.getXpath());
			if (el != null) {
				if (el.getAttribute("checked").equalsIgnoreCase(TRUE)
						&& (stepDetail.getActualData().equalsIgnoreCase("no")
								|| stepDetail.getActualData().equalsIgnoreCase("n"))) {
					CommonUtils.setFailDetails(stepDetail, "Element is Checked", driver);
				}
				if (el.getAttribute("checked").equalsIgnoreCase(FALSE)
						&& (stepDetail.getActualData().equalsIgnoreCase("yes")
								|| stepDetail.getActualData().equalsIgnoreCase("y"))) {
					CommonUtils.setFailDetails(stepDetail, "Element is not Checked", driver);
				}

			} else {
				CommonUtils.setFailDetails(stepDetail, "Element not found", driver);
			}

		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}

	/**
	 * Verify table column.
	 *
	 * @param stepDetail the step detail
	 * @param driver the driver
	 */
	public static void verifyTableColumn(StepDetail stepDetail, WebDriver driver) {
		CommonUtils.setStepStartDetails(stepDetail, "verifyTableColumn",
				CommonUtils.getLocalizationReportData(stepDetail, "$verifyValue"));
		String actualValue = BLANK;
		boolean tableElementFound = false;
		WebElement tableEl = null;
		WebElement orgEl = null;
		WebElement trEl = null;
		int colNo = 0;
		String[] inputValArr = stepDetail.getActualData().split(delimiter);
		try {
			WebElement el = CommonUtils.getWebElement(driver, stepDetail.getXpath());
			if (el != null) {
				orgEl = el;
				while (!tableElementFound) {
					WebElement parent = el.findElement(By.xpath("./.."));
					if (parent.getTagName().equalsIgnoreCase("table")) {
						tableEl = parent;
						tableElementFound = true;
					}
					el = parent;
				}
				for (WebElement thEl : tableEl.findElements(By.tagName("th"))) {
					if (orgEl.equals(thEl)) {
						break;
					} else {
						colNo++;
					}
				}
				for (WebElement tr : tableEl.findElements(By.tagName("tr"))) {
					if (tr.getText().contains(inputValArr[0])) {
						trEl = tr;
						break;
					}
				}
				actualValue = trEl.findElements(By.tagName("td")).get(colNo).getText().isEmpty()
						? trEl.findElements(By.tagName("td")).get(colNo).getAttribute("innerText")
						: trEl.findElements(By.tagName("td")).get(colNo).getText();
				if (!actualValue.equals(inputValArr[1])) {
					CommonUtils.setFailDetails(stepDetail,
							"Actual Value :-" + actualValue + " Expected Data :-" + inputValArr[1], driver);
				}

			} else {
				CommonUtils.setFailDetails(stepDetail, "Element not found", driver);
			}

		} catch (Exception e) {
			CommonUtils.setFailDetails(stepDetail, e.toString(), driver);
		} finally {
			CommonUtils.setStepEndDetails(stepDetail);
		}
	}
}