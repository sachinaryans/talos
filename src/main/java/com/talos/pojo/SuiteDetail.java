package com.talos.pojo;

import com.google.common.collect.LinkedHashMultimap;

/**
 * SuiteDetail.
 * @author Sachin
 */
public class SuiteDetail {

	/** The thread. */
	String thread;
	
	/** The suite name. */
	String suiteName;
	
	/** The status. */
	String status;
	
	/** The suite total tc. */
	int suiteTotalTc;
	
	/** The suite total pass tc. */
	int suiteTotalPassTc;
	
	/** The suite total fail tc. */
	int suiteTotalFailTc;
	
	/** The suite total skipped tc. */
	int suiteTotalSkippedTc;
	
	/** The suite start time. */
	String suiteStartTime;
	
	/** The suite end time. */
	String suiteEndTime;
	
	/** The suite time. */
	String suiteTime;
	
	/** The test case list. */
	LinkedHashMultimap<String, TestCaseDetail> testCaseList;

	/**
	 * Gets the thread.
	 *
	 * @return the thread
	 */
	public String getThread() {
		return thread;
	}

	/**
	 * Sets the thread.
	 *
	 * @param thread the new thread
	 */
	public void setThread(String thread) {
		this.thread = thread;
	}

	/**
	 * Gets the suite name.
	 *
	 * @return the suite name
	 */
	public String getSuiteName() {
		return suiteName;
	}

	/**
	 * Sets the suite name.
	 *
	 * @param suiteName the new suite name
	 */
	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
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

	/**
	 * Gets the suite total tc.
	 *
	 * @return the suite total tc
	 */
	public int getSuiteTotalTc() {
		return suiteTotalTc;
	}

	/**
	 * Sets the suite total tc.
	 *
	 * @param suiteTotalTc the new suite total tc
	 */
	public void setSuiteTotalTc(int suiteTotalTc) {
		this.suiteTotalTc = suiteTotalTc;
	}

	/**
	 * Gets the suite total pass tc.
	 *
	 * @return the suite total pass tc
	 */
	public int getSuiteTotalPassTc() {
		return suiteTotalPassTc;
	}

	/**
	 * Sets the suite total pass tc.
	 *
	 * @param suiteTotalPassTc the new suite total pass tc
	 */
	public void setSuiteTotalPassTc(int suiteTotalPassTc) {
		this.suiteTotalPassTc = suiteTotalPassTc;
	}

	/**
	 * Gets the suite total fail tc.
	 *
	 * @return the suite total fail tc
	 */
	public int getSuiteTotalFailTc() {
		return suiteTotalFailTc;
	}

	/**
	 * Sets the suite total fail tc.
	 *
	 * @param suiteTotalFailTc the new suite total fail tc
	 */
	public void setSuiteTotalFailTc(int suiteTotalFailTc) {
		this.suiteTotalFailTc = suiteTotalFailTc;
	}

	/**
	 * Gets the suite total skipped tc.
	 *
	 * @return the suite total skipped tc
	 */
	public int getSuiteTotalSkippedTc() {
		return suiteTotalSkippedTc;
	}

	/**
	 * Sets the suite total skipped tc.
	 *
	 * @param suiteTotalSkippedTc the new suite total skipped tc
	 */
	public void setSuiteTotalSkippedTc(int suiteTotalSkippedTc) {
		this.suiteTotalSkippedTc = suiteTotalSkippedTc;
	}

	/**
	 * Gets the suite start time.
	 *
	 * @return the suite start time
	 */
	public String getSuiteStartTime() {
		return suiteStartTime;
	}

	/**
	 * Sets the suite start time.
	 *
	 * @param suiteStartTime the new suite start time
	 */
	public void setSuiteStartTime(String suiteStartTime) {
		this.suiteStartTime = suiteStartTime;
	}

	/**
	 * Gets the suite end time.
	 *
	 * @return the suite end time
	 */
	public String getSuiteEndTime() {
		return suiteEndTime;
	}

	/**
	 * Sets the suite end time.
	 *
	 * @param suiteEndTime the new suite end time
	 */
	public void setSuiteEndTime(String suiteEndTime) {
		this.suiteEndTime = suiteEndTime;
	}

	/**
	 * Gets the suite time.
	 *
	 * @return the suite time
	 */
	public String getSuiteTime() {
		return suiteTime;
	}

	/**
	 * Sets the suite time.
	 *
	 * @param suiteTime the new suite time
	 */
	public void setSuiteTime(String suiteTime) {
		this.suiteTime = suiteTime;
	}

	/**
	 * Gets the test case list.
	 *
	 * @return the test case list
	 */
	public LinkedHashMultimap<String, TestCaseDetail> getTestCaseList() {
		return testCaseList;
	}

	/**
	 * Sets the test case list.
	 *
	 * @param testCaseList the test case list
	 */
	public void setTestCaseList(LinkedHashMultimap<String, TestCaseDetail> testCaseList) {
		this.testCaseList = testCaseList;
	}
}