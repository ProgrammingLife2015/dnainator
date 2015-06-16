package nl.tudelft.dnainator.graph.impl;

import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Edge;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.GraphBuilder;
import nl.tudelft.dnainator.graph.interestingness.Scores;
import nl.tudelft.dnainator.tree.TreeNode;

import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link GraphBuilder} which uses a BatchInserter.
 */
public class Neo4jBatchBuilder implements GraphBuilder {
	private String storeDir;
	private BatchInserter batchInserter;
	private Map<String, Long> annotationIDToNodeID;
	private Map<String, Long> sequenceIDToNodeID;
	private Map<String, Long> sourceToNodeID;
	private Map<String, Object> annotationProperties;
	private Map<String, Object> nodeProperties;
	private AnnotationCollection annotations;
	private TreeNode phylogeny;

	/**
	 * Create a new {@link Neo4jBatchBuilder}, which batch inserts to
	 * the specified database path.
	 * @param storeDir the path where the database is stored.
	 * @param annotations the {@link AnnotationCollection} to connect the nodes with.
	 */
	public Neo4jBatchBuilder(String storeDir, AnnotationCollection annotations) {
		this(storeDir, annotations, null);
	}

	/**
	 * Create a new {@link Neo4jBatchBuilder}, which batch inserts to
	 * the specified database path.
	 * @param storeDir the path where the database is stored.
	 * @param annotations the {@link AnnotationCollection} to connect the nodes with.
	 * @param phylogeny the phylogenetic tree, for linking the source nodes together.
	 */
	public Neo4jBatchBuilder(String storeDir, AnnotationCollection annotations,
			TreeNode phylogeny) {
		this.annotations = annotations;
		this.storeDir = storeDir;
		this.phylogeny = phylogeny;

		batchInserter = BatchInserters.inserter(storeDir);
		annotationIDToNodeID = new HashMap<>();
		sequenceIDToNodeID = new HashMap<>();
		sourceToNodeID = new HashMap<>();
		annotationProperties = new HashMap<>();
		nodeProperties = new HashMap<>();

		annotations.getAll().forEach(e -> {
			annotationIDToNodeID.put(e.getGeneName(), createAnnotation(e));
		});
	}

	@Override
	public GraphBuilder addEdge(Edge<String> edge) {
		batchInserter.createRelationship(sequenceIDToNodeID.get(edge.getSource()),
				sequenceIDToNodeID.get(edge.getDest()),
				RelTypes.NEXT, null);
		return this;
	}

	@Override
	public GraphBuilder addNode(SequenceNode s) {
		long nodeId = createNode(s);
		sequenceIDToNodeID.put(s.getId(), nodeId);

		s.getSources().forEach(source -> {
			connectSource(nodeId, source);
			if (source.equals("TKK_REF")) {
				annotations.getSubrange(s.getStartRef(), s.getEndRef())
					.forEach(a -> connectAnnotation(nodeId, a));
			}
		});
		return this;
	}

	@Override
	public Graph build() {
		connectPhylogeny();
		createIndicesAndConstraints();
		batchInserter.shutdown();

		// Initialize the graph.
		Neo4jGraph g = new Neo4jGraph(storeDir);
		g.analyze();
		return g;
	}

	/**
	 * Create all indices using deferred initialization.
	 */
	private void createIndicesAndConstraints() {
		batchInserter.createDeferredConstraint(NodeLabels.NODE)
			.assertPropertyIsUnique(PropertyTypes.ID.name())
			.create();
		batchInserter.createDeferredConstraint(NodeLabels.SOURCE)
			.assertPropertyIsUnique(PropertyTypes.SOURCE.name())
			.create();
		batchInserter.createDeferredSchemaIndex(NodeLabels.NODE)
			.on(PropertyTypes.RANK.name())
			.create();
		batchInserter.createDeferredSchemaIndex(NodeLabels.NODE)
			.on(PropertyTypes.STARTREF.name())
			.create();
		batchInserter.createDeferredSchemaIndex(NodeLabels.NODE)
			.on(PropertyTypes.ENDREF.name())
			.create();
	}

