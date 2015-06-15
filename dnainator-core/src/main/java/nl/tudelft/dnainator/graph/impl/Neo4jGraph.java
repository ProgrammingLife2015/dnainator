package nl.tudelft.dnainator.graph.impl;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.annotation.AnnotationCollectionFactory;
import nl.tudelft.dnainator.annotation.Range;
import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.command.AnalyzeCommand;
import nl.tudelft.dnainator.graph.impl.command.Command;
import nl.tudelft.dnainator.graph.impl.properties.AnnotationProperties;
import nl.tudelft.dnainator.graph.impl.properties.PhylogenyProperties;
import nl.tudelft.dnainator.graph.impl.properties.SequenceProperties;
import nl.tudelft.dnainator.graph.impl.properties.SourceProperties;
import nl.tudelft.dnainator.graph.impl.query.AllClustersQuery;
import nl.tudelft.dnainator.graph.impl.query.Query;
import nl.tudelft.dnainator.graph.interestingness.InterestingnessStrategy;
import nl.tudelft.dnainator.graph.interestingness.Scores;
import nl.tudelft.dnainator.graph.interestingness.impl.SummingScoresStrategy;
import nl.tudelft.dnainator.graph.query.GraphQueryDescription;
import nl.tudelft.dnainator.parser.AnnotationParser;
import nl.tudelft.dnainator.tree.TreeNode;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class realizes a graphfactory using Neo4j as it's backend.
 */
public final class Neo4jGraph implements Graph {
	private static final String GET_MAX_RANK = "MATCH (n:" + NodeLabels.NODE.name() + ") "
			+ "RETURN MAX(n." + SequenceProperties.RANK.name() + ") AS max";
	private static final String GET_MAX_BASEPAIRS = "MATCH (n:" + NodeLabels.NODE.name() + ") "
			+ "RETURN MAX(n." + SequenceProperties.BASE_DIST.name() + ") AS max";
	private static final String GET_RANK_FROM_BASEPAIR = "MATCH (n:" + NodeLabels.NODE.name() + ") "
			+ "WHERE {dist} > n." + SequenceProperties.BASE_DIST.name()
			+ " AND {dist} < n." + SequenceProperties.BASE_DIST.name()
			+ " + n." + Scores.SEQ_LENGTH.name() + " RETURN n." + SequenceProperties.RANK.name()
			+ " AS rank";
	private static final String GET_ROOT = "MATCH (s:" + NodeLabels.NODE.name() + ") "
			+ "WHERE NOT (s)<-[:NEXT]-(:" + NodeLabels.NODE.name() + ") "
			+ "RETURN s";
	private static final String GET_PHYLO_ROOT =
			"MATCH n-[r:" + RelTypes.ANCESTOR_OF.name() + "]->s "
			+ "WHERE n." + PhylogenyProperties.DIST_TO_ROOT.name() + " = 0 RETURN n";
	private static final String GET_REF_RANGE =
			"MATCH (n:" + NodeLabels.NODE.name() + ")-[r:" + NodeLabels.SOURCE.name() + "]->s "
			+ "WHERE s." + SourceProperties.SOURCE.name() + " = \"TKK_REF\" "
			+ "AND n." + SequenceProperties.STARTREF.name() + " <= {to} "
			+ "AND n." + SequenceProperties.ENDREF.name() + " >= {from} RETURN n";
	private static final String GET_SUB_RANGE =
			"MATCH (a:" + NodeLabels.ANNOTATION.name() + ") "
			+ "WHERE a." + AnnotationProperties.STARTREF.name() + " < {to} "
			+ "AND a." + AnnotationProperties.ENDREF.name() + " >= {from} RETURN a";
	private static final String GET_ANNOTATION_BY_RANK =
			"MATCH (n:" + NodeLabels.NODE.name() + ")-[r:" + RelTypes.ANNOTATED.name() + "]->a "
			+ "WHERE n." + SequenceProperties.RANK.name() + " >= {from} "
			+   "AND n." + SequenceProperties.RANK.name() + " <= {to} RETURN DISTINCT a";

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
		return query(e -> {
			Node n = e.findNode(NodeLabels.NODE, SequenceProperties.ID.name(), s);
			if (n != null) {
				return createSequenceNode(n);
			}
			return null;
		});
	}

	@Override
	public AnnotationCollection getAnnotations() {
		return this;
	}

	@Override
	public List<EnrichedSequenceNode> getRank(int rank) {
		return query(e -> {
			ResourceIterator<Node> res = e.findNodes(NodeLabels.NODE,
					SequenceProperties.RANK.name(), rank);
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
		return new Neo4jSequenceNode(service, node);
	}

	@Override
	public int getMaxRank() {
		return query(e -> (int) e.execute(GET_MAX_RANK).columnAs("max").next());
	}

	@Override
	public int getMaxBasePairs() {
		return query(e -> (int) e.execute(GET_MAX_BASEPAIRS).columnAs("max").next());
	}

	@Override
	public int getRankFromBasePair(int base) {
		Map<String, Object> parameters = Collections.singletonMap("dist", base);
		return query(e -> (int) e.execute(GET_RANK_FROM_BASEPAIR, parameters)
				.columnAs("rank").next());
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
			annotation.setProperty(AnnotationProperties.ID.name(), a.getGeneName());
			annotation.setProperty(AnnotationProperties.STARTREF.name(), a.getStart());
			annotation.setProperty(AnnotationProperties.ENDREF.name(), a.getEnd());
			annotation.setProperty(AnnotationProperties.SENSE.name(), a.isSense());

			ResourceIterator<Node> nodes = service.execute(GET_REF_RANGE, parameters).columnAs("n");
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

	@Override
	public TreeNode getTree() {
		return query(e -> {
			ResourceIterator<Node> dbroots = e.execute(GET_PHYLO_ROOT).columnAs("n");
			Node dbroot = dbroots.next();

			return getTree(dbroot, new TreeNode(null));
		});
	}

	private TreeNode getTree(Node parent, TreeNode treeparent) {
		for (Relationship r : parent.getRelationships(Direction.OUTGOING, RelTypes.ANCESTOR_OF)) {
			TreeNode child = new TreeNode(treeparent);
			child.setDistance((int) parent.getProperty(PhylogenyProperties
									.DIST_TO_ROOT.name()) + 1);

			if (r.getEndNode().hasLabel(NodeLabels.SOURCE)) {
				child.setName((String) r.getEndNode().getProperty(SourceProperties.SOURCE.name()));
			} else {
				getTree(r.getEndNode(), child);
			}
		}
		return treeparent;
	}
}
