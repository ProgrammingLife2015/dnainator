package nl.tudelft.dnainator.javafx.drawables;

/**
 * This enum holds zooming thresholds for the various zooming levels.
 * The amount corresponds to the level of scaling.
 */
public enum Thresholds {
	GRAPH(1.7),
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
