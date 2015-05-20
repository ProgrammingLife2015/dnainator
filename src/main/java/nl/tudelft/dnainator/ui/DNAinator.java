package nl.tudelft.dnainator.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;

import java.io.IOException;

/**
 * DNAinator's main window, from which all interaction will occur.
 * <p>
 * The {@link #start(Stage) start} method will automatically load
 * the required fxml files, which in turn instantiates all controllers.
 * </p>
 */
public class DNAinator extends Application {
	private static final String DNAINATOR = "DNAinator";
	private static final String FXML = "/ui/fxml/dnainator.fxml";
	private static final String ICON = "/ui/dnainator.iconset/icon_512x512.png";

	private static final int MIN_WIDTH = 300;
	private static final int MIN_HEIGHT = 150;

	private BooleanProperty ready = new SimpleBooleanProperty(false);

	private void loadDB() {
		Task<Graph> task = new Task<Graph>() {
			@Override
			protected Graph call() throws Exception {
				Graph db = Neo4jSingleton.getInstance().getDatabase();
				ready.set(true);
				return db;
			}
		};

		new Thread(task).start();
	}

	@Override
	public void init() {
		loadDB();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ready.addListener(event -> {
			Platform.runLater(() -> {
				primaryStage.setTitle(DNAINATOR);
				primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON)));
				primaryStage.setMinWidth(MIN_WIDTH);
				primaryStage.setMinHeight(MIN_HEIGHT);

				notifyPreloader(new Preloader.ProgressNotification(1));

				try {
					BorderPane rootLayout = FXMLLoader.load(getClass().getResource(FXML));
					Scene scene = new Scene(rootLayout, getScreenWidth(), getScreenHeight());
					primaryStage.setScene(scene);
					primaryStage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		});
	}

	private double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}

	private double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}

}
