package nl.tudelft.dnainator.ui.drawables;

/**
 * An interface that each drawable should implement.
 */
public interface Drawable {
	/**
	 * Applies the CSS class <code>style</code> to all
	 * children of this drawable.
	 * @param style The CSS class to apply.
	 */
	void addStyle(String style);

	/**
	 * Removes the CSS class <code>style</code> from
	 * all children of this drawable.
	 * @param style The CSS class to remove.
	 */
	void removeStyle(String style);
}
