package nl.tudelft.dnainator.graph.impl.query;

import static org.neo4j.helpers.collection.IteratorUtil.loop;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.SequenceNodeImpl;
import nl.tudelft.dnainator.graph.impl.PropertyTypes;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

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
		String source	= (String) node.getProperty(PropertyTypes.SOURCE.name());
		int startref	= (int)    node.getProperty(PropertyTypes.STARTREF.name());
		int endref	= (int)    node.getProperty(PropertyTypes.ENDREF.name());
		String sequence	= (String) node.getProperty(PropertyTypes.SEQUENCE.name());
		int rank	= (int)    node.getProperty(PropertyTypes.RANK.name());

		List<String> outgoing = new ArrayList<>();
		for (Relationship e : loop(node.getRelationships(Direction.OUTGOING).iterator())) {
			outgoing.add((String) e.getEndNode().getProperty(PropertyTypes.ID.name()));
		}

		return new SequenceNodeImpl(id, source, startref, endref, sequence, rank, outgoing);
	}
}
