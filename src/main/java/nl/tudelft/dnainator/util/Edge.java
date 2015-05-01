package nl.tudelft.dnainator.util;

import nl.tudelft.dnainator.core.SequenceNode;

/**
 * Class representing an edge, for containing
 * identifiers of {@link SequenceNode}s or other kinds of
 * nodes.
 * @param <T> The type of the identifiers.
 */
public final class Edge<T> {
	private final T source;
	private final T dest;

	/**
	 * Constructs a new {@link Edge}.
	 * @param source The source of the edge.
	 * @param dest The destination of the edge.
	 */
	public Edge(T source, T dest) {
		this.source = source;
		this.dest = dest;
	}

	/**
	 * Get's the source node identifier of this edge.
	 * @return the source node identifier.
	 */
	public T getSource() {
		return source;
	}

	/**
	 * Get's the destination node identifier of this edge.
	 * @return the destination node identifier.
	 */
	public T getDest() {
		return dest;
	}
}
