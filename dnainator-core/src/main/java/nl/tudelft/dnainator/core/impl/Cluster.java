package nl.tudelft.dnainator.core.impl;

import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.core.SequenceNode;

import java.util.List;

/**
 * A cluster is a node that contains one or more clustered {@link SequenceNode}s.
 */
public class Cluster {
	private List<EnrichedSequenceNode> nodes;
	private int rankStart;

	/**
	 * Create a new {@link Cluster} using a list of {@link SequenceNode}s.
	 * The rankStart is the rank of the first {@link SequenceNode} in the list.
	 * This will be used for positioning this {@link Cluster}.
	 * @param rankStart	the start rank of this {@link Cluster}
	 * @param nodes		the list of {@link EnrichedSequenceNode}s in this
	 * cluster.
	 */
	public Cluster(int rankStart, List<EnrichedSequenceNode> nodes) {
		this.rankStart = rankStart;
		this.nodes = nodes;
	}

	/**
	 * Return this {@link Cluster}s list of {@link SequenceNode}s.
	 * @return	the list of {@link SequenceNode}s
	 */
	public List<EnrichedSequenceNode> getNodes() {
		return nodes;
	}

	/**
	 * Return this {@link Cluster}s start rank.
	 * @return	the start rank of this {@link Cluster}
	 */
	public int getStartRank() {
		return rankStart;
	}

	@Override
	public String toString() {
		return "<Cluster " + nodes.toString() + ">";
	}
}
