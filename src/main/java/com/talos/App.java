package com.talos;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.WebDriver;

import com.talos.constants.StringConstants;
import com.talos.excel.CreateRestTemplate;
import com.talos.logger.MyCustomLogger;
import com.talos.selenium.Browser;
import com.talos.selenium.utils.FileGenerator;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Sachin
 *  This class contain UI elements and methods for calling thread details  
 */
@SuppressWarnings("restriction")
public class App extends Application implements StringConstants {

	/** The logging view. */
	private final TextArea loggingView = new TextArea();
	
	/** The Constant logger. */
	final static Logger logger = LogManager.getLogger(App.class);
	
	/** The generator driver. */
	WebDriver generatorDriver = null;
	
	/** The html parser. */
	HtmlParser htmlParser = new HtmlParser();
	
	/** The x offset. */
	private double xOffset = 0;
	
	/** The y offset. */
	private double yOffset = 0;
	
	/** The executor thread. */
	Thread executorThread;
	
	/** The logth. */
	LogFileServer logth = new LogFileServer(null);

	/**
	 * Instantiates a new app.
	 */
	public App() {
		MyCustomLogger.setTextArea(loggingView);
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {

		Init.loadToolProperties();
		Init.checkToolFolders();
		System.setProperty("logFolder", Init.workDir.concat("/log"));
		// Init.checkToolFolders();
		System.setProperty("threadName", "thread1");
		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		File file = new File(Init.confDir.concat("/log4j.properties"));

		// this will force a reconfiguration
		context.setConfigLocation(file.toURI());
		logger.info("Tool Started");
		launch(args);
	}

	/**
	 * Start.
	 *
	 * @param stage the stage
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage stage) {
		logth = new LogFileServer(null);
		logth.start();
		// Creating a Text object
		Button launchBrowser = new Button();
		Button generateTemplate = new Button();
		Button quit = new Button();
		Button execute = new Button();
		Button pause = new Button();
		Button stop = new Button();
		Label label = new Label();
		Label toolLabel = new Label();
		Hyperlink reports = new Hyperlink();
		reports.setText("Reports");
		reports.setFont(Font.font("Arial", 14));
		reports.underlineProperty().setValue(Boolean.TRUE);
		label.setText("Template Type");
		toolLabel.setText("Talos");
		toolLabel.setText("Talos");
		toolLabel.setStyle("-fx-font-weight: bold");
		toolLabel.setStyle("-fx-font-size: 16");
		toolLabel.setTextFill(Color.RED);
		ComboBox comboBox = new ComboBox(FXCollections.observableArrayList("UI", "Rest"));
		comboBox.getSelectionModel().selectFirst();

		TabPane tabpane = new TabPane();
		Tab templataTab = new Tab("Template");
		Tab executionTab = new Tab("Execution");
		templataTab.setClosable(false);
		executionTab.setClosable(false);
		tabpane.setTranslateX(0);
		tabpane.setTranslateY(-80);
		tabpane.setMaxHeight(80);
		tabpane.setMaxWidth(400);
		VBox executionTabvBox = new VBox();
		VBox templateTabvBox = new VBox();
		execute.setTranslateX(10);
		execute.setTranslateY(10);
		pause.setTranslateX(80);
		pause.setTranslateY(-15);
		stop.setTranslateX(140);
		stop.setTranslateY(-40);
		pause.disableProperty().set(true);
		stop.disableProperty().set(true);
		executionTabvBox.getChildren().add(execute);
		executionTabvBox.getChildren().add(pause);
		executionTabvBox.getChildren().add(stop);
		executionTabvBox.setPrefSize(10, 10);

		label.setTranslateX(0);
		label.setTranslateY(0);
		comboBox.setTranslateX(0);
		comboBox.setTranslateY(5);
		launchBrowser.setTranslateX(80);
		launchBrowser.setTranslateY(-20);
		generateTemplate.setTranslateX(190);
		generateTemplate.setTranslateY(-45);

		templateTabvBox.getChildren().add(label);
		templateTabvBox.getChildren().add(comboBox);
		templateTabvBox.getChildren().add(launchBrowser);
		templateTabvBox.getChildren().add(generateTemplate);

		executionTab.setContent(executionTabvBox);
		templataTab.setContent(templateTabvBox);
		tabpane.getTabs().add(templataTab);
		tabpane.getTabs().add(executionTab);

		// Setting the text to be added.
		launchBrowser.setText("Launch Browser");
		generateTemplate.setText("Generate Template");
		execute.setText("Execute");
		pause.setText("Pause");
		stop.setText("Stop");

		quit.setText("Quit");

		quit.setTranslateX(170);
		quit.setTranslateY(-16);
		reports.setTranslateX(120);
		reports.setTranslateY(-16);

		toolLabel.setTranslateX(-180);
		toolLabel.setTranslateY(-140);
		loggingView.setMaxHeight(150);
		loggingView.setMinHeight(150);
		loggingView.setMinWidth(395);
		loggingView.setMaxWidth(395);
		loggingView.setTranslateX(0);
		loggingView.setTranslateY(74);
		loggingView.setWrapText(true);

		StackPane root = new StackPane();
		root.setStyle("-fx-border-color: black;");

		root.getChildren().add(quit);
		// root.getChildren().add(execute);

		root.getChildren().add(loggingView);
		root.getChildren().add(tabpane);
		root.getChildren().add(toolLabel);
		root.getChildren().add(reports);

		root.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});

		// Creating a scene object
		quit.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("deprecation")
			@Override
			public void handle(ActionEvent event) {
				Task<Void> task = new Task<Void>() {
					@Override
					public Void call() {
						execute.disableProperty().set(true);
						quit.disableProperty().set(true);
						launchBrowser.disableProperty().set(true);
						generateTemplate.disableProperty().set(true);
						if (generatorDriver != null) {
							generatorDriver.close();
							generatorDriver.quit();
						}
						stopThread();
						for (WebDriver driver : Init.listOfWebdriver) {
							driver.close();
							driver.quit();
						}

						return null;
					}
				};
				task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
						logth.stop();
						stage.close();
						LogManager.shutdown();
						System.exit(0);
					}
				});
				Thread th1 = new Thread(task);
				th1.setDaemon(true);
				th1.start();
			}
		});
		launchBrowser.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("deprecation")
			@Override
			public void handle(ActionEvent event) {
				Task<Void> task = new Task<Void>() {
					@Override
					public Void call() {
						launchBrowser.disableProperty().set(true);
						generatorDriver = Browser.launchBrowser("", "");
						return null;
					}
				};
				task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {

						logger.info("\n Browser Launched ");
					}
				});
				Thread th1 = new Thread(task);
				th1.setDaemon(true);
				th1.start();
			}
		});
		generateTemplate.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("deprecation")
			@Override
			public void handle(ActionEvent event) {
				Task<Void> task = new Task<Void>() {
					@Override
					public Void call() {
						if (comboBox.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase("ui")) {
							htmlParser.parse(generatorDriver);
						} else {
							Workbook workbook = CreateRestTemplate.create();
							CreateRestTemplate.saveExcel(workbook,
									Init.templateDir.concat(File.separator).concat("RestTemplate.xlsx"));
						}

						return null;
					}
				};
				task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {

						logger.info("\n Template generated successfully");
					}
				});
				Thread th1 = new Thread(task);
				th1.setDaemon(true);
				th1.start();
			}
		});
		execute.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("deprecation")
			@Override
			public void handle(ActionEvent event) {
				Task<Void> task = new Task<Void>() {
					@Override
					public Void call() throws IOException {
						if (execute.getText().equalsIgnoreCase("Execute")) {
							Init.initializeVariable();
							Init.loadDataLocalizationValue();
							Init.loadReportLocalizationValue();
							launchBrowser.disableProperty().set(true);
							generateTemplate.disableProperty().set(true);
							execute.disableProperty().set(true);
							pause.disableProperty().set(false);
							stop.disableProperty().set(false);
							ExecutorService executorService = new ExecutorService();
							executorThread = new Thread(executorService);
							executorThread.run();
						} else {
							Task<Void> task = new Task<Void>() {
								@Override
								public Void call() {
									pause.disableProperty().set(false);
									execute.disableProperty().set(true);
									playThread();
									return null;
								}
							};
							task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
								@Override
								public void handle(WorkerStateEvent t) {
									logger.info("\n Execution continued");
								}
							});
							Thread th1 = new Thread(task);
							th1.setDaemon(true);
							th1.start();
						}
						return null;
					}
				};
				task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent t) {
						if (Init.executionCompleted) {
							execute.setText("Execute");
							stop.disableProperty().set(true);
							launchBrowser.disableProperty().set(false);
							generateTemplate.disableProperty().set(false);
							execute.disableProperty().set(false);
							pause.disableProperty().set(true);
							logger.info("\n Execute Completed");
						}
					}
				});
				// new Thread(task).start();
				Thread th1 = new Thread(task);
				th1.setDaemon(true);
				th1.start();
			}
		});
		pause.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("deprecation")
			@Override
			public void handle(ActionEvent event) {
				execute.setText("Continue");
				execute.disableProperty().set(false);
				pause.disableProperty().set(true);
				logger.info("\n Execution Paused");
				pauseThread();
			}
		});
		stop.setOnAction(new EventHandler<ActionEvent>() {

			@SuppressWarnings("deprecation")
			@Override
			public void handle(ActionEvent event) {
				Init.isStopped = true;
				stopThread();
				FileGenerator.generateResultFile();
				execute.setText("Execute");
				pause.disableProperty().set(true);
				execute.disableProperty().set(false);
				stop.disableProperty().set(true);
				logger.info("\n Execution stopped");
			}
		});
		reports.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
					URI oURL = new URI(
							"http://" + InetAddress.getLocalHost().getHostName() + ":" + Init.serverPort + "/");
					desktop.browse(oURL);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Scene scene = new Scene(root, 400, 300);

		stage.setTitle("Talos");
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
		stage.setAlwaysOnTop(true);
		stage.setX(900);
		stage.setY(400);
		stage.show();

	}

	/**
	 * Pause thread.
	 */
	public void pauseThread() {
		for (Thread th : Init.listOfThread) {
			try {
				th.suspend();
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	/**
	 * Play thread.
	 */
	public void playThread() {
		for (Thread th : Init.listOfThread) {
			try {
				th.resume();
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	/**
	 * Stop thread.
	 */
	public void stopThread() {
		for (Thread th : Init.listOfThread) {
			try {
				th.stop();
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
}
