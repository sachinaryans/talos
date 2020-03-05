package com.talos.logger;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

// TODO: Auto-generated Javadoc
/**
 * The Class MyCustomLogger.
 *
 * @author Sachin Sharma
 */
@Plugin(
	    name = "MyCustomLogger",
	    category = "Core",
	    elementType = "appender",
	    printObject = true)
public class MyCustomLogger extends AbstractAppender {

	/** The text area. */
	@SuppressWarnings("restriction")
	private static volatile TextArea textArea = null;
	
	/** The rw lock. */
	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	
	/** The read lock. */
	private final Lock readLock = rwLock.readLock();

	/**
	 * Instantiates a new my custom logger.
	 *
	 * @param name the name
	 * @param filter the filter
	 * @param layout the layout
	 * @param ignoreExceptions the ignore exceptions
	 */
	protected MyCustomLogger(String name, Filter filter, Layout<? extends Serializable> layout,
			final boolean ignoreExceptions) {
		super(name, filter, layout, ignoreExceptions);
	}

	/**
	 * Set the target TextArea for the logging information to appear.
	 *
	 * @param textArea the new text area
	 */
	@SuppressWarnings("restriction")
	public static void setTextArea(final TextArea textArea) {
		MyCustomLogger.textArea = textArea;
	}

	/**
	 * Format and then append the loggingEvent to the stored TextArea.
	 *
	 * @param loggingEvent the logging event
	 */
	@SuppressWarnings("restriction")
	public void append(LogEvent loggingEvent) {
		readLock.lock();
		final String message = new String(getLayout().toByteArray(loggingEvent)).replace("\n", "");
		try {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						if (textArea != null) {
							if (textArea.getText().length() == 0) {
								textArea.setText(message);
							} else {
								textArea.selectEnd();
								textArea.insertText(textArea.getText().length(), message);
							}
						}
					} catch (final Throwable t) {
						System.out.println("Unable to append log to text area: " + t.getMessage());
					}
				}
			});
		} catch (final IllegalStateException e) {
			// ignore case when the platform hasn't yet been iniitialized
		} finally {
			readLock.unlock();
		}
	}

	/**
	 * Creates the appender.
	 *
	 * @param name the name
	 * @param layout the layout
	 * @param filter the filter
	 * @return the my custom logger
	 */
	@PluginFactory
	public static MyCustomLogger createAppender(@PluginAttribute("name") String name,
			@PluginElement("Layout") Layout<? extends Serializable> layout,
			@PluginElement("Filter") final Filter filter) {
		if (name == null) {
			LOGGER.error("No name provided for TextAreaAppender");
			return null;
		}
		if (layout == null) {
			layout = PatternLayout.createDefaultLayout();
		}
		return new MyCustomLogger(name, filter, layout, true);
	}

}