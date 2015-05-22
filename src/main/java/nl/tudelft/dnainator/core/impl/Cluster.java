package nl.tudelft.dnainator.core.impl;

import java.util.List;

import nl.tudelft.dnainator.core.SequenceNode;

public class Cluster {
	private List<SequenceNode> nodes;
	private int rankStart;

	public Cluster(int rankStart, List<SequenceNode> list) {
		this.rankStart = rankStart;
		this.nodes = list;
	}

	public List<SequenceNode> getNodes() {
		return nodes;
	}

	public int getStartRank() {
		return rankStart;
	}

	@Override
	public String toString() {
		return "<Cluster " + nodes.toString() + ">";
	}
}
