package nl.tudelft.dnainator.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
	private static final String ICON = "/ui/icons/dnainator.png";

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(DNAINATOR);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(ICON)));
		primaryStage.setMaximized(true);
		try {
			BorderPane rootLayout = FXMLLoader.load(getClass().getResource(FXML));
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
		primaryStage.show();
	}
}
