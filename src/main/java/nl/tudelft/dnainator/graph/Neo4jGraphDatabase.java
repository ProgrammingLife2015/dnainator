package nl.tudelft.dnainator.graph;

import static org.neo4j.helpers.collection.IteratorUtil.loop;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import nl.tudelft.dnainator.core.DefaultSequenceNode;
import nl.tudelft.dnainator.core.Edge;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;

import org.neo4j.graphalgo.impl.shortestpath.DijkstraPriorityQueue;
import org.neo4j.graphalgo.impl.shortestpath.DijkstraPriorityQueueFibonacciImpl;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;
import org.neo4j.tooling.GlobalGraphOperations;

/**
 * This class realizes a graphfactory using Neo4j as it's backend.
 */
public final class Neo4jGraphDatabase implements Graph {
	private static Neo4jGraphDatabase instance = null;
	private static final String DB_PATH = "target/neo4j-hello-db";
	private static final String GET_ROOT = "MATCH (s:Node) "
			+ "WHERE NOT (s)<-[:NEXT]-(:Node)"
			+ "RETURN s";

	private GraphDatabaseService graphDb;
	private Label nodeLabel;

	/**
	 * Edge relationship types.
	 */
	private static enum RelTypes implements RelationshipType {
		NEXT
	}

	/**
	 * Constructs a Neo4j database on the default path.
	 * @throws IOException	when the database could not be created
	 */
	private Neo4jGraphDatabase() throws IOException {
		this(new File(DB_PATH));
	}

	/**
	 * Constructs a Neo4j database on the specified path.
	 * @param file			specified path
	 * @throws IOException	when the database could not be created
	 */
	private Neo4jGraphDatabase(File file) throws IOException {
		// FIXME: remove this sometime.
		FileUtils.deleteRecursively(new File(DB_PATH));

		// Create our database and register a shutdown hook
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});

		// Assign a label to our nodes
		nodeLabel = DynamicLabel.label("Node");
		try (Transaction tx = graphDb.beginTx()) {
			// Generate a unique index on 'id'
			graphDb.schema().constraintFor(nodeLabel)
			.assertPropertyIsUnique("id")
			.create();

			// Generate an index on 'dist'
			graphDb.schema().indexFor(nodeLabel)
			.on("dist")
			.create();

			tx.success();
		}
	}

	/**
	 * Returns the singleton instance of this database if it exists.
	 * If the singleton does not exist yet, it will be created first.
	 * @return		the singleton database instance
	 * @throws IOException	when the database could not be initialized
	 */
	public static Neo4jGraphDatabase getInstance() throws IOException {
		if (instance == null) {
			instance = new Neo4jGraphDatabase();
		}
		return instance;
	}

	@Override
	public void addEdge(Edge<String> edge) {
		try (Transaction tx = graphDb.beginTx()) {
			Node source = graphDb.findNode(nodeLabel, "id", edge.getSource());
			Node dest   = graphDb.findNode(nodeLabel, "id", edge.getDest());
			source.createRelationshipTo(dest, RelTypes.NEXT);

			tx.success();
		}
	}

	@Override
	public void addNode(SequenceNode s) {
		try (Transaction tx = graphDb.beginTx()) {
			Node node = graphDb.createNode(nodeLabel);
			node.setProperty("id", s.getId());
			node.setProperty("start", s.getStartRef());
			node.setProperty("end", s.getEndRef());
			node.setProperty("sequence", s.getSequence());
			node.setProperty("source", s.getSource());

			tx.success();
		}
	}

	@Override
	public void constructGraph(NodeParser np, EdgeParser ep)
			throws IOException, ParseException {
		try (Transaction tx = graphDb.beginTx()) {
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

	/**
	 * Retrieve a single root (ie. node with no incoming edges) of the graph database.
	 * @return	a Node with no incoming edges
	 */
	private Node getRoot() {
		Node root = null;

		try (Transaction tx = graphDb.beginTx()) {
			Result res = graphDb.execute(GET_ROOT);
			Iterator<Node> col = res.columnAs("s");
			root = col.next();

			tx.success();
		}

		return root;
	}

	/**
	 * Assign dijkstra's shortest path as rank to all nodes in the graph.
	 */
	private void rankGraph() {
		try (Transaction tx = graphDb.beginTx()) {
			Node root = getRoot();

			DijkstraPriorityQueue<Integer> prio = new DijkstraPriorityQueueFibonacciImpl<>(
					new Comparator<Integer>() {
						public int compare(Integer o1, Integer o2) {
							return Integer.compare(o1, o2);
						} });
			for (Node n : GlobalGraphOperations.at(graphDb).getAllNodes()) {
				prio.insertValue(n, Integer.MAX_VALUE);
				n.setProperty("dist", Integer.MAX_VALUE);
			}

			prio.decreaseValue(root, 0);
			root.setProperty("dist", 0);

			while (!prio.isEmpty()) {
				Node u = prio.extractMin();
				relaxedges(u, prio);
			}

			tx.success();
		}
	}

	/**
	 * Part of dijkstra's algorithm. Relaxes all edges of a node.
	 * @param u		the node
	 * @param prio	the dijkstra min queue
	 */
	private void relaxedges(Node u, DijkstraPriorityQueue<Integer> prio) {
		for (Relationship p : u.getRelationships(Direction.OUTGOING)) {
			Node source = p.getStartNode();
			Node sink   = p.getEndNode();

			int dist  = (int) source.getProperty("dist") + 1;
			int ndist = (int) sink.getProperty("dist");
			if (dist < ndist) {
				p.getEndNode().setProperty("dist", dist);
				prio.decreaseValue(sink, dist);
			}
		}
	}

	@Override
	public SequenceNode getRootNode() {
		SequenceNode node = null;

		try (Transaction tx = graphDb.beginTx()) {
			Node root = getRoot();
			node = createSequenceNode(root);

			tx.success();
		}

		return node;
	}

	@Override
	public SequenceNode getNode(String s) {
		SequenceNode node = null;

		try (Transaction tx = graphDb.beginTx()) {
			node = createSequenceNode(graphDb.findNode(nodeLabel, "id", s));

			tx.success();
		}

		return node;
	}

	@Override
	public List<SequenceNode> getRank(int rank) {
		List<SequenceNode> nodes = new LinkedList<SequenceNode>();

		try (Transaction tx = graphDb.beginTx()) {
			ResourceIterator<Node> res = graphDb.findNodes(nodeLabel, "dist", rank);

			for (Node n : loop(res)) {
				nodes.add(createSequenceNode(n));
			}

			tx.success();
		}

		return nodes;
	}

	private SequenceNode createSequenceNode(Node node) {
		String id       = (String) node.getProperty("id");
		String source   = (String) node.getProperty("source");
		int startref    = (int) node.getProperty("start");
		int endref      = (int) node.getProperty("end");
		String sequence = (String) node.getProperty("sequence");

		return new DefaultSequenceNode(id, source, startref, endref, sequence);
	}
}
