package nl.tudelft.dnainator.core.graph;

import nl.tudelft.dnainator.core.Sequence;

public interface GraphFactory {
	void addEdge(int l, int r);
	void addNode(Sequence s);
}
