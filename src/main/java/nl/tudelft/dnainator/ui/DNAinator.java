package nl.tudelft.dnainator.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

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

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(DNAINATOR);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON)));

		try {
			BorderPane rootLayout = FXMLLoader.load(getClass().getResource(FXML));
			Scene scene = new Scene(rootLayout, getScreenWidth(), getScreenHeight());
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
		primaryStage.show();
	}

	private double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}

	private double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}

}
