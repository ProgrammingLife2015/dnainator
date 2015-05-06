package nl.tudelft.dnainator.ui.views;

import nl.tudelft.dnainator.ui.models.GraphModel;
import javafx.scene.Group;
import javafx.scene.layout.Pane;

/**
 * This abstract class is the View part of the MVC pattern.
 * <p>
 * Each extending class defines its own view on the data.
 * </p>
 */
public abstract class View extends Group {
	protected Pane root;
	protected GraphModel model;

	/**
	 * Creates a new view instance.
	 * @param root The root node to attach all drawables to.
	 * @param model The {@link GraphModel} whose data to display.
	 */
	public View(Pane root, GraphModel model) {
		this.root = root;
		this.model = model;
	}

	/**
	 * Redraws this view's contents.
	 */
	public abstract void redraw();
}
