package nl.tudelft.dnainator.graph.query;

public interface QueryElement {
	void accept(GraphQuery q);
}