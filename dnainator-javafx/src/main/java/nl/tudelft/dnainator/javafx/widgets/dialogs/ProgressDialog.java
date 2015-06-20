package nl.tudelft.dnainator.javafx.widgets.dialogs;

import javafx.concurrent.Service;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;

/**
 * Creates an {@link Alert} while a file is loading.
 */
public class ProgressDialog extends Alert {
	private static final int PROGRESSBAR_WIDTH = 300;
	private static final String STYLE = "/style.css";
	private Node parent;
	private ProgressBar progressBar;

	/**
	 * Sets up the {@link Alert}, using the {@link Service} provided.
	 * When the service has succeeded, the alert is closed.
	 * @param parent The parent Node of this dialog.
	 */
	public ProgressDialog(Node parent) {
		super(AlertType.NONE);
		this.parent = parent;
		setupProgressBar();
		setupAlert();
	}

	private void setupAlert() {
		this.setTitle("DNAinator");
		this.setHeaderText("Loading...");
		this.getButtonTypes().add(ButtonType.CANCEL);
		this.getDialogPane().getStylesheets().add(getClass().getResource(STYLE).toString());
		this.initOwner(parent.getScene().getWindow());
		this.getDialogPane().setContent(progressBar);
	}

	private void setupProgressBar() {
		progressBar = new ProgressBar();
		progressBar.setPrefWidth(PROGRESSBAR_WIDTH);
	}
}
