package nl.tudelft.dnainator.ui.splashscreen;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A splashscreen to display while the application is initialising.
 */
public class SplashScreen extends Preloader {
	private static final String SPLASH_IMG = "/ui/splashscreen/splashscreen.png";
	private static final int SPLASH_WIDTH = 450;
	private static final int SPLASH_HEIGHT = 301;

	private Stage stage;
	private ProgressBar progressBar;

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(createPreloaderScene());
		stage.show();
	}

	@Override
	public void handleApplicationNotification(PreloaderNotification info) {
		stage.hide();
	}

	private Scene createPreloaderScene() {
		VBox container = new VBox();
		ImageView imageView = new ImageView(new Image(SPLASH_IMG));

		progressBar = new ProgressBar();
		progressBar.setPrefWidth(SPLASH_WIDTH);

		container.getChildren().addAll(imageView, progressBar);
		return new Scene(container, SPLASH_WIDTH, SPLASH_HEIGHT);
	}
}
