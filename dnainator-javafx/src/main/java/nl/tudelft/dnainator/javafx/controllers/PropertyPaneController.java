package nl.tudelft.dnainator.javafx.controllers;

import nl.tudelft.dnainator.javafx.widgets.PropertyType;
import nl.tudelft.dnainator.javafx.widgets.Propertyable;
import nl.tudelft.dnainator.javafx.widgets.animations.LeftSlideAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.SlidingAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.TransitionAnimation.Position;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * Controls the property pane on the right side of the application.
 * It provides information about selected strain data.
 */
public class PropertyPaneController {
	private SlidingAnimation animation;
	private static final int WIDTH = 300;
	private static final int ANIM_DURATION = 250;
	private static final int MAX_PROPERTY_SIZE = 750;

	@SuppressWarnings("unused") @FXML private VBox propertyPane;

	/*
	 * Sets up the animation for the property pane.
	 */
	@SuppressWarnings("unused") @FXML
	private void initialize() {
		animation = new LeftSlideAnimation(propertyPane, WIDTH, ANIM_DURATION, Position.RIGHT);
	}

	/**
	 * Updates the displayed information.
	 * @param p The new {@link Propertyable} whose information to display.
	 */
	public void update(Propertyable p) {
		propertyPane.getChildren().clear();
		p.getPropertyMap().forEach((k, v) -> addLabel(k, v));
	}

	private void addLabel(PropertyType type, String value) {
		Label description = new Label(type.description());
		description.getStyleClass().add("property-header");

		Node label = new Label(value);
		label.getStyleClass().add("property-info");
		if (value != null && value.length() > MAX_PROPERTY_SIZE) {
			ScrollPane sp = new ScrollPane(label);
			sp.setFitToWidth(true);
			sp.setPrefHeight(WIDTH);
			label = sp;
		}
		propertyPane.getChildren().add(new VBox(description, label));
	}

	/**
	 * Toggle the sliding animation on the {@link PropertyPane}.
	 */
	public void toggle() {
		animation.toggle();
	}
}
