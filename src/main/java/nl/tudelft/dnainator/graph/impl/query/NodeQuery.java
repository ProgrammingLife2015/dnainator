package nl.tudelft.dnainator.graph.impl.query;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.SequenceNodeImpl;
import nl.tudelft.dnainator.graph.impl.PropertyTypes;
import nl.tudelft.dnainator.graph.impl.RelTypes;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

/**
 * The {@link NodeQuery} returns one single {@link SequenceNode} using the specified neo4j node.
 */
public class NodeQuery implements Query<SequenceNode> {
	private Node node;

	/**
	 * Create a {@link NodeQuery} that will query for a Neo4j node.
	 * FIXME: Might change to String later?
	 * @param node	the specified Node
	 */
	public NodeQuery(Node node) {
		this.node = node;	
	}
	
	@Override
	public SequenceNode execute(GraphDatabaseService service) {
		String id	= (String) node.getProperty(PropertyTypes.ID.name());
		int startref	= (int)    node.getProperty(PropertyTypes.STARTREF.name());
		int endref	= (int)    node.getProperty(PropertyTypes.ENDREF.name());
		String sequence	= (String) node.getProperty(PropertyTypes.SEQUENCE.name());
		int rank	= (int)    node.getProperty(PropertyTypes.RANK.name());

		List<String> sources = new ArrayList<>();
		node.getRelationships(RelTypes.SOURCE).forEach(e -> {
			sources.add((String) e.getEndNode().getProperty(PropertyTypes.SOURCE.name()));
		});

		List<String> outgoing = new ArrayList<>();
		node.getRelationships(RelTypes.NEXT, Direction.OUTGOING).forEach(e -> {
			outgoing.add((String) e.getEndNode().getProperty(PropertyTypes.ID.name()));
		});

		return new SequenceNodeImpl(id, sources, startref, endref,
						sequence, rank, outgoing);
	}
}
