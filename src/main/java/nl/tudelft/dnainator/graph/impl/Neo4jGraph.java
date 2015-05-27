package nl.tudelft.dnainator.graph.impl;

import static nl.tudelft.dnainator.graph.impl.PropertyTypes.ENDREF;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.ID;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.NODELABEL;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.RANK;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.SEQUENCE;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.SOURCE;
import static nl.tudelft.dnainator.graph.impl.PropertyTypes.STARTREF;
import static org.neo4j.helpers.collection.IteratorUtil.loop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.core.impl.Edge;
import nl.tudelft.dnainator.core.impl.SequenceNodeImpl;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.command.Command;
import nl.tudelft.dnainator.graph.impl.command.RankCommand;
import nl.tudelft.dnainator.graph.impl.query.ClusterQuery;
import nl.tudelft.dnainator.graph.impl.query.Query;
import nl.tudelft.dnainator.graph.query.GraphQueryDescription;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

/**
 * This class realizes a graphfactory using Neo4j as it's backend.
 */
public final class Neo4jGraph implements Graph {
	private static final String GET_MAX_RANK = "MATCH n RETURN MAX(n." + RANK.name() + ")";
	private static final String GET_ROOT = "MATCH (s:" + NODELABEL.name() + ") "
			+ "WHERE NOT (s)<-[:NEXT]-(:" + NODELABEL.name() + ") "
			+ "RETURN s";

