package nl.tudelft.dnainator.graph.query;

/**
 * An element of a {@link GraphQueryDescription}.
 */
public interface QueryElement {
	/**
	 * Accept the given graph query as a visitor,
	 * to compile this part of the query description.
	 * @param q The query to compile.
	 */
	void accept(GraphQuery q);
}