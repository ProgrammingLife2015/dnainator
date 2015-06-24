package nl.tudelft.dnainator.javafx.drawables.strains;

/**
 * This enum holds zooming thresholds for the various zooming levels, thus defining the levels of
 * semantic zooming. The amounts correspond to sequence lengths and are the threshold passed to
 * {@link nl.tudelft.dnainator.graph.impl.Neo4jGraph#getAllClusters(java.util.List, int, int)}
 */
public enum Thresholds {
	INDIVIDUAL(700),
	SMALL_CLUSTER(600),
	MEDIUM_CLUSTER(500),
	LARGE_CLUSTER(50),
	GRAPH(0);

	private final int threshold;

	/**
	 * Creates the threshold enum item with the specific threshold.
	 * @param threshold the threshold to be used for the item
	 */
	Thresholds(int threshold) {
		this.threshold = threshold;
	}

	/**
	 * Returns the threshold to which the given scale belongs.
	 * @param scale the scale whose threshold to retrieve.
	 * @return  the threshold to which the given scale belongs.
	 */
	public static Thresholds retrieve(double scale) {
		for (Thresholds t : values()) {
			if (scale >= t.get()) {
				return t;
			}
		}
		// Clamp negative scales.
		return GRAPH;
	}

	/**
	 * Return the associated threshold.
	 * @return	a zooming threshold
	 */
	public int get() {
		return threshold;
	}
}