	/**
	 * Connect a sequence node to an annotation node.
	 * @param nodeId	the sequence node
	 * @param source	the annotation
	 */
	private void connectAnnotation(long nodeId, Annotation annotation) {
		long annotationId = annotationIDToNodeID.get(annotation.getGeneName());
		batchInserter.createRelationship(nodeId, annotationId, RelTypes.ANNOTATED, null);
	}

	/**
	 * Connect a sequence node to a source.
	 * @param nodeId	the sequence node
	 * @param source	the source node
	 */
	private void connectSource(long nodeId, String source) {
		long sourceId;
		if (!sourceToNodeID.containsKey(source)) {
			sourceId = createSource(source);
			sourceToNodeID.put(source, sourceId);
		} else {
			sourceId = sourceToNodeID.get(source);
		}
		batchInserter.createRelationship(nodeId, sourceId, RelTypes.SOURCE, null);
	}

	/**
	 * Connect the root phylogeny node and all of its children to their sources (recursively).
	 */
	private void connectPhylogeny() {
		if (phylogeny != null) {
			connectPhylogeny(0, phylogeny);
		}
	}

	/**
	 * Connect a phylogeny node and its children to their sources.
	 * @param distanceToRoot	distance of this node to root of the tree
	 * @param current		the current phylogeny tree node
	 * @return			the id of the created node
	 */
	private long connectPhylogeny(int distanceToRoot, TreeNode current) {
		if (current.getChildren().size() == 0) {
			return sourceToNodeID.get(current.getName());
		}
		long anc = createAncestor(distanceToRoot);
		current.getChildren().forEach(child ->
			batchInserter.createRelationship(anc,
					connectPhylogeny(distanceToRoot + 1, child),
					RelTypes.ANCESTOR_OF, null)
		);
		return anc;
	}

	/**
	 * Create an annotation node.
	 * @param a	the annotation
	 * @return	the id of the created node
	 */
	private long createAnnotation(Annotation a) {
		annotationProperties.put(PropertyTypes.ID.name(), a.getGeneName());
		annotationProperties.put(PropertyTypes.STARTREF.name(), a.getStart());
		annotationProperties.put(PropertyTypes.ENDREF.name(), a.getEnd());
		annotationProperties.put(PropertyTypes.SENSE.name(), a.isSense());
		return batchInserter.createNode(annotationProperties, NodeLabels.ANNOTATION);
	}

	/**
	 * Create a phylogeny ancestor node.
	 * @param distanceToRoot	distance of this node to root of the tree
	 * @return			the id of the created node
	 */
	private long createAncestor(int distanceToRoot) {
		return batchInserter.createNode(
				Collections.singletonMap(PropertyTypes.DIST_TO_ROOT.name(), distanceToRoot),
				NodeLabels.ANCESTOR);
	}

	/**
	 * Create a sequence node.
	 * @param s	the sequencenode
	 * @return	the id of the created node
	 */
	private long createNode(SequenceNode s) {
		nodeProperties.put(PropertyTypes.ID.name(), s.getId());
		nodeProperties.put(PropertyTypes.STARTREF.name(), s.getStartRef());
		nodeProperties.put(PropertyTypes.ENDREF.name(), s.getEndRef());
		nodeProperties.put(PropertyTypes.SEQUENCE.name(), s.getSequence());
		nodeProperties.put(PropertyTypes.RANK.name(), 0);
		nodeProperties.put(Scores.SEQ_LENGTH.getName(), s.getSequence().length());
		return batchInserter.createNode(nodeProperties, NodeLabels.NODE);
	}

	/**
	 * Create a source node.
	 * @param s	the source
	 * @return	the id of the created node
	 */
	private long createSource(String source) {
		return batchInserter.createNode(
				Collections.singletonMap(PropertyTypes.SOURCE.name(), source),
				NodeLabels.SOURCE);
	}
}
