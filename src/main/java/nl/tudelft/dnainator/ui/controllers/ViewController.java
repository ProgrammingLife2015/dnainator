package nl.tudelft.dnainator.ui.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import nl.tudelft.dnainator.ui.views.View;

/**
 * Controller class for all graph interaction.
 */
public class ViewController {
	private static final double ZOOM_FACTOR = 1e-3;
	private static final int X_DELTA = 30;
	private static final int Y_DELTA = 15;
	private static final double SCROLLSPEED_INC = 0.3;
	private static final int MAX_SCROLL_FACTOR = 4;

	@FXML private View view;
	@FXML private Group group;

	private Point2D dragstart;
	private double scrollSpeedFactor;

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
		scrollSpeedFactor = 1;
		view.requestFocus();
		if (e.getButton() == MouseButton.PRIMARY) {
			dragstart = new Point2D(e.getX(), e.getY());
		}
	}

	@FXML
	private void onScroll(ScrollEvent e) {
		view.zoom(e.getDeltaY() * ZOOM_FACTOR, new Point2D(e.getX(), e.getY()));
	}

	@FXML
	private void onKeyPressed(KeyEvent e) {
		scrollSpeedFactor = Math.min(scrollSpeedFactor + SCROLLSPEED_INC, MAX_SCROLL_FACTOR);
		Point2D cur = new Point2D(view.getTranslateX(), view.getTranslateY());

		switch (e.getCode()) {
			case LEFT:
				view.pan(cur.add(X_DELTA * scrollSpeedFactor, 0));
				break;
			case RIGHT:
				view.pan(cur.add(-X_DELTA * scrollSpeedFactor, 0));
				break;
			case UP:
				view.pan(cur.add(0, Y_DELTA * scrollSpeedFactor));
				break;
			case DOWN:
				view.pan(cur.add(0, -Y_DELTA * scrollSpeedFactor));
				break;
			default:
				break;
		}
	}

	@FXML
	private void onKeyReleased(KeyEvent e) {
		scrollSpeedFactor = 1;
	}
}
