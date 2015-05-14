package nl.tudelft.dnainator.ui.controllers;

import nl.tudelft.dnainator.ui.models.GraphItem;
import nl.tudelft.dnainator.ui.models.ModelItem;
import nl.tudelft.dnainator.ui.views.View;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;

/**
 * Controller class for all graph interaction.
 */
public class ViewController {
	@FXML private View view;
	@FXML private Group group;
	private Scale scale;
	private ModelItem mi;

	@FXML
	private void initialize() {
		this.scale = new Scale();

		mi = new GraphItem();
		mi.setTranslateX(400);
		mi.setTranslateY(400);
		mi.getTransforms().add(scale);

		view.getChildren().add(mi);
	}

	@FXML
	private void onScroll(ScrollEvent e) {
		scale.setX(scale.getX() + (scale.getX() * e.getDeltaY() / 1000));
		scale.setY(scale.getY() + (scale.getY() * e.getDeltaY() / 1000));
		System.out.println("view:  " + view.getLayoutBounds());
		mi.update();
	}

}
