package nl.tudelft.dnainator.ui.views;

import nl.tudelft.dnainator.ui.models.GraphModel;
import javafx.scene.Group;

/**
 * This abstract class is the View part of the MVC pattern.
 * <p>
 * Each extending class defines its own view on the data.
 * </p>
 */
public abstract class View extends Group {
	protected GraphModel model;

	/**
	 * Creates a new view instance.
	 * @param model The {@link GraphModel} whose data to display.
	 */
	public View(GraphModel model) {
		this.model = model;
	}

	/**
	 * Redraws this view's contents.
	 */
	public abstract void redraw();
}