	private GraphDatabaseService service;
	private Label nodeLabel;

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
		nodeLabel = DynamicLabel.label(NODELABEL.name());
		// Recreate our indices
		try (Transaction tx = service.beginTx()) {
			service.schema().getConstraints().forEach(e -> e.drop());
			service.schema().getIndexes().forEach(e -> e.drop());

			service.schema().constraintFor(nodeLabel)
			.assertPropertyIsUnique(ID.name())
			.create();

			// Generate an index on 'dist'
			service.schema().indexFor(nodeLabel)
			.on(RANK.name())
			.create();

			tx.success();
		}
	}

	/**
	 * Delete all nodes and relationships from this graph.
	 */
	public void clear() {
		try (Transaction tx = service.beginTx()) {
			for (Relationship r : GlobalGraphOperations.at(service).getAllRelationships()) {
				r.delete();
			}
			for (Node n : GlobalGraphOperations.at(service).getAllNodes()) {
				n.delete();
			}

			tx.success();
		}
	}

	@Override
	public void addEdge(Edge<String> edge) {
		try (Transaction tx = service.beginTx()) {
			Node source = service.findNode(nodeLabel, ID.name(), edge.getSource());
			Node dest   = service.findNode(nodeLabel, ID.name(), edge.getDest());
			source.createRelationshipTo(dest, RelTypes.NEXT);

			tx.success();
		}
	}

	@Override
	public void addNode(SequenceNode s) {
		try (Transaction tx = service.beginTx()) {
			Node node = service.createNode(nodeLabel);
			node.setProperty(ID.name(), s.getId());
			node.setProperty(STARTREF.name(), s.getStartRef());
			node.setProperty(ENDREF.name(), s.getEndRef());
			node.setProperty(SEQUENCE.name(), s.getSequence());
			node.setProperty(SOURCE.name(), s.getSource());
			node.setProperty(RANK.name(), 0);

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
			execute(new RankCommand(rootIterator()));

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

	private Node getRoot() {
		return rootIterator().next();
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
			node = createSequenceNode(service.findNode(nodeLabel, ID.name(), s));

			tx.success();
		}

		return node;
	}

	@Override
	public List<SequenceNode> getRank(int rank) {
		List<SequenceNode> nodes = new LinkedList<>();

		try (Transaction tx = service.beginTx()) {
			ResourceIterator<Node> res = service.findNodes(nodeLabel, RANK.name(), rank);

			for (Node n : loop(res)) {
				nodes.add(createSequenceNode(n));
			}

			tx.success();
		}

		return nodes;
	}

	/**
	 * Create a {@link SequenceNode} from the information in the given
	 * Neo4j {@link Node}.
	 * @param node from the database.
	 * @return a {@link SequenceNode} with the information of the given {@link Node}.
	 */
	public static SequenceNode createSequenceNode(Node node) {
		String id	= (String) node.getProperty(ID.name());
		String source	= (String) node.getProperty(SOURCE.name());
		int startref	= (int)    node.getProperty(STARTREF.name());
		int endref	= (int)    node.getProperty(ENDREF.name());
		String sequence	= (String) node.getProperty(SEQUENCE.name());
		int rank	= (int)    node.getProperty(RANK.name());

		List<String> outgoing = new ArrayList<>();
		for (Relationship e : loop(node.getRelationships(Direction.OUTGOING).iterator())) {
			outgoing.add((String) e.getEndNode().getProperty(ID.name()));
		}

		return new SequenceNodeImpl(id, source, startref, endref, sequence, rank, outgoing);
	}

	@Override
	public List<List<SequenceNode>> getRanks() {
		int maxrank = (int) service.execute(GET_MAX_RANK).columnAs("s").next();
		List<List<SequenceNode>> nodes = new LinkedList<>();
		try (Transaction tx = service.beginTx()) {
			for (int i = 0; i < maxrank; i++) {
				nodes.add(getRank(i));
			}

			tx.success();
		}

		return nodes;
	}

	protected List<SequenceNode> getCluster(String start, int threshold) {
		try (Transaction tx = service.beginTx()) {
			return new ClusterQuery(new HashSet<String>(), start, threshold)
					.execute(service).getNodes();
		}
	}

	@Override
	public Queue<Cluster> getClusters(Set<String> visited, List<String> startNodes, int threshold) {
		Queue<Cluster> rootClusters = new LinkedList<Cluster>();

		try (Transaction tx = service.beginTx()) {
			for (String sn : startNodes) {
				// Continue if this startNode was consumed by another cluster
				if (visited.contains(sn)) {
					continue;
				}

				// Otherwise get the cluster starting from this startNode
				Cluster c = new ClusterQuery(visited, sn, threshold).execute(service);
				rootClusters.add(c);
			}

			tx.success();
		}

		return rootClusters;
	}

	@Override
	public Map<Integer, List<Cluster>> getClusters(List<String> startNodes,
							int end, int threshold) {
		Queue<Cluster> rootClusters = new PriorityQueue<>((e1, e2) -> 
			e1.getStartRank() - e2.getStartRank()
		);
		Set<String> visited = new HashSet<>();
		Map<Integer, List<Cluster>> result = new HashMap<Integer, List<Cluster>>();

		try (Transaction tx = service.beginTx()) {
			// Retrieve the root clusters starting from the startNodes and add them to the queue
			rootClusters.addAll(getClusters(visited, startNodes, threshold));

			// Find adjacent clusters as long as there are root clusters in the queue
			while (!rootClusters.isEmpty()) {
				Cluster c = rootClusters.poll();
				if (c.getStartRank() > end) {
					break;
				}
				// TODO: Might want to introduce a QueryResult class instead of this map
				result.putIfAbsent(c.getStartRank(), new ArrayList<>());
				result.get(c.getStartRank()).add(c);

				c.getNodes().forEach(sn -> {
					rootClusters.addAll(getClusters(visited, sn.getOutgoing(), threshold));
				});
			}

			tx.success();
		}
		return result;
	}

	@Override
	public List<SequenceNode> queryNodes(GraphQueryDescription qd) {
		return Neo4jQuery.of(qd).execute(service);
	}

	/**
	 * Execute a command on this database.
	 * @param c	the command
	 */
	public void execute(Command c) {
		c.execute(service);
	}

	/**
	 * Execute a query on this database.
	 * @param q	the query
	 * @param <T>	the result type
	 * @return	the result of the query
	 */
//	public Cluster query(Query<? extends QueryResult> q) {
	public <T> T query(Query<T> q) {
		return q.execute(service);
	}
}
