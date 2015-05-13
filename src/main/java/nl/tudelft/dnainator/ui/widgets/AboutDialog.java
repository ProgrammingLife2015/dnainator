package nl.tudelft.dnainator.ui.widgets;

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
	private static final String ICON = "/ui/icons/dnainator.png";
	
	
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
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		Image icon = new Image(this.getClass().getResourceAsStream(ICON));
		stage.getIcons().add(icon);
		
		ImageView img = new ImageView();
		img.setImage(icon);
		
		// temp resizing of the logo
		// CHECKSTYLE.OFF: MagicNumber
		img.setFitHeight(100);
		img.setFitWidth(100);
		// CHECKSTYLE.ON: MagicNumber
		img.setPreserveRatio(true);
		img.setSmooth(true);
		dialog.setGraphic(img);
		
		dialog.setHeaderText(null);
		dialog.setContentText("Hello!");
		
		ButtonType close = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().add(close);

		
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
	
	/**
	 * Closes the {@link dialog} if it is not null.
	 */
	public void close() {
		if (dialog != null) {
			dialog.close();
		}
	}

}
