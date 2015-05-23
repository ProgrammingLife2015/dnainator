package nl.tudelft.dnainator.ui.splashscreen;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.application.Platform;
import nl.tudelft.dnainator.ui.widgets.dialogs.ExceptionDialog;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A splashscreen to display while the application is initialising.
 */
public class SplashScreen extends Preloader {
	private static final String SPLASH_IMG = "/ui/splashscreen/splashscreen.png";
	private static final String ICON = "/ui/dnainator.iconset/icon_512x512.png";
	private static final String PROPERTIES = "config.properties";
	private static final String STYLE = "/ui/style.css";
	private static final int SPLASH_WIDTH = 620;
	private static final int SPLASH_HEIGHT = 317;

	private Stage stage;
	private ProgressBar progressBar;
	private Properties prop;

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		stage.initStyle(StageStyle.UNDECORATED);
		stage.getIcons().add(new Image(getClass().getResourceAsStream(ICON)));
		stage.setScene(createPreloaderScene());
		stage.show();
	}

	@Override
	public boolean handleErrorNotification(ErrorNotification info) {
		new ExceptionDialog(null, info.getCause(),
				"Could not launch the application!");
		Platform.exit();
		return false;
	}

	@Override
	public void handleApplicationNotification(PreloaderNotification info) {
		stage.hide();
		if (info instanceof ErrorNotification) {
			handleErrorNotification((ErrorNotification) info);
		}
	}

	private Scene createPreloaderScene() {
		VBox container = new VBox();
		container.getStylesheets().add(getClass().getResource(STYLE).toString());
		ImageView imageView = new ImageView(new Image(SPLASH_IMG));

		progressBar = new ProgressBar();
		progressBar.setPrefWidth(SPLASH_WIDTH);
		
		Text version = new Text();
		readProperties();
		version.setText("Version: " + prop.getProperty("version"));
		
		// CHECKSTYLE.OFF: MagicNumber
		version.setTranslateX(167);
		version.setTranslateY(-150);
		// CHECKSTYLE.ON: MagicNumber
		
		version.getStyleClass().add("splash-version");
		container.getChildren().addAll(imageView, progressBar, version);
		
		return new Scene(container, SPLASH_WIDTH, SPLASH_HEIGHT);
	}
	
	private void readProperties() {
		prop = new Properties();
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(PROPERTIES)) {
			prop.load(in);
		} catch (IOException e) {
			new ExceptionDialog(null, e, "Error reading properties.");
		}
	}
}
