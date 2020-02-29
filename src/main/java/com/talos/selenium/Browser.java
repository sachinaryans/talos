package com.talos.selenium;

import java.io.File;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.talos.Init;

/**
 * LaunchBrowser.
 * @author Sachin
 */
public class Browser extends Init {
	
	/** The Constant logger. */
	final static Logger logger = Logger.getLogger(Browser.class);

	/**
	 * Launch browser.
	 *
	 * @param browserType the browser type
	 * @param version the version
	 * @return the web driver
	 */
	public static WebDriver launchBrowser(String browserType, String version) {
		WebDriver driver = null;
		try {
			// System.setProperty("webdriver.firefox.marionette","C:\\geckodriver.exe");
			// WebDriver driver = new FirefoxDriver();
			// comment the above 2 lines and uncomment below 2 lines to use Chrome
			System.setProperty("webdriver.chrome.driver", workDir.concat(File.separator).concat("webdrivers")
					.concat(File.separator).concat("chromedriver.exe"));
			driver = new ChromeDriver();

		} catch (Exception e) {

		}
		return driver;
	}

}