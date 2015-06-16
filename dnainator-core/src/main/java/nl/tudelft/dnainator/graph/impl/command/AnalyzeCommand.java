package nl.tudelft.dnainator.graph.impl.command;

import java.util.HashSet;
import java.util.Set;

import nl.tudelft.dnainator.graph.impl.RelTypes;
import nl.tudelft.dnainator.graph.interestingness.Scores;

import org.neo4j.collection.primitive.Primitive;
import org.neo4j.collection.primitive.PrimitiveLongSet;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.InitialBranchState.State;
import org.neo4j.graphdb.traversal.Uniqueness;

import static nl.tudelft.dnainator.graph.impl.PropertyTypes.RANK;
import static org.neo4j.helpers.collection.IteratorUtil.loop;

/**
 * The {@link AnalyzeCommand} creates a topological ordering and
 * ranks the nodes in the Neo4j database accordingly.
 */
public class AnalyzeCommand implements Command {
	private static final int INIT_CAP = 4096;
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
	public Iterable<Node> topologicalOrder(GraphDatabaseService service) {
		return topologicalOrder(service, Primitive.longSet());
	}

	private Iterable<Node> topologicalOrder(GraphDatabaseService service,
						PrimitiveLongSet processed) {
		return service.traversalDescription()
				.depthFirst()
				.expand(new TopologicalPathExpander()
				, new State<>(processed, null))
				// We manage uniqueness for ourselves.
				.uniqueness(Uniqueness.NONE)
				.traverse(loop(roots))
				.nodes();
	}

	@Override
	public void execute(GraphDatabaseService service) {
		try (
			Transaction tx = service.beginTx();
			// Our set is located "off heap", i.e. not managed by the garbage collector.
			// It is automatically closed after the try block, which frees the allocated memory.
			PrimitiveLongSet processed = Primitive.offHeapLongSet(INIT_CAP)
		) {
			for (Node n : topologicalOrder(service, processed)) {
				rankDest(n);
				scoreIndependentMutation(n);
			}
			tx.success();
		}
	}

	private void rankDest(Node n) {
		int rankSource = (int) n.getProperty(RANK.name());
		for (Relationship r : n.getRelationships(RelTypes.NEXT, Direction.OUTGOING)) {
			Node dest = r.getEndNode();
			if ((int) dest.getProperty(RANK.name()) < rankSource + 1) {
				dest.setProperty(RANK.name(), rankSource + 1);
			}
		}
	}

	private void scoreIndependentMutation(Node n) {
		Set<Node> ancestors = new HashSet<>();
		for (Relationship r : n.getRelationships(RelTypes.SOURCE)) {
			Node ancestor = r.getEndNode()
					.getSingleRelationship(RelTypes.ANCESTOR_OF, Direction.INCOMING)
					.getStartNode();
			ancestors.add(ancestor);
		}
		// TODO: check whether ancestors exist in separate branches of the phylogeny.
		n.setProperty(Scores.INDEP_MUT.getName(), ancestors.size());
	}
}
