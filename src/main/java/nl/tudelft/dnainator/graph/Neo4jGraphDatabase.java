package nl.tudelft.dnainator.graph;

import static org.neo4j.helpers.collection.IteratorUtil.loop;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import nl.tudelft.dnainator.core.DefaultSequenceNode;
import nl.tudelft.dnainator.core.Edge;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.io.fs.FileUtils;

/**
 * This class realizes a graphfactory using Neo4j as it's backend.
 */
public final class Neo4jGraphDatabase implements Graph {
	private static final String DB_PATH = "target/neo4j-hello-db";
	private static GraphDatabaseService service = null;
	private static final String GET_ROOT = "MATCH (s:Node) "
			+ "WHERE NOT (s)<-[:NEXT]-(:Node)"
			+ "RETURN s";

	private static GraphDatabaseService createInstance(String path) throws IOException {
		if (service != null) {
			service.shutdown();
		}
		FileUtils.deleteRecursively(new File(path));
		service = new GraphDatabaseFactory().newEmbeddedDatabase(path);

		return service;
	}

	/**
	 * Protected for junit, returns the unique instance of the db service.
	 * @return	the unique instance of the db service
	 */
	protected static GraphDatabaseService getInstance() {
		if (service == null) {
			throw new NumberFormatException();
		}
		return service;
	}

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
	public Neo4jGraphDatabase() throws IOException {
		this(DB_PATH);
	}

	/**
	 * Constructs a Neo4j database on the specified path.
	 * WARNING: EVERYTIME YOU INSTANTIATE THIS CLASS, THE GRAPH INSTANCE IS REPLACED!
	 * @param path			specified path
	 * @throws IOException	when the database could not be created
	 */
	public Neo4jGraphDatabase(String path) throws IOException {
		// Create our database and register a shutdown hook
		createInstance(path);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				getInstance().shutdown();
			}
		});

		// Assign a label to our nodes
		nodeLabel = DynamicLabel.label("Node");
		try (Transaction tx = getInstance().beginTx()) {
			// Generate a unique index on 'id'
			getInstance().schema().constraintFor(nodeLabel)
			.assertPropertyIsUnique("id")
			.create();

			// Generate an index on 'dist'
			getInstance().schema().indexFor(nodeLabel)
			.on("dist")
			.create();

			tx.success();
		}
	}

	@Override
	public void addEdge(Edge<String> edge) {
		try (Transaction tx = getInstance().beginTx()) {
			Node source = getInstance().findNode(nodeLabel, "id", edge.getSource());
			Node dest   = getInstance().findNode(nodeLabel, "id", edge.getDest());
			source.createRelationshipTo(dest, RelTypes.NEXT);

			tx.success();
		}
	}

	@Override
	public void addNode(SequenceNode s) {
		try (Transaction tx = getInstance().beginTx()) {
			Node node = getInstance().createNode(nodeLabel);
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
		try (Transaction tx = getInstance().beginTx()) {
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
	 * Retrieves a resource iterator over all roots.
	 * The roots are all the nodes with no incoming edges.
	 * @return	a resource iterator
	 */
	private ResourceIterator<Node> rootIterator() {
		ResourceIterator<Node> roots;
		try (Transaction tx = getInstance().beginTx()) {
			Result res = getInstance().execute(GET_ROOT);
			roots = res.columnAs("s");

			tx.success();
		}
		return roots;
	}

	private Node getRoot() {
		return rootIterator().next();
	}

	/**
	 * Get a topological ordering of the graph.
	 * @return an {@link Iterable}, containing the nodes in
	 * topological order.
	 */
	protected Iterable<Node> topologicalOrder() {
		ResourceIterator<Node> roots = rootIterator();
		return getInstance().traversalDescription()
					.breadthFirst()
					.expand(new IncludesNodesWithoutIncoming())
					.traverse(loop(roots))
					.nodes();
	}

	/**
	 * PathExpander for determining the topological ordering.
	 */
	static class IncludesNodesWithoutIncoming implements PathExpander<Object> {

		private boolean hasUnprocessedIncoming(Node from, Node n) {
			Iterable<Relationship> in = n.getRelationships(RelTypes.NEXT, Direction.INCOMING);
			for (Relationship r : in) {
				if (!(boolean) r.getProperty("processed", false)) {
					return true;
				}
			}
			// All incoming edges have been processed.
			return false;
		}

		@Override
		public Iterable<Relationship> expand(Path path,
				BranchState<Object> state) {
			Node from = path.endNode();
			List<Relationship> expand = new LinkedList<Relationship>();
			for (Relationship r : from.getRelationships(RelTypes.NEXT, Direction.OUTGOING)) {
				r.setProperty("processed", true);
				if (!hasUnprocessedIncoming(from, r.getEndNode())) {
					// All of the dependencies of this node have been added to the result.
					expand.add(r);
				}
			}
			return expand;
		}

		@Override
		public PathExpander<Object> reverse() {
			throw new UnsupportedOperationException();
		}

	}

	private void rankGraph() {
		try (Transaction tx = getInstance().beginTx()) {
			for (Node n : topologicalOrder()) {
				int rankSource = (int) n.getProperty("dist");
				for (Relationship r : n.getRelationships(RelTypes.NEXT, Direction.OUTGOING)) {
					Node dest = r.getEndNode();
					if ((int) dest.getProperty("dist") < rankSource + 1) {
						dest.setProperty("dist", rankSource + 1);
					}
				}
			}
			tx.success();
		}
	}

	@Override
	public SequenceNode getRootNode() {
		SequenceNode node = null;

		try (Transaction tx = getInstance().beginTx()) {
			Node root = getRoot();
			node = createSequenceNode(root);

			tx.success();
		}

		return node;
	}

	@Override
	public SequenceNode getNode(String s) {
		SequenceNode node = null;

		try (Transaction tx = getInstance().beginTx()) {
			node = createSequenceNode(getInstance().findNode(nodeLabel, "id", s));

			tx.success();
		}

		return node;
	}

	@Override
	public List<SequenceNode> getRank(int rank) {
		List<SequenceNode> nodes = new LinkedList<SequenceNode>();

		try (Transaction tx = getInstance().beginTx()) {
			ResourceIterator<Node> res = getInstance().findNodes(nodeLabel, "dist", rank);

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
