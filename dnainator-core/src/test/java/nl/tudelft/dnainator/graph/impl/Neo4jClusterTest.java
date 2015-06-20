package nl.tudelft.dnainator.graph.impl;

import nl.tudelft.dnainator.annotation.impl.AnnotationCollectionImpl;
import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.core.impl.SequenceNodeFactoryImpl;
import nl.tudelft.dnainator.graph.interestingness.Scores;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;
import nl.tudelft.dnainator.parser.impl.EdgeParserImpl;
import nl.tudelft.dnainator.parser.impl.NodeParserImpl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;
import org.neo4j.io.fs.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static nl.tudelft.dnainator.graph.impl.Neo4jTestUtils.assertUnorderedIDEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test clustering in a DNA sequence graph.
 */
public class Neo4jClusterTest {
	private static final String DB_PATH = "target/neo4j-junit";
	private static Neo4jGraph db;
	private static InputStream nodeFile;
	private static InputStream edgeFile;

	/**
	 * Setup the database and construct the graph.
	 */
	@BeforeClass
	public static void setUp() {
		try {
			FileUtils.deleteRecursively(new File(DB_PATH));
			nodeFile = Neo4jGraphTest.class.getResourceAsStream("/strains/cluster.node.graph");
			edgeFile = Neo4jGraphTest.class.getResourceAsStream("/strains/cluster.edge.graph");
			NodeParser np = new NodeParserImpl(new SequenceNodeFactoryImpl(),
					new BufferedReader(new InputStreamReader(nodeFile, "UTF-8")));
			EdgeParser ep = new EdgeParserImpl(new BufferedReader(
							new InputStreamReader(edgeFile, "UTF-8")));
			db = (Neo4jGraph) new Neo4jBatchBuilder(DB_PATH, new AnnotationCollectionImpl())
				.constructGraph(np, ep)
				.build();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	private Stream<EnrichedSequenceNode> getAllNodes(Map<Integer, List<Cluster>> clusters) {
		return clusters.values().stream()
				.flatMap(list -> list.stream())
				.flatMap(cluster -> cluster.getNodes().stream());
	}

	/**
	 * Test returning various clusters from the sample graph.
	 */
	@Test
	public void testSingleNestedBubble() {
		// CHECKSTYLE.OFF: MagicNumber
		Map<Integer, List<Cluster>> clusters = db.getAllClusters(0, 6, 11);
		System.out.println(clusters);
		assertProperClustering(clusters, 10);
		testSingleNestedBubbleNodes(clusters);
		// CHECKSTYLE.ON: MagicNumber
	}

	private void testSingleNestedBubbleNodes(Map<Integer, List<Cluster>> clusters) {
		// CHECKSTYLE.OFF: MagicNumber
		// The root node is not associated with a bubble, so it should be a singleton cluster.
		assertUnorderedIDEquals(Sets.newSet("0"), clusters.get(0).get(0).getNodes());

		// first bubble is not clustered because one node has length greater than 11.
		assertUnorderedIDEquals(Sets.newSet("1"), clusters.get(1).get(0).getNodes());

		// 2 and 3 Expected on rank 1
		assertUnorderedIDEquals(Sets.newSet("2"), clusters.get(2).get(1).getNodes());
		assertUnorderedIDEquals(Sets.newSet("3"), clusters.get(2).get(0).getNodes());

		// Source node is not collapsed.
		assertUnorderedIDEquals(Sets.newSet("4"), clusters.get(3).get(0).getNodes());

		// Collapsed bubble.
		assertUnorderedIDEquals(Sets.newSet("5"), clusters.get(4).get(1).getNodes());
		assertUnorderedIDEquals(Sets.newSet("6"), clusters.get(4).get(0).getNodes());

		// Multiple bubble sink.
		assertUnorderedIDEquals(Sets.newSet("7", "20"), clusters.get(5).get(0).getNodes());

		// Sink node is not collapsed.
		assertUnorderedIDEquals(Sets.newSet("8"), clusters.get(6).get(0).getNodes());
	}

	/**
	 * Test the part of the graph that has multiple bubbles nested.
	 */
	@Test
	public void testMultipleNestedBubbles() {
		// CHECKSTYLE.OFF: MagicNumber
		Map<Integer, List<Cluster>> clusters = db.getAllClusters(7, 13, 11);
		System.out.println(clusters);
		assertProperClustering(clusters, 11);
		testMultipleNestedBubbleNodes(clusters);
	}

	private void testMultipleNestedBubbleNodes(Map<Integer, List<Cluster>> clusters) {
		// CHECKSTYLE.OFF: MagicNumber
		// Source node of new bubble is not collapsed.
		assertUnorderedIDEquals(Sets.newSet("9"), clusters.get(7).get(0).getNodes());

		// Source node of nested bubble is not collapsed.
		assertUnorderedIDEquals(Sets.newSet("18"), clusters.get(8).get(0).getNodes());
		assertUnorderedIDEquals(Sets.newSet("10"), clusters.get(8).get(1).getNodes());

		// 15 and 16 have sequencelength of 8.
		assertUnorderedIDEquals(Sets.newSet("16"), clusters.get(9).get(0).getNodes());
		assertUnorderedIDEquals(Sets.newSet("15"), clusters.get(9).get(1).getNodes());
		// Source node of nested nested bubble is not collapsed.
		assertUnorderedIDEquals(Sets.newSet("11"), clusters.get(9).get(2).getNodes());

		// 12 and 13 are not clustered, because 13 has sequencelength of 12.
		assertUnorderedIDEquals(Sets.newSet("12", "13"), clusters.get(10).get(0).getNodes());

		// 14, 17, and 19 are sink nodes, so the'yre not clustered.
		assertUnorderedIDEquals(Sets.newSet("14"), clusters.get(11).get(0).getNodes());
		assertUnorderedIDEquals(Sets.newSet("17"), clusters.get(12).get(0).getNodes());
		assertUnorderedIDEquals(Sets.newSet("19"), clusters.get(13).get(0).getNodes());
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Test the entire graph.
	 */
	@Test
	public void testEntireGraph() {
		// Set the interestingness strategy to return the sequence length.
		db.setInterestingnessStrategy(container -> container.getScore(Scores.SEQ_LENGTH));
		// CHECKSTYLE.OFF: MagicNumber
		Map<Integer, List<Cluster>> clusters = db.getAllClusters(0, 13, 11);
		System.out.println(clusters);
		assertProperClustering(clusters, 21);
		testSingleNestedBubbleNodes(clusters);
		testMultipleNestedBubbleNodes(clusters);
		// CHECKSTYLE.ON: MagicNumber
	}

	private void assertProperClustering(Map<Integer, List<Cluster>> clustering, int numNodes) {
		// Assert that all elements occur only once, no duplicates.
		assertTrue(getAllNodes(clustering).count() == getAllNodes(clustering).distinct().count());
		// Assert that no elements are missing.
		assertTrue(getAllNodes(clustering).count() == numNodes);
	}
	/**
	 * Clean up after ourselves.
	 * @throws IOException when the database could not be deleted
	 */
	@AfterClass
	public static void cleanUp() throws IOException {
		db.shutdown();
	}
}
