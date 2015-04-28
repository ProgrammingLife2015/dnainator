package nl.tudelft.dnainator.core.graph;

import nl.tudelft.dnainator.core.Sequence;

public interface GraphFactory<T> {
	void addEdge(int l, int r);
	void addNode(Sequence s);
	T getGraph();
}
