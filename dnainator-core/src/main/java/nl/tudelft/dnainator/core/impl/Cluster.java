package nl.tudelft.dnainator.core.impl;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.core.SequenceNode;

import java.util.List;

/**
 * A cluster is a node that contains one or more clustered {@link SequenceNode}s.
 */
public class Cluster {
	private List<SequenceNode> nodes;
	private int rankStart;
	private List<Annotation> annotations;

	/**
	 * Create a new {@link Cluster} using a list of {@link SequenceNode}s.
	 * The rankStart is the rank of the first {@link SequenceNode} in the list.
	 * This will be used for positioning this {@link Cluster}.
	 * @param rankStart	the start rank of this {@link Cluster}
	 * @param annotations	the annotations associated with this cluster
	 * @param list		the list of {@link SequenceNode}s
	 */
	public Cluster(int rankStart, List<SequenceNode> list, List<Annotation> annotations) {
		this.rankStart = rankStart;
		this.annotations = annotations;
		this.nodes = list;
	}

	/**
	 * Return the {@link Annotation}s associated with the nodes in this {@link Cluster}.
	 * @return	the associated {@link Annotation}s
	 */
	public List<Annotation> getAnnotations() {
		return annotations;
	}

	/**
	 * Return this {@link Cluster}s list of {@link SequenceNode}s.
	 * @return	the list of {@link SequenceNode}s
	 */
	public List<SequenceNode> getNodes() {
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
