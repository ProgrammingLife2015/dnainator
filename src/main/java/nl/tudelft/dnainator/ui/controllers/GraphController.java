package nl.tudelft.dnainator.ui.controllers;

import java.io.IOException;

import nl.tudelft.dnainator.graph.Neo4jGraphDatabase;
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
		try {
			System.out.println(Neo4jGraphDatabase.getInstance().getRank(0));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// CHECKSTYLE.ON: MagicNumber
	}
}
