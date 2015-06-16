package nl.tudelft.dnainator.javafx.controllers;

import nl.tudelft.dnainator.javafx.widgets.PropertyType;
import nl.tudelft.dnainator.javafx.widgets.Propertyable;
import nl.tudelft.dnainator.javafx.widgets.animations.LeftSlideAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.SlidingAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.TransitionAnimation.Position;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Controls the property pane on the right side of the application.
 * It provides information about selected strain data.
 */
public class PropertyPaneController {
	private SlidingAnimation animation;
	private static final int WIDTH = 300;
	private static final int ANIM_DURATION = 250;

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
		addLabel(p.getTitle(), p.getPropertyMap().get(p.getTitle()));
		p.getPropertyMap().entrySet().stream()
				.filter(e -> e.getKey() != p.getTitle())
				.forEach(e -> addLabel(e.getKey(), e.getValue()));
	}

	private void addLabel(PropertyType type, String value) {
		Region spacer = new Region();
		Label label = new Label(value);
		label.setWrapText(true);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		propertyPane.getChildren().add(new HBox(new Label(type.description()), spacer, label));
	}

	/**
	 * Toggle the sliding animation on the {@link PropertyPane}.
	 */
	public void toggle() {
		animation.toggle();
	}
}
