package com.talos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.talos.constants.StringConstants;
import com.talos.excel.ExcelUtils;
import com.talos.pojo.ComponentDetails;
import com.talos.pojo.ControllerDetail;
import com.talos.pojo.ExecutionThreadDetail;
import com.talos.pojo.StepDetail;

import jscover.Main;

/**
 * The Class Init.
 * @author Sachin
 */
public class Init implements StringConstants {
	
	/** The Constant logger. */
	final static Logger logger = LogManager.getLogger(Init.class);
	
	/** The work dir. */
	public static String workDir = System.getProperty("user.dir");
	
	/** The conf dir. */
	public static String confDir = System.getProperty("user.dir").concat(File.separator).concat("conf");
	
	/** The template dir. */
	public static String templateDir = System.getProperty("user.dir").concat(File.separator).concat("Template");
	
	/** The test scripts dir. */
	public static String testScriptsDir = System.getProperty("user.dir").concat(File.separator).concat("TestScripts");
	
	/** The web driver dir. */
	public static String webDriverDir = System.getProperty("user.dir").concat(File.separator).concat("webdrivers");
	
	/** The result dir. */
	public static String resultDir = System.getProperty("user.dir").concat(File.separator).concat("Results");
	
	/** The os arch. */
	public static String osArch = System.getProperty("os.arch");
	
	/** The implicit wait. */
	public static int implicitWait = 5;
	
	/** The explicit wait. */
	public static int explicitWait = 2;
	
	/** The no of retires. */
	public static int noOfRetires = 1;
	
	/** The step time. */
	public static int stepTime = 100;
	
	/** The delimiter. */
	public static String delimiter = "`";
	
	/** The jscoverage. */
	public static boolean jscoverage = false;
	
	/** The app url. */
	public static String appUrl = BLANK;
	
	/** The component details. */
	public static ComponentDetails componentDetails;
	
	/** The list of thread. */
	public static ArrayList<Thread> listOfThread = new ArrayList<>();
	
	/** The list of webdriver. */
	public static List<WebDriver> listOfWebdriver = new ArrayList<WebDriver>();
	
	/** The date format. */
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
	
	/** The controller details. */
	public static LinkedListMultimap<String, ControllerDetail> controllerDetails = LinkedListMultimap.create();
	
	/** The execution thread details map. */
	public static Map<String, ExecutionThreadDetail> executionThreadDetailsMap = new LinkedHashMap<String, ExecutionThreadDetail>();
	
	/** The total test case. */
	public static int totalTestCase = 0;
	
	/** The total pass test case. */
	public static int totalPassTestCase = 0;
	
	/** The total fail test case. */
	public static int totalFailTestCase = 0;
	
	/** The total skipped test case. */
	public static int totalSkippedTestCase = 0;
	
	/** The total test steps. */
	public static int totalTestSteps = 0;
	
	/** The total fail steps. */
	public static int totalFailSteps = 0;
	
	/** The total skipped steps. */
	public static int totalSkippedSteps = 0;
	
	/** The total pass steps. */
	public static int totalPassSteps = 0;
	
	/** The executionresult dir. */
	public static String executionresultDir = StringConstants.BLANK;
	
	/** The script details map. */
	public static Multimap<String, StepDetail> scriptDetailsMap = LinkedListMultimap.create();
	
	/** The execution completed. */
	public static boolean executionCompleted = false;
	
	/** The is stopped. */
	public static boolean isStopped = false;
	
	/** The report localization map. */
	public static HashMap<String, String> reportLocalizationMap = new HashMap<>();
	
	/** The data localization map. */
	public static HashMap<String, String> dataLocalizationMap = new HashMap<>();
	
	/** The report language. */
	public static String reportLanguage = BLANK;
	
	/** The data language. */
	public static String dataLanguage = BLANK;
	
	/** The run time data map. */
	public static HashMap<String, String> runTimeDataMap = new HashMap<>();
	
	/** The server port. */
	public static int serverPort = 5000;
	
	/** The unique data. */
	public static String uniqueData = BLANK;
	
	/** The result folder name. */
	public static String resultFolderName = BLANK;
	
	/** The jsresult path. */
	public static String jsresultPath = BLANK;
	
	/** The server. */
	private static Thread server;
	
	/** The js covermain. */
	private static Main jsCovermain = new Main();

	/**
	 * Initialize variable.
	 */
	public static void initializeVariable() {
		controllerDetails = LinkedListMultimap.create();
		executionThreadDetailsMap = new LinkedHashMap<String, ExecutionThreadDetail>();
		listOfThread = new ArrayList<>();
		listOfWebdriver = new ArrayList<WebDriver>();
		runTimeDataMap = new HashMap<>();
		reportLocalizationMap = new HashMap<>();
		dataLocalizationMap = new HashMap<>();
		scriptDetailsMap = LinkedListMultimap.create();
		totalTestCase = 0;
		totalPassTestCase = 0;
		totalFailTestCase = 0;
		totalSkippedTestCase = 0;
		totalTestSteps = 0;
		totalFailSteps = 0;
		totalSkippedSteps = 0;
		totalPassSteps = 0;

	}

