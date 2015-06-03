package nl.tudelft.dnainator.javafx.controllers;

import java.util.List;

import nl.tudelft.dnainator.javafx.widgets.Propertyable;
import nl.tudelft.dnainator.javafx.widgets.animations.LeftSlideAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.SlidingAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.TransitionAnimation.Position;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
	@SuppressWarnings("unused") @FXML private VBox vbox;
	@SuppressWarnings("unused") @FXML private Label propertyTitle;
	
	/*
	 * Sets up the services, filechooser and treeproperty.
	 */
	@SuppressWarnings("unused") @FXML
	private void initialize() {
		propertyPane.getStyleClass().add("property-pane");
		propertyTitle.setId("properties-label");
		animation = new LeftSlideAnimation(propertyPane, WIDTH, ANIM_DURATION, Position.RIGHT);
	}
	
	/**
	 * Updates the displayed information.
	 * @param p The new {@link Propertyable} whose information to display.
	 */
	public void update(Propertyable p) {
		propertyTitle.setText(p.getType());
		vbox.getChildren().clear();

		updateSources(p);
	}

	private void updateSources(Propertyable p) {
		List<String> list = p.getSources();
		if (list == null) {
			return;
		}

		Label id = new Label("Sources");
		id.getStyleClass().add("property-header");
		vbox.getChildren().add(id);

		for (String s : list) {
			vbox.getChildren().add(new Label(s));
		}
	}
	
	/**
	 * Toggle the sliding animation on the {@link PropertyPane}.
	 */
	public void toggle() {
		animation.toggle();
	}
}
