package nl.tudelft.dnainator.graph.impl;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.graph.interestingness.ScoreIdentifier;
import nl.tudelft.dnainator.graph.interestingness.Scores;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A {@link SequenceNode} which delegates to a Neo4j Node containing
 * the information.
 */
public class Neo4jSequenceNode implements EnrichedSequenceNode {
	private GraphDatabaseService service;
	private Node node;

	private String id;
	private int start;
	private int end;
	private String sequence;
	private int basedist;
	private int rank;
	private List<Annotation> annotations;
	private Set<String> sources;
	private List<String> outgoing;
	private Map<ScoreIdentifier, Integer> scores;
	private int interestingness;

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
		this.annotations = new ArrayList<>();
		this.outgoing = new ArrayList<>();
		this.sources = new HashSet<>();
		this.scores = new HashMap<>();

		try (Transaction tx = service.beginTx()) {
			this.id = (String) node.getProperty(PropertyTypes.ID.name());

			node.getRelationships(RelTypes.ANNOTATED, Direction.OUTGOING).forEach(e -> {
				annotations.add(new Neo4jAnnotation(service, e.getEndNode()));
			});
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
	public List<Annotation> getAnnotations() {
		return annotations;
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
	public int getBaseDistance() {
		load();
		return basedist;
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

	@Override
	public int getScore(ScoreIdentifier id) {
		load();
		return scores.get(id);
	}

	@Override
	public Map<ScoreIdentifier, Integer> getScores() {
		load();
		return scores;
	}

	@Override
	public int getInterestingnessScore() {
		load();
		return interestingness;
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
			basedist	= (int)    node.getProperty(PropertyTypes.BASE_DIST.name());
			rank		= (int)    node.getProperty(PropertyTypes.RANK.name());
			for (ScoreIdentifier id : Scores.values()) {
				scores.put(id, (Integer) node.getProperty(id.name(), 0));
			}
			interestingness = (int) node.getProperty(PropertyTypes.INTERESTINGNESS.name(), 0);
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
