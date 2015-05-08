package nl.tudelft.dnainator.ui.widgets;

import javafx.concurrent.Service;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;

/**
 * Creates an {@link Alert} while a file is loading.
 */
public class ProgressDialog {
	private static final int PROGRESSBAR_WIDTH = 300;
	private Alert alert;
	private ProgressBar progressBar;
	private Service service;

	/**
	 * Sets up the {@link Alert}, using the {@link Service} provided.
	 * When the service has succeeded, the alert is closed.
	 *
	 * @param service The service to be monitored.
	 */
	public ProgressDialog(Service service) {
		this.service = service;
		setupProgressBar();
		setupAlert();

		this.service.setOnSucceeded(e -> alert.close());
	}

	private void setupAlert() {
		alert = new Alert(Alert.AlertType.NONE);
		alert.setTitle(" ");
		alert.setHeaderText("Loading...");
		alert.getButtonTypes().add(ButtonType.CANCEL);

		alert.getDialogPane().setContent(progressBar);
	}

	private void setupProgressBar() {
		progressBar = new ProgressBar();
		progressBar.setPrefWidth(PROGRESSBAR_WIDTH);
	}

	/**
	 * Closes the {@link Alert} if it is not null.
	 */
	public void close() {
		if (alert != null) {
			alert.close();
		}
	}

	/**
	 * Shows the {@link Alert} if it is not null. If the cancel button is pressed,
	 * the database is safely closed.
	 * FIXME: ugly public path
	 */
	public void show() {
		if (alert != null) {
			alert.showAndWait().filter(response -> response == ButtonType.CANCEL)
					.ifPresent(response -> service.cancel());
		}
	}
}
