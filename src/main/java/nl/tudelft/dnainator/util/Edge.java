package nl.tudelft.dnainator.util;

/**
 * Class representing an edge, for containing
 * identifiers of {@link Sequence}s or other kinds of
 * nodes.
 * @param <T> The type of the identifiers.
 */
public final class Edge<T> {
	public final T source;
	public final T dest;

	/**
	 * Constructs a new {@link Edge}.
	 * @param source The source of the edge.
	 * @param dest The destination of the edge.
	 */
	public Edge(T source, T dest) {
		this.source = source;
		this.dest = dest;
	}
}
