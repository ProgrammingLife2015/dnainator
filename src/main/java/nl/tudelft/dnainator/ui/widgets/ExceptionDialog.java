package nl.tudelft.dnainator.ui.widgets;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Provides a dialog to inform the user that an exception has occurred.
 * <p>
 * Based on code from a tutorial found here: http://code.makery.ch/blog/javafx-dialogs-official/
 * </p>
 */
public class ExceptionDialog {

	/**
	 * Instantiates a new ExceptionDialog.
	 * @param throwable Throwable for which this dialog is created.
	 * @param title Title of this dialog.
	 */
	public ExceptionDialog(Throwable throwable, String title) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(throwable.getMessage());
		alert.setContentText("The exception stacktrace was:");

		TextArea textArea = initTextArea(throwable);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane content = new GridPane();
		content.setMaxWidth(Double.MAX_VALUE);
		content.setMaxHeight(Double.MAX_VALUE);
		content.add(textArea, 0, 1);

		alert.getDialogPane().setExpandableContent(content);
		alert.showAndWait();
	}

	private TextArea initTextArea(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		String exceptionText = sw.toString();

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
//		textArea.setMaxHeight(Double.MAX_VALUE);
		return textArea;
	}
}
