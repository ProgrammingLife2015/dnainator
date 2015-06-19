package nl.tudelft.dnainator.graph.impl.command;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.tudelft.dnainator.graph.impl.NodeLabels;
import nl.tudelft.dnainator.graph.impl.RelTypes;
import nl.tudelft.dnainator.graph.impl.properties.AnnotationProperties;
import nl.tudelft.dnainator.graph.impl.properties.SequenceProperties;
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

import static nl.tudelft.dnainator.graph.impl.properties.SequenceProperties.BASE_DIST;
import static nl.tudelft.dnainator.graph.impl.properties.SequenceProperties.RANK;
import static org.neo4j.helpers.collection.IteratorUtil.loop;

/**
 * The {@link AnalyzeCommand} creates a topological ordering and
 * ranks the nodes in the Neo4j database accordingly.
 */
public class AnalyzeCommand implements Command {
	private static final int INIT_CAP = 4096;
	private static final String LABEL = "n";
	private static final String GET_NODES_BASEDIST =
			"MATCH (n:" + NodeLabels.NODE.name() + ") "
			+ "WHERE {dist} >= n." + SequenceProperties.BASE_DIST.name()
			+ " AND {dist} < n." + SequenceProperties.BASE_DIST.name()
			+ " + n." + Scores.SEQ_LENGTH.name() + " RETURN n AS " + LABEL;
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
			scoreDRMutations(service);
			tx.success();
		}
	}

	/**
	 * Rank the destination nodes of the outgoing edges of the given node.
	 * @param n the source node of the destination nodes to be ranked.
	 */
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

	/**
	 * Scores the amount of drug resistance mutations.
	 * @param service	the graph service
	 */
	private void scoreDRMutations(GraphDatabaseService service) {
		service.findNodes(NodeLabels.DRMUTATION).forEachRemaining(drannotations ->
			drannotations.getRelationships(RelTypes.ANNOTATED).forEach(node -> {
				// From the startref of the annotation
				// subtract the startref of the annotated node
				// and add the base distance of the annotated node
				int basedist = (int) drannotations.getProperty(AnnotationProperties.STARTREF.name())
					- (int) node.getStartNode().getProperty(SequenceProperties.STARTREF.name())
					+ (int) node.getStartNode().getProperty(SequenceProperties.BASE_DIST.name());

				Map<String, Object> params = Collections.singletonMap("dist", basedist);
				ResourceIterator<Node> mutations = service.execute(GET_NODES_BASEDIST, params)
									.columnAs(LABEL);
				mutations.forEachRemaining(m -> {
					int score = (int) m.getProperty(Scores.DR_MUT.name(), 0);
					m.setProperty(Scores.DR_MUT.name(), score + 1);
				});
			})
		);
	}

	/**
	 * Scores the amount of independent mutations, using the phylogeny.
	 * @param n the node representing the mutation.
	 */
	protected void scoreIndependentMutation(Node n) {
		Set<Node> ancestors = new HashSet<>();
		for (Relationship r : n.getRelationships(RelTypes.SOURCE)) {
			r.getEndNode().getRelationships(RelTypes.ANCESTOR_OF, Direction.INCOMING).forEach(e ->
				ancestors.add(e.getStartNode())
			);
		}
		n.setProperty(Scores.INDEP_MUT.name(), ancestors.size());
	}
}
