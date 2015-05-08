package nl.tudelft.dnainator.ui.widgets;

import javafx.concurrent.Service;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;

public class ProgressDialog {

	public ProgressDialog(Service service) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Loading");
		alert.setHeaderText("Loading...");
		ProgressBar progressBar = new ProgressBar();
		service.setOnSucceeded(e -> alert.close());

		alert.getDialogPane().setContent(progressBar);
		alert.show();
	}
}
