package nl.tudelft.dnainator.core.graph;

import nl.tudelft.dnainator.core.SequenceNode;

public interface GraphBuilder {
	void addEdge(int l, int r);
	void addNode(SequenceNode s);
}
