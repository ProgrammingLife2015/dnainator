package nl.tudelft.dnainator.ui.models;

/**
 * This enum holds zooming thresholds for the various zooming levels.
 */
public enum Thresholds {
	GRAPH(20000),
	CLUSTER(2000);
	
	private final int threshold;
	private Thresholds(int threshold) {
		this.threshold = threshold;
	}
	
	/**
	 * Return the associated threshold.
	 * @return	a zooming threshold
	 */
	public int get() {
		return threshold;
	}
}
