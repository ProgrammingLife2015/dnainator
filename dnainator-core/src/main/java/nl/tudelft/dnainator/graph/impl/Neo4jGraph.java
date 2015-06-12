package nl.tudelft.dnainator.graph.impl;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.annotation.AnnotationCollectionFactory;
import nl.tudelft.dnainator.annotation.Range;
import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.command.Command;
import nl.tudelft.dnainator.graph.impl.command.AnalyzeCommand;
import nl.tudelft.dnainator.graph.impl.query.AllClustersQuery;
import nl.tudelft.dnainator.graph.impl.query.Query;
import nl.tudelft.dnainator.graph.interestingness.InterestingnessStrategy;
import nl.tudelft.dnainator.graph.interestingness.impl.SummingScoresStrategy;
import nl.tudelft.dnainator.graph.query.GraphQueryDescription;
import nl.tudelft.dnainator.parser.AnnotationParser;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class realizes a graphfactory using Neo4j as it's backend.
 */
public final class Neo4jGraph implements Graph {
	private static final String GET_MAX_RANK = "MATCH (n:" + NodeLabels.NODE.name() + ") "
			+ "RETURN MAX(n." + PropertyTypes.RANK.name() + ") AS max";
	private static final String GET_ROOT = "MATCH (s:" + NodeLabels.NODE.name() + ") "
			+ "WHERE NOT (s)<-[:NEXT]-(:" + NodeLabels.NODE.name() + ") "
			+ "RETURN s";
	private static final String GET_RANGE =
			"MATCH (n:" + NodeLabels.NODE.name() + ")-[r:" + NodeLabels.SOURCE.name() + "]->s "
			+ "WHERE s." + PropertyTypes.SOURCE.name() + " = \"TKK_REF\" "
			+ "AND n." + PropertyTypes.STARTREF.name() + " <= {to} "
			+ "AND n." + PropertyTypes.ENDREF.name() + " >= {from} RETURN n";
	private static final String GET_SUB_RANGE =
			"MATCH (a:" + NodeLabels.ANNOTATION.name() + ") "
			+ "WHERE a." + PropertyTypes.STARTREF.name() + " < {to} "
			+ "AND a." + PropertyTypes.ENDREF.name() + " >= {from} RETURN a";
	private static final String GET_ANNOTATION_BY_RANK =
			"MATCH (n:" + NodeLabels.NODE.name() + ")-[r:" + RelTypes.ANNOTATED.name() + "]->a "
			+ "WHERE n." + PropertyTypes.RANK.name() + " >= {from} "
			+   "AND n." + PropertyTypes.RANK.name() + " <= {to} RETURN DISTINCT a";

	private GraphDatabaseService service;
	private InterestingnessStrategy is;

	/**
	 * Constructs a Neo4j database on the specified path, using
	 * the default annotation collection factory ({@link AnnotationCollectionFactory}).
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
		this.is = new SummingScoresStrategy();
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
	public EnrichedSequenceNode getRootNode() {
		return query(e -> createSequenceNode(getRoot()));
	}

	@Override
	public EnrichedSequenceNode getNode(String s) {
		return query(e -> createSequenceNode(e.findNode(NodeLabels.NODE,
				PropertyTypes.ID.name(), s)));
	}

	@Override
	public AnnotationCollection getAnnotations() {
		return this;
	}

	@Override
	public List<EnrichedSequenceNode> getRank(int rank) {
		return query(e -> {
			ResourceIterator<Node> res = e.findNodes(NodeLabels.NODE,
					PropertyTypes.RANK.name(), rank);
			List<EnrichedSequenceNode> nodes = new LinkedList<>();
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
	public EnrichedSequenceNode createSequenceNode(Node node) {
		if (node == null) {
			return null;
		}
		return new Neo4jSequenceNode(service, node);
	}

	@Override
	public int getMaxRank() {
		return query(e -> (int) e.execute(GET_MAX_RANK).columnAs("max").next());
	}

	@Override
	public Map<Integer, List<Cluster>> getAllClusters(List<String> startNodes,
							int end, int threshold) {
		return query(new AllClustersQuery(startNodes, end, threshold, is));
	}

	@Override
	public List<EnrichedSequenceNode> queryNodes(GraphQueryDescription qd) {
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

	@Override
	public void addAnnotations(AnnotationParser source) {
		execute(e -> addAnnotations(source));
	}

	@Override
	public void addAnnotation(Annotation a) {
		Map<String, Object> parameters = new HashMap<>(2);
		parameters.put("from", a.getStart());
		parameters.put("to", a.getEnd());
		execute(e -> {
			Node annotation = e.createNode(NodeLabels.ANNOTATION);
			annotation.setProperty(PropertyTypes.ID.name(), a.getGeneName());
			annotation.setProperty(PropertyTypes.STARTREF.name(), a.getStart());
			annotation.setProperty(PropertyTypes.ENDREF.name(), a.getEnd());
			annotation.setProperty(PropertyTypes.SENSE.name(), a.isSense());

			ResourceIterator<Node> nodes = service.execute(GET_RANGE, parameters).columnAs("n");
			nodes.forEachRemaining(n -> n.createRelationshipTo(annotation, RelTypes.ANNOTATED));
		});
	}

	private Collection<Annotation> getAnnotationRange(Range r, String query) {
		Map<String, Object> parameters = new HashMap<>(2);
		List<Annotation> result = new LinkedList<Annotation>();
		parameters.put("from", r.getX());
		parameters.put("to", r.getY());
		return query(service -> {
			ResourceIterator<Node> annotations = service.execute(query, parameters)
					.columnAs("a");
			while (annotations.hasNext()) {
				result.add(new Neo4jAnnotation(service, annotations.next()));
			}
			return result;
		});
	}

	@Override
	public Collection<Annotation> getSubrange(Range r) {
		return getAnnotationRange(r, GET_SUB_RANGE);
	}

	@Override
	public Collection<Annotation> getAnnotationByRank(Range r) {
		return getAnnotationRange(r, GET_ANNOTATION_BY_RANK);
	}

	@Override
	public void setInterestingnessStrategy(InterestingnessStrategy is) {
		this.is = is;
	}

	/**
	 * Analyzes the graph by doing a pass over the entire graph in topological
	 * order, to assign ranks and scores to nodes.
	 */
	protected void analyze() {
		// Rank the graph.
		execute(e -> new AnalyzeCommand(rootIterator()).execute(e));
	}
}
