package nl.tudelft.dnainator.javafx.drawables;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import nl.tudelft.dnainator.javafx.drawables.strains.Thresholds;

/**
 * An abstract drawable that enables a semantic display of data.
 */
public abstract class SemanticDrawable extends Group {
	protected Group content;
	protected Group childContent;

	/**
	 * Creates a new {@link SemanticDrawable}.
	 * @param content Top-level content.
	 * @param childContent Lower-level content.
	 */
	public SemanticDrawable(Group content, Group childContent) {
		this.content = content;
		this.childContent = childContent;
		getChildren().addAll(content, childContent);
	}

	/**
	 * Load the {@link Drawable} content of the view itself. Must be called by the constructor of
	 * the overriding class!
	 * @param bounds The bounds of the viewport.
	 */
	protected abstract void loadContent(Bounds bounds);

	/**
	 * Load the drawable content of the view's children, given the bounds of the viewport.
	 * @param bounds The bounds of the viewport.
	 */
	protected abstract void loadChildren(Bounds bounds);

	/**
	 * Check whether this object intersects with the given viewport bounds.
	 * @param bounds	The given viewport bounds.
	 * @return	True if (partially) in viewport, false otherwise.
	 */
	public boolean isInViewport(Bounds bounds) {
		return bounds.contains(content.localToParent(0, 0));
	}

	/**
	 * Toggle between displaying own content or children.
	 * @param bounds     The bounds of the viewport.
	 * @param visible    True if the content needs to be visible, false otherwise.
	 */
	public void toggle(Bounds bounds, boolean visible) {
		if (visible && !content.isVisible()) {
			childContent.getChildren().clear();
			content.setVisible(true);
			loadContent(bounds);
		}
		if (!visible) {
			content.setVisible(false);
			loadChildren(bounds);
		}
	}

	/**
	 * Update method that should be called after scaling.
	 * This method checks how zoomed in we are by transforming bounds to root coordinates,
	 * and then dynamically adds and deletes items in the JavaFX scene graph.
	 * @param bounds	The bounds of the viewport to update.
	 * @param curZoom   The current zoom level.
	 */
	public void update(Bounds bounds, double curZoom) {
		if (curZoom < Thresholds.GRAPH.get()) {
			toggle(bounds, true);
		} else {
			toggle(bounds, false);
		}
	}
}
