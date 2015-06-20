package nl.tudelft.dnainator.javafx.drawables;

/**
 * This enum holds zooming thresholds for the various zooming levels.
 * The amount corresponds to the level of scaling.
 */
public enum Thresholds {
	GRAPH(2.0),
	CLUSTER(4.0);
	
	private final double threshold;
	private Thresholds(double threshold) {
		this.threshold = threshold;
	}
	
	/**
	 * Return the associated threshold.
	 * @return	a zooming threshold
	 */
	public double get() {
		return threshold;
	}
}
