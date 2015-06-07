package nl.tudelft.dnainator.graph.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tudelft.dnainator.core.SequenceNode;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * A {@link SequenceNode} which delegates to a Neo4j Node containing
 * the information.
 */
public class Neo4jSequenceNode implements SequenceNode {
	private GraphDatabaseService service;
	private Node node;

	private String id;
	private int start;
	private int end;
	private String sequence;
	private int rank;
	private Set<String> sources;
	private List<String> outgoing;

	private boolean loaded;

	/**
	 * Construct a new {@link Neo4jSequenceNode} which wraps the given
	 * Neo4j {@link Node}.
	 * @param service The Neo4j service, for accessing the database.
	 * @param node The Neo4j node.
	 */
	public Neo4jSequenceNode(GraphDatabaseService service, Node node) {
		loaded = false;

		this.service = service;
		this.node = node;
		this.outgoing = new ArrayList<>();
		this.sources = new HashSet<>();

		try (Transaction tx = service.beginTx()) {
			this.id = (String) node.getProperty(PropertyTypes.ID.name());

			node.getRelationships(RelTypes.NEXT, Direction.OUTGOING).forEach(e -> {
				outgoing.add((String) e.getEndNode().getProperty(PropertyTypes.ID.name()));
			});
			node.getRelationships(RelTypes.SOURCE, Direction.OUTGOING).forEach(e -> {
				sources.add((String) e.getEndNode().getProperty(PropertyTypes.SOURCE.name()));
			});

			tx.success();
		}
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Set<String> getSources() {
		return sources;
	}

	@Override
	public int getStartRef() {
		load();
		return start;
	}

	@Override
	public int getEndRef() {
		load();
		return end;
	}

	@Override
	public String getSequence() {
		load();
		return sequence;
	}

	@Override
	public int getRank() {
		load();
		return rank;
	}

	@Override
	public List<String> getOutgoing() {
		return outgoing;
	}

	private void load() {
		if (loaded) {
			return;
		}

		System.out.println("--- loading sequence node " + getId() + " ---");

		try (Transaction tx = service.beginTx()) {
			start		= (int)    node.getProperty(PropertyTypes.STARTREF.name());
			end		= (int)    node.getProperty(PropertyTypes.ENDREF.name());
			sequence	= (String) node.getProperty(PropertyTypes.SEQUENCE.name());
			rank		= (int)    node.getProperty(PropertyTypes.RANK.name());

			tx.success();
		}

		loaded = true;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SequenceNode)) {
			return false;
		}

		SequenceNode other = (SequenceNode) obj;
		return getId().equals(other.getId());
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return "SequenceNode<" + getId() + "," + getSequence().length() + ">";
	}
}
