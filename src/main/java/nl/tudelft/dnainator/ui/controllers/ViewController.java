package nl.tudelft.dnainator.ui.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import nl.tudelft.dnainator.ui.views.View;

/**
 * Controller class for all graph interaction.
 */
public class ViewController {
	private static final double ZOOM_FACTOR = 1e-3;

	@FXML private View view;
	@FXML private Group group;

	private Point2D dragstart;

	@FXML
	private void onMouseDragged(MouseEvent e) {
		if (e.getButton() == MouseButton.PRIMARY) {
			Point2D end = new Point2D(e.getX(), e.getY());
			Point2D delta = end.subtract(dragstart);
			view.pan(delta);

			dragstart = end;
		}
	}

	@FXML
	private void onMousePressed(MouseEvent e) {
		if (e.getButton() == MouseButton.PRIMARY) {
			dragstart = new Point2D(e.getX(), e.getY());
		}
	}

	@FXML
	private void onScroll(ScrollEvent e) {
		view.zoom(e.getDeltaY() * ZOOM_FACTOR);
	}
}
