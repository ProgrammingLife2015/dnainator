package nl.tudelft.dnainator.ui.widgets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Creates a {@link Dialog} displaying information about the
 * application.
 */
public class AboutDialog {
	private Dialog<String> dialog;
	private Properties prop;
	private static final String ICON = "/ui/icons/dnainator128x128.png";
	private static final String STYLE = "/ui/style.css";
	private static final String PROPERTIES = "config.properties";
	
	/**
	 * Instantiates a new AboutDialog.
	 * It will set up all the necessities for displaying 
	 * information of the application.
	 */
	public AboutDialog() {
		setupDialog();
	}

	private void setupDialog() {
		dialog = new Dialog<String>();
		dialog.setTitle("About DNAinator");
		dialog.getDialogPane().getStylesheets().add(getClass().getResource(STYLE).toString());
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		Image icon = new Image(this.getClass().getResourceAsStream(ICON));
		stage.getIcons().add(icon);
		
		ImageView img = new ImageView();
		img.setImage(icon);
		dialog.setGraphic(img);
		readProperties();
		
		dialog.setHeaderText("DNAinator\nDNA network visualization tool");
		dialog.setContentText(contentText());
		
		ButtonType close = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().add(close);
	}

	private String contentText() {
		StringBuilder contents = new StringBuilder();
		contents.append("Version: " + prop.getProperty("version") + "\n");
		contents.append("Jente Hidskes, Gerlof Fokkema, Owen Huang, "
				+ "Skip Lentz, Piet van Agtmaal");
		return contents.toString();
	}
	
	private void readProperties() {
		prop = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES);
		if (inputStream != null) {
			try {
				prop.load(inputStream);
			} catch (IOException e) {
				new ExceptionDialog(e, "Error reading properties");
			}
		}
	}

	/**
	 * Shows the {@link dialog} if it is not null.
	 * The {@link dialog} will block until user input is received. 
	 */
	public void show() {
		if (dialog != null) {
			dialog.showAndWait();
		}
	}
}
