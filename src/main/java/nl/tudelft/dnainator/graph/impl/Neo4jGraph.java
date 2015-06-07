package nl.tudelft.dnainator.graph.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.annotation.AnnotationCollectionFactory;
import nl.tudelft.dnainator.annotation.impl.AnnotationCollectionFactoryImpl;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.command.Command;
import nl.tudelft.dnainator.graph.impl.command.RankCommand;
import nl.tudelft.dnainator.graph.impl.query.AllClustersQuery;
import nl.tudelft.dnainator.graph.impl.query.ClusterQuery;
import nl.tudelft.dnainator.graph.impl.query.ClustersFromQuery;
import nl.tudelft.dnainator.graph.impl.query.Query;
import nl.tudelft.dnainator.graph.query.GraphQueryDescription;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * This class realizes a graphfactory using Neo4j as it's backend.
 */
public final class Neo4jGraph implements Graph {
	private static final String GET_MAX_RANK = "MATCH n RETURN MAX(n."
			+ PropertyTypes.RANK.name() + ")";
	private static final String GET_ROOT = "MATCH (s:" + NodeLabels.NODE.name() + ") "
			+ "WHERE NOT (s)<-[:NEXT]-(:" + NodeLabels.NODE.name() + ") "
			+ "RETURN s";
	private GraphDatabaseService service;
	private AnnotationCollection annotations;

	/**
	 * Constructs a Neo4j database on the specified path, using
	 * the default annotation collection factory ({@link AnnotationCollectionFactory}).
	 * @param path			specified path
	 */
	public Neo4jGraph(String path) {
		this(new AnnotationCollectionFactoryImpl(), path);
	}

	/**
	 * Constructs a Neo4j database on the specified path.
	 * @param fact			the factory for building the {@link AnnotationCollection}.
	 * @param path			specified path
	 */
	public Neo4jGraph(AnnotationCollectionFactory fact, String path) {
		// Create our database and register a shutdown hook
		service = new GraphDatabaseFactory().newEmbeddedDatabase(path);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				service.shutdown();
			}
		});

		// Rank the graph.
		execute(e -> new RankCommand(rootIterator()).execute(e));
		this.annotations = fact.build();
	}

	/**
	 * Retrieves a resource iterator over all roots.
	 * The roots are all the nodes with no incoming edges.
	 * @return	a resource iterator
	 */
	protected ResourceIterator<Node> rootIterator() {
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
		return query(e -> createSequenceNode(getRoot()));
	}

	@Override
	public SequenceNode getNode(String s) {
		return query(e -> createSequenceNode(e.findNode(NodeLabels.NODE,
				PropertyTypes.ID.name(), s)));
	}

	@Override
	public AnnotationCollection getAnnotations() {
		return annotations;
	}

	@Override
	public List<SequenceNode> getRank(int rank) {
		return query(e -> {
			ResourceIterator<Node> res = e.findNodes(NodeLabels.NODE,
					PropertyTypes.RANK.name(), rank);
			List<SequenceNode> nodes = new LinkedList<>();
			res.forEachRemaining(n -> nodes.add(createSequenceNode(n)));

			return nodes;
		});
	}

	/**
	 * Create a {@link SequenceNode} from the information in the given
	 * Neo4j {@link Node}.
	 * @param node from the database.
	 * @return a {@link SequenceNode} with the information of the given {@link Node}.
	 */
	public SequenceNode createSequenceNode(Node node) {
		return new Neo4jSequenceNode(service, node);
	}

	@Override
	public List<List<SequenceNode>> getRanks() {
		return query(e -> {
			int maxrank = (int) e.execute(GET_MAX_RANK).columnAs("s").next();
			List<List<SequenceNode>> nodes = new LinkedList<>();

			for (int i = 0; i < maxrank; i++) {
				nodes.add(getRank(i));
			}

			return nodes;
		});
	}

	/**
	 * Return a single cluster using the specified values.
	 * @param start		the start node
	 * @param threshold	the cluster threshold
	 * @return		a list of sequence nodes
	 */
	protected List<SequenceNode> getCluster(String start, int threshold) {
		return query(new ClusterQuery(new HashSet<String>(), start, threshold)).getNodes();
	}

	@Override
	public Queue<Cluster> getClustersFrom(Set<String> visited,
						List<String> startNodes, int threshold) {
		return query(new ClustersFromQuery(visited, startNodes, threshold));
	}

	@Override
	public Map<Integer, List<Cluster>> getAllClusters(List<String> startNodes,
							int end, int threshold) {
		return query(new AllClustersQuery(startNodes, end, threshold));
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
		try (Transaction tx = service.beginTx()) {
			c.execute(service);
			tx.success();
		}
	}

	/**
	 * Execute a query on this database.
	 * @param q	the query
	 * @param <T>	the result type
	 * @return	the result of the query
	 */
//	public Cluster query(Query<? extends QueryResult> q) {
	public <T> T query(Query<T> q) {
		T res = null;
		try (Transaction tx = service.beginTx()) {
			res = q.execute(service);
			tx.success();
		}
		return res;
	}

	/**
	 * Shut down this database.
	 * USE WITH CAUTION!
	 */
	public void shutdown() {
		service.shutdown();
	}
}
