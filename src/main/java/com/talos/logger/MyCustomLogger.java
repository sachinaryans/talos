package com.talos.logger;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * The Class MyCustomLogger.
 *
 * @author Sachin Sharma
 */
public class MyCustomLogger extends WriterAppender {

	/** The text area. */
	@SuppressWarnings("restriction")
	private static volatile TextArea textArea = null;

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
	@Override
	public void append(final LoggingEvent loggingEvent) {
		final String message = this.layout.format(loggingEvent);

		// Append formatted message to text area using the Thread.
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
		}
	}
}