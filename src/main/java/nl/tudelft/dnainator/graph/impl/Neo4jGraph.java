package nl.tudelft.dnainator.graph.impl;

import static org.neo4j.helpers.collection.IteratorUtil.loop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Edge;
import nl.tudelft.dnainator.core.impl.SequenceNodeImpl;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.query.GraphQueryDescription;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;

import org.neo4j.collection.primitive.Primitive;
import org.neo4j.collection.primitive.PrimitiveLongSet;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.InitialBranchState.State;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Uniqueness;

/**
 * Edge relationship types.
 */
enum RelTypes implements RelationshipType {
	NEXT
}

/**
 * This class realizes a graphfactory using Neo4j as it's backend.
 */
public final class Neo4jGraph implements Graph {
	private static final int INIT_CAP = 4096;
	private static final String GET_ROOT = "MATCH (s:Node) "
			+ "WHERE NOT (s)<-[:NEXT]-(:Node)"
			+ "RETURN s";

	private GraphDatabaseService service;
	private Label nodeLabel;
	private int maxRank;

	/**
	 * Constructs a Neo4j database on the specified path.
	 * @param path			specified path
	 */
	public Neo4jGraph(String path) {
		// Create our database and register a shutdown hook
		service = new GraphDatabaseFactory().newEmbeddedDatabase(path);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				service.shutdown();
			}
		});

		// Assign a label to our nodes
		nodeLabel = DynamicLabel.label("Node");
		try (Transaction tx = service.beginTx()) {
			// Generate a unique index on 'id'
			service.schema().constraintFor(nodeLabel)
			.assertPropertyIsUnique("id")
			.create();

			// Generate an index on 'dist'
			service.schema().indexFor(nodeLabel)
			.on("dist")
			.create();

			tx.success();
		}
	}

	@Override
	public void addEdge(Edge<String> edge) {
		try (Transaction tx = service.beginTx()) {
			Node source = service.findNode(nodeLabel, "id", edge.getSource());
			Node dest   = service.findNode(nodeLabel, "id", edge.getDest());
			source.createRelationshipTo(dest, RelTypes.NEXT);

			tx.success();
		}
	}

	@Override
	public void addNode(SequenceNode s) {
		try (Transaction tx = service.beginTx()) {
			Node node = service.createNode(nodeLabel);
			node.setProperty("id", s.getId());
			node.setProperty("start", s.getStartRef());
			node.setProperty("end", s.getEndRef());
			node.setProperty("sequence", s.getSequence());
			node.setProperty("source", s.getSource());
			node.setProperty("dist", 0);

			tx.success();
		}
	}

	@Override
	public void constructGraph(NodeParser np, EdgeParser ep)
			throws IOException, ParseException {
		try (Transaction tx = service.beginTx()) {
			while (np.hasNext()) {
				addNode(np.next());
			}
			while (ep.hasNext()) {
				addEdge(ep.next());
			}
			rankGraph();

			tx.success();
		}
	}

	@Override
	public List<SequenceNode> getCluster(String startId, int threshold) {
		TraversalDescription cluster = service.traversalDescription()
						.depthFirst()
						.relationships(RelTypes.NEXT, Direction.OUTGOING)
						.evaluator(new ClusterEvaluator(threshold));

		List<SequenceNode> list = new ArrayList<>();
		try (Transaction tx = service.beginTx()) {
			Node root = service.findNode(nodeLabel, "id", startId);
			for (Path p : cluster.traverse(root)) {
				list.add(createSequenceNode(p.endNode()));
			}
		}
		return list;
	}

	@Override
	public SequenceNode getRootNode() {
		SequenceNode node;

		try (Transaction tx = service.beginTx()) {
			Node root = getRoot();
			node = createSequenceNode(root);

			tx.success();
		}

		return node;
	}

	@Override
	public SequenceNode getNode(String s) {
		SequenceNode node;

		try (Transaction tx = service.beginTx()) {
			node = createSequenceNode(service.findNode(nodeLabel, "id", s));

			tx.success();
		}

		return node;
	}

	@Override
	public List<SequenceNode> getRank(int rank) {
		List<SequenceNode> nodes = new LinkedList<>();

		try (Transaction tx = service.beginTx()) {
			ResourceIterator<Node> res = service.findNodes(nodeLabel, "dist", rank);

			for (Node n : loop(res)) {
				nodes.add(createSequenceNode(n));
			}

			tx.success();
		}

		return nodes;
	}

	@Override
	public List<List<SequenceNode>> getRanks() {
		List<List<SequenceNode>> nodes = new LinkedList<>();
		try (Transaction tx = service.beginTx()) {
			for (int i = 0; i < maxRank; i++) {
				nodes.add(getRank(i));
			}

			tx.success();
		}

		return nodes;
	}

	@Override
	public List<SequenceNode> queryNodes(GraphQueryDescription qd) {
		return Neo4jQuery.of(qd).execute(service);
	}



	/**
	 * Create a {@link SequenceNode} from the information in the given
	 * Neo4j {@link Node}.
	 * @param node from the database.
	 * @return a {@link SequenceNode} with the information of the given {@link Node}.
	 */
	protected static SequenceNode createSequenceNode(Node node) {
		String id       = (String) node.getProperty("id");
		String source   = (String) node.getProperty("source");
		int startref    = (int) node.getProperty("start");
		int endref      = (int) node.getProperty("end");
		String sequence = (String) node.getProperty("sequence");
		int rank		= (int) node.getProperty("dist");

		return new SequenceNodeImpl(id, source, startref, endref, sequence, rank);
	}

	private Node getRoot() {
		return rootIterator().next();
	}

	/**
	 * Retrieve the database service object of this instance.
	 * @return	a neo4j database service
	 */
	protected GraphDatabaseService getService() {
		return service;
	}

	/**
	 * Get a topological ordering of the graph.
	 * @return an {@link Iterable}, containing the nodes in
	 * topological order.
	 */
	protected Iterable<Node> topologicalOrder() {
		return topologicalOrder(Primitive.longSet(INIT_CAP));
	}

	private Iterable<Node> topologicalOrder(PrimitiveLongSet processed) {
		ResourceIterator<Node> roots = rootIterator();
		return service.traversalDescription()
					.depthFirst()
					.expand(new TopologicalExpander()
					, new State<>(processed, null))
					// We manage uniqueness for ourselves.
					.uniqueness(Uniqueness.NONE)
					.traverse(loop(roots))
					.nodes();
	}

	private void rankGraph() {
		try (
			Transaction tx = service.beginTx();
			// Our set is located "off heap", i.e. not managed by the garbage collector.
			// It is automatically closed after the try block, which frees the allocated memory.
			PrimitiveLongSet processed = Primitive.offHeapLongSet(INIT_CAP)
		) {
			for (Node n : topologicalOrder(processed)) {
				int rankSource = (int) n.getProperty("dist");
				for (Relationship r : n.getRelationships(RelTypes.NEXT, Direction.OUTGOING)) {
					Node dest = r.getEndNode();
					if ((int) dest.getProperty("dist") < rankSource + 1) {
						dest.setProperty("dist", rankSource + 1);
						maxRank = rankSource + 1;
					}
				}
			}
			tx.success();
		}
	}

	/**
	 * Retrieves a resource iterator over all roots.
	 * The roots are all the nodes with no incoming edges.
	 * @return	a resource iterator
	 */
	private ResourceIterator<Node> rootIterator() {
		ResourceIterator<Node> roots;
		Result res = service.execute(GET_ROOT);
		roots = res.columnAs("s");
		return roots;
	}
}
