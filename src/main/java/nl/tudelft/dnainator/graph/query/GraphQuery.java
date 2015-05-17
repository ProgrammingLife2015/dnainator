package nl.tudelft.dnainator.graph.query;

/**
 * Interface for classes which can compile a
 * {@link GraphQueryDescription} and {@link QueryElement}s.
 * This is implemented according to the visitor pattern. The compile
 * methods are the "visit" methods in the visitor pattern.
 */
public interface GraphQuery {

	/**
	 * Compiles the given {@link GraphQueryDescription}.
	 * @param qd The description for the query to be compiled.
	 */
	void compile(GraphQueryDescription qd);

	/**
	 * @param ids The filter on node IDs to be compiled.
	 */
	void compile(IDsFilter ids);

	/**
	 * @param sources The filter on sources to be compiled.
	 */
	void compile(SourcesFilter sources);

	/**
	 * @param p The predicate to be compiled.
	 */
	void compile(PredicateFilter p);

	/**
	 * @param start The start rank from where to start searching.
	 */
	void compile(RankStart start);

	/**
	 * @param end The end rank where to stop searching.
	 */
	void compile(RankEnd end);

}
