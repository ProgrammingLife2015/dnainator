package nl.tudelft.dnainator.graph.impl.command;

import nl.tudelft.dnainator.graph.impl.RelTypes;
import nl.tudelft.dnainator.graph.interestingness.Scores;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.InitialBranchState;
import org.neo4j.graphdb.traversal.Uniqueness;

import static nl.tudelft.dnainator.graph.impl.properties.SequenceProperties.BASE_DIST;
import static nl.tudelft.dnainator.graph.impl.properties.SequenceProperties.RANK;

import static org.neo4j.helpers.collection.IteratorUtil.loop;

/**
 * The {@link AnalyzeCommand} creates a topological ordering and
 * ranks the nodes in the Neo4j database accordingly.
 */
public class AnalyzeCommand implements Command {
	private ResourceIterator<Node> roots;

	/**
	 * Create a new {@link AnalyzeCommand} that will
	 * start ranking from the specified roots.
	 * @param roots	the roots
	 */
	public AnalyzeCommand(ResourceIterator<Node> roots) {
		this.roots = roots;
	}

	/**
	 * Return a topological ordering on the specified database service.
	 * @param service	the database service
	 * @return		a topological ordering, starting from the roots
	 */
	@SuppressWarnings("unchecked")
	public Iterable<Node> topologicalOrder(GraphDatabaseService service) {
		return service.traversalDescription()
				// Depth first order, for creating bubbles.
				.depthFirst()
				.expand(new TopologicalPathExpander(), InitialBranchState.NO_STATE)
				// We manage uniqueness for ourselves.
				.uniqueness(Uniqueness.NONE)
				.traverse(loop(roots))
				.nodes();
	}

	@Override
	public void execute(GraphDatabaseService service) {
		try (
			Transaction tx = service.beginTx();
		) {
			for (Node n : topologicalOrder(service)) {
				rankDest(n);
			}
			tx.success();
		}
	}

	private void rankDest(Node n) {
		int baseSource = (int) n.getProperty(BASE_DIST.name())
				+ (int) n.getProperty(Scores.SEQ_LENGTH.name());
		int rankSource = (int) n.getProperty(RANK.name()) + 1;

		for (Relationship r : n.getRelationships(RelTypes.NEXT, Direction.OUTGOING)) {
			Node dest = r.getEndNode();

			if ((int) dest.getProperty(BASE_DIST.name()) < baseSource) {
				dest.setProperty(BASE_DIST.name(), baseSource);
			}
			if ((int) dest.getProperty(RANK.name()) < rankSource) {
				dest.setProperty(RANK.name(), rankSource);
			}
		}
	}
}