	/**
	 * Load tool properties.
	 */
	public static void loadToolProperties() {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(confDir.concat(File.separator).concat("tool.properties")));
			implicitWait = Integer.valueOf(prop.getProperty("implicitWait"));
			explicitWait = Integer.valueOf(prop.getProperty("explicitWait"));
			noOfRetires = Integer.valueOf(prop.getProperty("noOfRetires"));
			stepTime = Integer.valueOf(prop.getProperty("stepTime"));
			delimiter = prop.getProperty("delimiter");
			jscoverage = Boolean.valueOf(prop.getProperty("jscoverage"));
			appUrl = prop.getProperty("appUrl");
			reportLanguage = prop.getProperty("reportLanguage");
			dataLanguage = prop.getProperty("dataLanguage");
			serverPort = Integer.parseInt(prop.getProperty("serverPort"));
			uniqueData = String.valueOf(prop.getProperty("uniquedata"));
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Check tool folders.
	 */
	public static void checkToolFolders() {
		try {
			if (!Files.exists(Paths.get(templateDir))) {
				new File(templateDir).mkdir();
			}
			if (!Files.exists(Paths.get(testScriptsDir))) {
				new File(testScriptsDir).mkdir();
			}
			if (!Files.exists(Paths.get(resultDir))) {
				new File(resultDir).mkdir();
			}

			File logFile = new File(workDir.concat(File.separator).concat("log"));
			for (File file : logFile.listFiles()) {
				Files.delete(Paths.get(file.getAbsolutePath()));
			}

		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Load report localization value.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void loadReportLocalizationValue() throws IOException {
		FileInputStream fis = null;
		XSSFWorkbook wb = null;
		try {
			fis = new FileInputStream(new File(confDir.concat(File.separator).concat("ReportLocalization.xlsx")));
			wb = new XSSFWorkbook(fis);
			Sheet sheet = wb.getSheetAt(0);
			int colNo = 1;
			Row row = sheet.getRow(0);
			for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
				if (ExcelUtils.getCellValue(row.getCell(i)).equalsIgnoreCase(reportLanguage)) {
					colNo = i;
					break;
				}
			}
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				if (sheet.getRow(i) != null) {
					reportLocalizationMap.put(ExcelUtils.getCellValue(sheet.getRow(i).getCell(0)),
							ExcelUtils.getCellValue(sheet.getRow(i).getCell(colNo)));
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
	 * Load data localization value.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void loadDataLocalizationValue() throws IOException {
		FileInputStream fis = null;
		XSSFWorkbook wb = null;
		try {
			fis = new FileInputStream(new File(confDir.concat(File.separator).concat("DataLocalization.xlsx")));
			wb = new XSSFWorkbook(fis);
			Sheet sheet = wb.getSheetAt(0);
			int colNo = 1;
			Row row = sheet.getRow(0);
			for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
				if (ExcelUtils.getCellValue(row.getCell(i)).equalsIgnoreCase(dataLanguage)) {
					colNo = i;
					break;
				}
			}
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
				if (sheet.getRow(i) != null) {
					dataLocalizationMap.put(ExcelUtils.getCellValue(sheet.getRow(i).getCell(0)),
							ExcelUtils.getCellValue(sheet.getRow(i).getCell(colNo)));
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
	 * Sets the up once.
	 */
	public static void setUpOnce() {
		logger.info("-->setUpOnce --> ");
		try {
			jsresultPath = executionresultDir.concat(File.separator).concat("jsCover");
			if (!Files.exists(Paths.get(jsresultPath))) {
				new File(jsresultPath).mkdir();
			}
			String[] jsargs;
			// String includeJs = getJsRegexPath("conf//include_js.txt",
			// "--only-instrument-reg="); ,"--log=FINE"
			String excludeJs = getJsRegexPath("conf//exclude_js.txt", "--no-instrument-reg=");
			jsargs = new String[] { "-ws", "--port=3129", "--proxy", "--uri-to-file-matcher=/exclude(.*)",
					"--local-storage", "--uri-to-file-replace=$1", "--report-dir=" + jsresultPath, excludeJs };
			server = new Thread(new Runnable() {
				public void run() {
					jsCovermain.runMain(jsargs);
				}
			});
			server.start();
		} catch (Exception e) {
			logger.error("Error while starting proxy server" + e);
		}
		logger.info("<--setUpOnce <-- ");
	}

	/**
	 * Tear down once.
	 */
	public static void tearDownOnce() {
		logger.info("-->tearDownOnce --> ");
		jsCovermain.stop();
		logger.info("<--tearDownOnce <-- ");
	}

	/**
	 * Gets the js regex path.
	 *
	 * @param file the file
	 * @param pattern the pattern
	 * @return the js regex path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected static String getJsRegexPath(String file, String pattern) throws IOException {
		logger.info("-->getJsRegexPath --> Param = " + file + "," + pattern);
		String line = null;
		BufferedReader bufferedReader = null;
		FileReader fileReader = null;
		StringBuilder failMessage = new StringBuilder();
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				failMessage.append(pattern).append(line).append(BLANK);
			}
		} catch (IOException e) {
			logger.error("Error while getjsregexpath" + e);
		} finally {
			bufferedReader.close();
			fileReader.close();
		}
		logger.info("<--getJsRegexPath <-- ");
		return failMessage.toString();
	}
}
