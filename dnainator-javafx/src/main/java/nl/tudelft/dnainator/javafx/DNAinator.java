package nl.tudelft.dnainator.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import nl.tudelft.dnainator.javafx.widgets.dialogs.ExceptionDialog;

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
	private static final String FXML = "/fxml/dnainator.fxml";
	private static final String ICON = "/dnainator.iconset/icon_512x512.png";

	private static final int MIN_WIDTH = 300;
	private static final int MIN_HEIGHT = 150;

	@Override
	public void start(Stage primaryStage) throws Exception {
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
				new ExceptionDialog(null, e, "Could not launch the Application!");
			}
		});
	}

	private double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}

	private double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}

}
