package com.talos.utils;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.talos.Init;
import com.talos.pojo.ComponentDetails;
import com.talos.pojo.ComponentDetails.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class Utils.
 * 
 * @author Sachin
 */
public class Utils extends Init {
	/** The Constant logger. */
	final static Logger logger = LogManager.getLogger(Utils.class);

	/**
	 * Load component details.
	 *
	 * @return the component details
	 */
	public static void loadComponentDetails() {
		try {
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			componentDetails = mapper.readValue(new File("conf/componentDetails.yml"), ComponentDetails.class);

		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Find component method.
	 *
	 * @param componentName the component name
	 * @return the string
	 */
	public static String findComponentMethod(String componentName) {
		String method = BLANK;
		for (Component component : componentDetails.getComponentDetails()) {
			if (component.getComponent().equalsIgnoreCase(componentName)) {
				method = component.getMethod();
				break;
			}
		}
		return method;
	}

}
