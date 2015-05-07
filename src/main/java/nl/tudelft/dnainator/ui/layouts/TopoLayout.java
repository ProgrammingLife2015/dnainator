package nl.tudelft.dnainator.ui.layouts;

import javafx.geometry.Point2D;

/**
 * A topologically sorted layout.
 */
public final class TopoLayout {
	private static final double WIDTH = 60;
	private static final double HEIGHT = 60;

	// Keep Checkstyle satisfied.
	private TopoLayout() {
	}

	/**
	 * Transforms a given node into x- and y-coordinates to display.
	 * @param rank The rank of the nodes.
	 * @param nodes The number of nodes in this rank.
	 * @param current The index of this node in the current rank.
	 * @return A {@link Point2D} containing the x- and y-coordinates for this node.
	 */
	public static Point2D transform(int rank, int nodes, int current) {
		return new Point2D(rank * WIDTH, (current % nodes - (float) nodes / 2) * HEIGHT);
	}
}
