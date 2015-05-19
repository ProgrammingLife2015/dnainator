package nl.tudelft.dnainator.ui.widgets.dialogs;

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
	private Node parent;
	private ProgressBar progressBar;
	private Service<?> service;

	/**
	 * Sets up the {@link Alert}, using the {@link Service} provided.
	 * When the service has succeeded, the alert is closed.
	 * @param parent The parent Node of this dialog.
	 * @param service The service to be monitored.
	 */
	public ProgressDialog(Node parent, Service<?> service) {
		super(AlertType.NONE);
		this.parent = parent;
		this.service = service;
		setupProgressBar();
		setupAlert();
	}

	private void setupAlert() {
		this.setTitle("DNAinator");
		this.setHeaderText("Loading...");
		this.getButtonTypes().add(ButtonType.CANCEL);

		this.initOwner(parent.getScene().getWindow());
		this.getDialogPane().setContent(progressBar);
	}

	private void setupProgressBar() {
		progressBar = new ProgressBar();
		progressBar.setPrefWidth(PROGRESSBAR_WIDTH);
	}

	/**
	 * Shows the {@link Alert} if it is not null. If the cancel button is pressed,
	 * the database is safely closed.
	 * FIXME: ugly public path
	 */
	public void showDialog() {
		this.showAndWait().filter(response -> response == ButtonType.CANCEL)
				.ifPresent(response -> service.cancel());
	}
}
