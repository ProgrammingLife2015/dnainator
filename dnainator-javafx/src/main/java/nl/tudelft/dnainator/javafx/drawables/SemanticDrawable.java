package nl.tudelft.dnainator.javafx.drawables;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import nl.tudelft.dnainator.annotation.Range;

/**
 * An abstract drawable that enables a semantic display of data.
 */
public abstract class SemanticDrawable extends Group {
	protected static final double RANK_WIDTH = 100;
	protected IntegerProperty minRank = new SimpleIntegerProperty(0, "minRank");
	protected IntegerProperty maxRank = new SimpleIntegerProperty(0, "maxRank");
	protected Group content;

	/**
	 * Creates a new {@link SemanticDrawable}.
	 * @param content The content to display.
	 */
	public SemanticDrawable(Group content) {
		this.content = content;
		getChildren().addAll(content);
	}

	/**
	 * Load the {@link Drawable} content of the view. The overriding class must make sure
	 * that this method gets called either in the constructor or right after!
	 * @param range The minimum and maximum range that are in view at the moment.
	 * @param zoom The current (geometric) zoom level.
	 */
	protected abstract void loadContent(Range range, double zoom);

	/**
	 * Update method that should be called after scaling.
	 * This method checks how zoomed in we are by transforming bounds to root coordinates,
	 * and then dynamically adds and deletes items in the JavaFX scene graph.
	 * @param bounds	The bounds of the new viewport.
	 * @param zoom      The current (geometric) zoom level.
	 */
	public void update(Bounds bounds, double zoom) {
		minRank.set((int) (Math.max(bounds.getMinX() / RANK_WIDTH, 0)));
		maxRank.set((int) (RANK_WIDTH + bounds.getMaxX() / RANK_WIDTH));
		loadContent(new Range(minRank.get(), maxRank.get()), zoom);
	}

	/**
	 * @return The minimum rank property.
	 */
	public IntegerProperty minRankProperty() {
		return minRank;
	}

	/**
	 * @return The maximum rank property.
	 */
	public IntegerProperty maxRankProperty() {
		return maxRank;
	}

	/**
	 * Get the rank width.
	 * @return the rank width.
	 */
	public double getRankWidth() {
		return RANK_WIDTH;
	}
}
