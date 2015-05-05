package nl.tudelft.dnainator.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

/**
 * Controller class for all graph interaction.
 */
public class GraphController {
	@FXML private Group graph;

	@FXML
	private void onMouseClick(MouseEvent e) {
		// CHECKSTYLE.OFF: MagicNumber
		graph.getChildren().add(new Circle(10));
		// CHECKSTYLE.ON: MagicNumber
	}
}
