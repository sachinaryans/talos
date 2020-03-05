package com.talos.selenium.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.talos.Init;

/**
 * FileGenerator.
 * @author Sachin
 */
public class FileGenerator extends Init {
	
	/** The Constant logger. */
	final static Logger logger = LogManager.getLogger(FileGenerator.class);

	/**
	 * Generate result file.
	 */
	public static void generateResultFile() {
		try {
			Velocity.init();
			VelocityContext context = new VelocityContext();
			context.put("executionThreadDetailsMap", executionThreadDetailsMap);
			context.put("scriptDetailsMap", scriptDetailsMap);
			context.put("totalPassTestCase", totalPassTestCase);
			context.put("totalFailTestCase", totalFailTestCase);
			context.put("totalSkippedTestCase", totalSkippedTestCase);
			Writer writer = new FileWriter(new File(executionresultDir.concat(File.separator).concat("index.html")));
			Velocity.mergeTemplate("conf/testResult.vm", "utf-8", context, writer);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}
}