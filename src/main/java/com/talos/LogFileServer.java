package com.talos;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * The Class LogFileServer.
 * @author Sachin
 */
public class LogFileServer extends Init implements Runnable {

	/** The client. */
	private Socket client = null;
	
	/** The in client. */
	private BufferedReader inClient = null;
	
	/** The out client. */
	private DataOutputStream outClient = null;
	
	/** The logserver. */
	private ServerSocket logserver;
	
	/** The log thread. */
	Thread logThread;
	
	/** The log thread bool. */
	private volatile boolean logThreadBool = true;
	
	/** The Constant logger. */
	static final Logger logger = Logger.getLogger(LogFileServer.class);

	/**
	 * Instantiates a new log file server.
	 *
	 * @param cl the cl
	 */
	public LogFileServer(Socket cl) {
		client = cl;
	}

	/**
	 * Run.
	 */
	public void run() {
		try {
			System.out.println("The Client " + client.getInetAddress() + ":" + client.getPort() + " is connected");
			System.out.println(client.getInetAddress().getHostAddress());

			inClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			outClient = new DataOutputStream(client.getOutputStream());

			String requestString = inClient.readLine();
			String headerLine = requestString;

			/** sometimes headerLine is null */
			if (headerLine == null)
				return;

			StringTokenizer tokenizer = new StringTokenizer(headerLine);
			String httpMethod = tokenizer.nextToken();
			String httpQueryString = tokenizer.nextToken();

			System.out.println("The HTTP request string is ....");
			while (inClient.ready()) {
				/** Read the HTTP request until the end */
				System.out.println(requestString);
				requestString = inClient.readLine();
			}

			if (httpMethod.equals("GET")) {
				if (httpQueryString.equals("/")) {
					/** return the home page */
					homePage();
				} else if (httpQueryString.contains(".")) {
					FileInputStream fis = null;
					try {
						logger.info("\n File " + httpQueryString);
						String NEW_LINE = "\r\n";
						fis = new FileInputStream("Results\\" + httpQueryString);
						byte b[];
						int x = fis.available();
						b = new byte[x];
						System.out.println(" b size" + b.length);
						byte[] bytes = new byte[16 * 1024];
						outClient.writeBytes(Status.HTTP_200);
						outClient.writeBytes(Headers.SERVER + ": Log Server");
						outClient.writeBytes(Headers.CONTENT_TYPE + ": text/html" + NEW_LINE);
						outClient.writeBytes(Headers.CONNECTION + ": close" + NEW_LINE);

						/** adding the new line between header and body */
						outClient.writeBytes(NEW_LINE);
						int count;
						while ((count = fis.read(bytes)) > 0) {
							outClient.write(bytes, 0, count);
						}
					} catch (Exception e) {
						sendResponse(404, "<b>The Requested resource not found.</b>");
					} finally {
						outClient.flush();
						outClient.close();
						if (fis != null) {
							fis.close();
						}

					}
				} else {
					sendResponse(404, "<b>The Requested resource not found.</b>");
				}
			} else {
				sendResponse(404, "<b>The Requested resource not found.</b>");
			}
		} catch (Exception e) {
			logger.error("Error while get method" + e);
		}
	}

	/**
	 * Method used to compose the response back to the client.
	 *
	 * @param statusCode the status code
	 * @param responseString the response string
	 * @throws Exception the exception
	 */
	public void sendResponse(int statusCode, String responseString) throws Exception {

		String HTML_START = "<html><title>Talos Log Server</title><body style=\"background-color:rgb(27, 27, 27)\">";
		String HTML_END = "</body></html>";
		String NEW_LINE = "\r\n";

		String statusLine = null;
		String serverdetails = Headers.SERVER + ": Log Server";
		String contentLengthLine = null;
		String contentTypeLine = Headers.CONTENT_TYPE + ": text/html" + NEW_LINE;

		if (statusCode == 200)
			statusLine = Status.HTTP_200;
		else
			statusLine = Status.HTTP_404;

		statusLine += NEW_LINE;
		responseString = HTML_START + responseString + HTML_END;
		contentLengthLine = Headers.CONTENT_LENGTH + responseString.length() + NEW_LINE;

		outClient.writeBytes(statusLine);
		outClient.writeBytes(serverdetails);
		outClient.writeBytes(contentTypeLine);
		outClient.writeBytes(contentLengthLine);
		outClient.writeBytes(Headers.CONNECTION + ": close" + NEW_LINE);

		/** adding the new line between header and body */
		outClient.writeBytes(NEW_LINE);

		outClient.writeBytes(responseString);

		outClient.close();
	}

	/**
	 * Method used to compose the home page response to the client.
	 *
	 * @throws Exception the exception
	 */
	public void homePage() throws Exception {
		logger.info("\n Home Page");
		StringBuffer responseBuffer = new StringBuffer();
		responseBuffer.append("<div align=\"center\"><b style=\"color:grey;\">Talos Report Server</b></div>");
		responseBuffer.append("<ul style=\"color:grey;\">Results");
		File results = new File("Results");
		File[] logFolders = results.listFiles();
		Arrays.sort(logFolders, Comparator.comparingLong(File::lastModified).reversed());
		for (File logFolder : logFolders) {
			if (logFolder.exists() && logFolder.isDirectory()) {
				File indexFile = new File("Results\\" + logFolder.getName() + "/index.html");
				if (indexFile.exists()) {
					responseBuffer.append("<li><a _target=\"blank\" href=\"" + logFolder.getName() + "/index.html"
							+ "\">" + logFolder.getName() + "</a></li>");
				} else if (resultFolderName != null && !logFolder.toString().contains(resultFolderName)) {
					FileUtils.deleteDirectory(logFolder);
				}
			}
		}
		responseBuffer.append("</ul><BR><BR>");
		sendResponse(200, responseBuffer.toString());
	}

	/**
	 * Method used to compose the hello page response to the client.
	 */

	/**
	 * class used to store headers constants
	 *
	 */
	private static class Headers {
		
		/** The Constant SERVER. */
		public static final String SERVER = "Server";
		
		/** The Constant CONNECTION. */
		public static final String CONNECTION = "Connection";
		
		/** The Constant CONTENT_LENGTH. */
		public static final String CONTENT_LENGTH = "Content-Length";
		
		/** The Constant CONTENT_TYPE. */
		public static final String CONTENT_TYPE = "Content-Type";
	}

	/**
	 * class used to store status line constants.
	 */
	private static class Status {
		
		/** The Constant HTTP_200. */
		public static final String HTTP_200 = "HTTP/1.1 200 OK";
		
		/** The Constant HTTP_404. */
		public static final String HTTP_404 = "HTTP/1.1 404 Not Found";
	}

	/**
	 * Start.
	 */
	public void start() {
		try {
			logserver = new ServerSocket(serverPort);
			logger.info("Log Server started on port- " + serverPort);
			/**
			 * loop to keep the application alive - a new HTTPServer object is created for
			 * each client
			 */
			logThread = new Thread() {
				public void run() {
					try {
						while (logThreadBool) {
							Socket connected = logserver.accept();
							LogFileServer httpServer = new LogFileServer(connected);
							new Thread(httpServer).start();
						}
					} catch (Exception e) {
						logger.error("Error while logserver start" + e);
					}
				}
			};
			logThread.start();
		} catch (Exception e) {
			logger.error("Error while logserver" + e);
		}
	}

	/**
	 * Stop.
	 */
	public void stop() {
		try {
			logThreadBool = false;
			if (logserver != null) {
				logserver.close();
			}
			logThread.interrupt();
		} catch (Exception e) {
			logger.error("Error while log server stop" + e);
		}
	}
}