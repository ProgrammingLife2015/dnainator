package nl.tudelft.dnainator.graph.impl;

import nl.tudelft.dnainator.annotation.impl.AnnotationCollectionImpl;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

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
//			nodeFile = new File("10_strains_graph/simple_graph.node.graph");
//			edgeFile = new File("10_strains_graph/simple_graph.edge.graph");
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

	/**
	 * Test returning various clusters from the sample graph.
	 */
	@Test
	public void test() {
		Set<String> expected;

		List<String> start = Arrays.asList("1");
		// Set the interestingness strategy to return the sequence length.
		db.setInterestingnessStrategy(container -> container.getScore(Scores.SEQ_LENGTH));
		// CHECKSTYLE.OFF: MagicNumber
		Map<Integer, List<Cluster>> clusters = db.getAllClusters(start, Integer.MAX_VALUE, 11);
		expected = Sets.newSet("1", "3", "4", "5", "6", "7");
		assertEquals(expected, clusters.get(0).get(0).getNodes()
				.stream()
				.map(sn -> sn.getId())
				.collect(Collectors.toSet()));
		// 2 Expected on rank 1
		expected = Sets.newSet("2");
		assertEquals(expected, clusters.get(1).get(0).getNodes()
				.stream()
				.map(sn -> sn.getId())
				.collect(Collectors.toSet()));
		// 8 Expected on rank 5
		expected = Sets.newSet("8");
		assertEquals(expected, clusters.get(5).get(0).getNodes()
				.stream()
				.map(sn -> sn.getId())
				.collect(Collectors.toSet()));
		// CHECKSTYLE.ON: MagicNumber
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
