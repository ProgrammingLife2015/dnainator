package nl.tudelft.dnainator.graph;

import org.graphstream.algorithm.generator.BaseGenerator;

/**
 * This class generates a graph that represents a binary tree.
 * @author gerlof
 */
public class TreeGenerator extends BaseGenerator {
	private int nodeCount;

	@Override
	public void begin() {
		nodeCount = 0;
		addNode(Integer.toString(nodeCount++), 0, 0);
	}

	@Override
	public boolean nextEvents() {
		String parent = Integer.toString((nodeCount - 1) / 2);
		String node = Integer.toString(nodeCount);

		addNode(node);
		addEdge(node + "_" + parent, node, parent);

		nodeCount++;
		return true;
	}

}
