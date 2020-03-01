package com.talos.pojo;

import org.openqa.selenium.WebDriver;

import com.google.common.collect.LinkedHashMultimap;

/**
 * ExecutionThreadDetail.
 * @author Sachin
 */
public class ExecutionThreadDetail {
	
	/** The thread name. */
	String threadName;
	
	/** The thread. */
	Thread thread;
	
	/** The browser. */
	String browser;
	
	/** The machine name. */
	String machineName;
	
	/** The execution status. */
	String executionStatus;
	
	/** The depends on. */
	String dependsOn;
	
	/** The thread start time. */
	String threadStartTime;
	
	/** The thread end time. */
	String threadEndTime;
	
	/** The thread time. */
	String threadTime;
	
	/** The status. */
	String status;

	int threadTotalTc;
	int threadPassTc;
	int threadFailTc;
	
	/** The driver. */
	WebDriver driver;
	
	/** The suite details. */
	LinkedHashMultimap<String, SuiteDetail> suiteDetails;

	/**
	 * Gets the thread name.
	 *
	 * @return the thread name
	 */
	public String getThreadName() {
		return threadName;
	}

	/**
	 * Sets the thread name.
	 *
	 * @param threadName the new thread name
	 */
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	/**
	 * Gets the thread.
	 *
	 * @return the thread
	 */
	public Thread getThread() {
		return thread;
	}

	/**
	 * Sets the thread.
	 *
	 * @param thread the new thread
	 */
	public void setThread(Thread thread) {
		this.thread = thread;
	}

	/**
	 * Gets the browser.
	 *
	 * @return the browser
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * Sets the browser.
	 *
	 * @param browser the new browser
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}

	/**
	 * Gets the machine name.
	 *
	 * @return the machine name
	 */
	public String getMachineName() {
		return machineName;
	}

	/**
	 * Sets the machine name.
	 *
	 * @param machineName the new machine name
	 */
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	/**
	 * Gets the execution status.
	 *
	 * @return the execution status
	 */
	public String getExecutionStatus() {
		return executionStatus;
	}

	/**
	 * Sets the execution status.
	 *
	 * @param executionStatus the new execution status
	 */
	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}

	/**
	 * Gets the depends on.
	 *
	 * @return the depends on
	 */
	public String getDependsOn() {
		return dependsOn;
	}

	/**
	 * Sets the depends on.
	 *
	 * @param dependsOn the new depends on
	 */
	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn;
	}

	/**
	 * Gets the thread start time.
	 *
	 * @return the thread start time
	 */
	public String getThreadStartTime() {
		return threadStartTime;
	}

	/**
	 * Sets the thread start time.
	 *
	 * @param threadStartTime the new thread start time
	 */
	public void setThreadStartTime(String threadStartTime) {
		this.threadStartTime = threadStartTime;
	}

	/**
	 * Gets the thread end time.
	 *
	 * @return the thread end time
	 */
	public String getThreadEndTime() {
		return threadEndTime;
	}

	/**
	 * Sets the thread end time.
	 *
	 * @param threadEndTime the new thread end time
	 */
	public void setThreadEndTime(String threadEndTime) {
		this.threadEndTime = threadEndTime;
	}

	/**
	 * Gets the thread time.
	 *
	 * @return the thread time
	 */
	public String getThreadTime() {
		return threadTime;
	}

	/**
	 * Sets the thread time.
	 *
	 * @param threadTime the new thread time
	 */
	public void setThreadTime(String threadTime) {
		this.threadTime = threadTime;
	}

	/**
	 * Gets the driver.
	 *
	 * @return the driver
	 */
	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * Sets the driver.
	 *
	 * @param driver the new driver
	 */
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Gets the suite details.
	 *
	 * @return the suite details
	 */
	public LinkedHashMultimap<String, SuiteDetail> getSuiteDetails() {
		return suiteDetails;
	}

	/**
	 * Sets the suite details.
	 *
	 * @param suiteDetails the suite details
	 */
	public void setSuiteDetails(LinkedHashMultimap<String, SuiteDetail> suiteDetails) {
		this.suiteDetails = suiteDetails;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public int getThreadTotalTc() {
		return threadTotalTc;
	}

	public void setThreadTotalTc(int threadTotalTc) {
		this.threadTotalTc = threadTotalTc;
	}

	public int getThreadPassTc() {
		return threadPassTc;
	}

	public void setThreadPassTc(int threadPassTc) {
		this.threadPassTc = threadPassTc;
	}

	public int getThreadFailTc() {
		return threadFailTc;
	}

	public void setThreadFailTc(int threadFailTc) {
		this.threadFailTc = threadFailTc;
	}

}