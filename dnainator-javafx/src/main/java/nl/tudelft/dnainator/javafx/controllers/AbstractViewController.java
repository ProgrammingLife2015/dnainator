package nl.tudelft.dnainator.javafx.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import nl.tudelft.dnainator.javafx.views.AbstractView;
import nl.tudelft.dnainator.javafx.widgets.Propertyable;

/**
 * Controller class for all views.
 */
public class AbstractViewController {
	private static final int X_DELTA = 40;
	private static final int Y_DELTA = 20;
	private static final double SCROLLSPEED_INC = 0.3;
	private static final int MAX_SCROLL_FACTOR = 4;

	@SuppressWarnings("unused") @FXML
	private AbstractView view;
	@SuppressWarnings("unused") @FXML
	private Group group;

	private Point2D dragstart;
	private double scrollSpeedFactor;

	@SuppressWarnings("unused") @FXML
	private void onMouseDragged(MouseEvent e) {
		if (e.getButton() == MouseButton.PRIMARY) {
			Point2D end = new Point2D(e.getX(), e.getY());
			Point2D delta = end.subtract(dragstart);
			view.pan(delta);

			dragstart = end;
		}
	}

	@SuppressWarnings("unused") @FXML
	private void onMousePressed(MouseEvent e) {
		scrollSpeedFactor = 1;
		view.requestFocus();
		if (e.getButton() == MouseButton.PRIMARY) {
			dragstart = new Point2D(e.getX(), e.getY());
		}
	}

	@SuppressWarnings("unused") @FXML
	private void onScroll(ScrollEvent e) {
		if (e.getDeltaY() > 0) {
			view.zoomInScroll(e.getX(), e.getY());
		} else if (e.getDeltaY() < 0) {
			view.zoomOutScroll(e.getX(), e.getY());
		}
	}

	@SuppressWarnings("unused") @FXML
	private void onKeyPressed(KeyEvent e) {
		KeyCode key = e.getCode();

		if (key.isArrowKey()) {
			e.consume();
			scrollTo(key);
		} else if (key == KeyCode.PLUS || key == KeyCode.EQUALS) {
			view.zoomIn();
		} else if (key == KeyCode.MINUS) {
			view.zoomOut();
		}
	}

	private void scrollTo(KeyCode keyCode) {
		scrollSpeedFactor = Math.min(scrollSpeedFactor + SCROLLSPEED_INC, MAX_SCROLL_FACTOR);

		switch (keyCode) {
			case LEFT:
				view.pan(new Point2D(X_DELTA * scrollSpeedFactor, 0));
				return;
			case RIGHT:
				view.pan(new Point2D(-X_DELTA * scrollSpeedFactor, 0));
				return;
			case UP:
				view.pan(new Point2D(0, Y_DELTA * scrollSpeedFactor));
				return;
			case DOWN:
				view.pan(new Point2D(0, -Y_DELTA * scrollSpeedFactor));
				return;
			default:
				return;
		}
	}

	@SuppressWarnings("unused") @FXML
	private void onKeyReleased(KeyEvent e) {
		if (e.getCode().isArrowKey()) {
			scrollSpeedFactor = 1;
		}
	}
	
	// TEMPORARY
	@SuppressWarnings("unused") @FXML
	private void onMouseClicked(MouseEvent e) {
		if (e.getButton() == MouseButton.PRIMARY) {
			Node res = e.getPickResult().getIntersectedNode();
			res.setPickOnBounds(true);
			
			if (res != null && res instanceof Propertyable) {
				System.out.println("FXNode: " + ((Propertyable) res).getType());
				view.setLastClicked((Propertyable) res);
			}
		}
	}
}
