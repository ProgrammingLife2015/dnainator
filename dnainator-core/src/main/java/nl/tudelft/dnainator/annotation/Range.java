package nl.tudelft.dnainator.annotation;

/**
 * A comparable range class, which equals another range
 * when they overlap. Immutable datatype.
 */
public class Range implements Comparable<Range> {
	private final int x;
	private final int y;

	/**
	 * Constructs a new range with the given coordinates.
	 * @param x The x coordinate of this range. (inclusive)
	 * @param y The y coordinate of this range. (inclusive)
	 */
	public Range(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the start coordinate of this range.
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the end coordinate of this range.
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return a new range, with an inclusive start
	 * coordinate and an exclusive end coordinate.
	 */
	public Range getExclusiveEnd() {
		return new Range(x, y - 1);
	}

	@Override
	public int compareTo(Range o) {
		if (this.x > o.y) {
			// 1 if this range comes after the other.
			return 1;
		} else if (this.y < o.x) {
			// -1 if this range comes before the other.
			return -1;
		}
		// 0 if both ranges overlap.
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Range) {
			return compareTo((Range) o) == 0;
		}
		return false;
	}

	@Override
	public int hashCode() {
		// Returns zero, because the equals method is true when two ranges overlap.
		// It's not possible therefore to create a good hashCode implementation which
		// is consistent with the contract.
		return 0;
	}

	@Override
	public String toString() {
		return "from: " + x + ", to: " + y;
	}
}
