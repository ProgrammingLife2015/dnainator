package nl.tudelft.dnainator.graph.impl.command;

import java.util.HashMap;
import java.util.Map;

import nl.tudelft.dnainator.graph.impl.NodeLabels;
import nl.tudelft.dnainator.graph.impl.RelTypes;
import nl.tudelft.dnainator.graph.impl.properties.AnnotationProperties;
import nl.tudelft.dnainator.graph.impl.properties.SequenceProperties;
import nl.tudelft.dnainator.graph.impl.properties.SourceProperties;
import nl.tudelft.dnainator.graph.interestingness.Scores;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
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
	private static final String LABEL = "n";
	private static final String GET_NODES_BASEDIST =
			"MATCH (n:" + NodeLabels.NODE.name() + ")-[:" + RelTypes.SOURCE.name() + "]-s, "
			+ "    (t {" + SourceProperties.SOURCE.name() + ": \"TKK_REF\"})"
			+ "WHERE NOT (n-->t)"
			+ " AND {dist} >= n." + SequenceProperties.BASE_DIST.name()
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
		for (Node n : topologicalOrder(service)) {
			rankDest(n);
		}
		scoreDRMutations(service);
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
		Map<String, Object> params = new HashMap<>(1);
		service.findNodes(NodeLabels.DRMUTATION).forEachRemaining(drannotations ->
			drannotations.getRelationships(RelTypes.ANNOTATED).forEach(node -> {
				// From the startref of the annotation
				// subtract the startref of the annotated node
				// and add the base distance of the annotated node
				int basedist = (int) drannotations.getProperty(AnnotationProperties.STARTREF.name())
					- (int) node.getStartNode().getProperty(SequenceProperties.STARTREF.name())
					+ (int) node.getStartNode().getProperty(SequenceProperties.BASE_DIST.name());

				params.put("dist", basedist);
				ResourceIterator<Node> mutations = service.execute(GET_NODES_BASEDIST, params)
									.columnAs(LABEL);
				mutations.forEachRemaining(m -> {
					int score = (int) m.getProperty(Scores.DR_MUT.name(), 0);
					m.setProperty(Scores.DR_MUT.name(), score + 1);
				});
			})
		);
	}
}
