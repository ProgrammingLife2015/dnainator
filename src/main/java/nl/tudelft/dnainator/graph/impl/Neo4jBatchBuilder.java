package nl.tudelft.dnainator.graph.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Edge;
import nl.tudelft.dnainator.graph.GraphBuilder;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;

/**
 * A {@link GraphBuilder} which uses a BatchInserter.
 */
public class Neo4jBatchBuilder implements GraphBuilder {
	private BatchInserter batchInserter;
	private Map<String, Long> sequenceIDToNodeID;
	private Map<String, Long> sourceNodeIDs;
	private Map<String, Object> nodeProperties;

	/**
	 * Create a new {@link Neo4jBatchBuilder}, which batch inserts to
	 * the specified database path.
	 * @param storeDir the path where the database is stored.
	 */
	public Neo4jBatchBuilder(String storeDir) {
		batchInserter = BatchInserters.inserter(storeDir);
		sequenceIDToNodeID = new HashMap<>();
		sourceNodeIDs = new HashMap<>();
		nodeProperties = new HashMap<>();
	}

	@Override
	public void constructGraph(NodeParser np, EdgeParser ep)
			throws IOException, ParseException {
		GraphBuilder.super.constructGraph(np, ep);
		// Create the indices and constraints.
		batchInserter.createDeferredConstraint(Neo4jGraph.NODELABEL)
			.assertPropertyIsUnique(PropertyTypes.ID.name())
			.create();
		batchInserter.createDeferredSchemaIndex(Neo4jGraph.NODELABEL)
			.on(PropertyTypes.RANK.name())
			.create();
		batchInserter.createDeferredConstraint(Neo4jGraph.SOURCELABEL)
			.assertPropertyIsUnique(PropertyTypes.SOURCE.name())
			.create();
		batchInserter.shutdown();
	}

	@Override
	public void addEdge(Edge<String> edge) {
		batchInserter.createRelationship(sequenceIDToNodeID.get(edge.getSource()),
				sequenceIDToNodeID.get(edge.getDest()),
				RelTypes.NEXT, null);
	}

	@Override
	public void addNode(SequenceNode s) {
		long nodeId = createNode(s);
		sequenceIDToNodeID.put(s.getId(), nodeId);

		s.getSources().forEach(e -> {
			long source;
			if (!sourceNodeIDs.containsKey(e)) {
				source = createSource(e);
				sourceNodeIDs.put(e, source);
			} else {
				source = sourceNodeIDs.get(e);
			}
			batchInserter.createRelationship(nodeId, source, RelTypes.SOURCE, null);
		});
	}

	private long createNode(SequenceNode s) {
		nodeProperties.put(PropertyTypes.ID.name(), s.getId());
		nodeProperties.put(PropertyTypes.STARTREF.name(), s.getStartRef());
		nodeProperties.put(PropertyTypes.ENDREF.name(), s.getEndRef());
		nodeProperties.put(PropertyTypes.SEQUENCE.name(), s.getSequence());
		nodeProperties.put(PropertyTypes.RANK.name(), 0);
		nodeProperties.put(PropertyTypes.SCORE.name(), s.getSequence().length());
		return batchInserter.createNode(nodeProperties, Neo4jGraph.NODELABEL);
	}

	private long createSource(String source) {
		return batchInserter.createNode(
				Collections.singletonMap(PropertyTypes.SOURCE.name(), source),
				Neo4jGraph.SOURCELABEL);
	}
}
