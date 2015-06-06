package nl.tudelft.dnainator.graph.impl;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.core.impl.SequenceNodeFactoryImpl;
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

/**
 * Test clustering in a DNA sequence graph.
 */
public class Neo4jClusterTest {
	private static final String DB_PATH = "target/neo4j-junit";
	private static Neo4jGraph db;
	private static File nodeFile;
	private static File edgeFile;

	/**
	 * Setup the database and construct the graph.
	 */
	@BeforeClass
	public static void setUp() {
		try {
			FileUtils.deleteRecursively(new File(DB_PATH));
			nodeFile
				= new File(Neo4jGraphTest.class.getResource("/strains/cluster.node.graph").toURI());
			edgeFile
				= new File(Neo4jGraphTest.class.getResource("/strains/cluster.edge.graph").toURI());
//			nodeFile = new File("10_strains_graph/simple_graph.node.graph");
//			edgeFile = new File("10_strains_graph/simple_graph.edge.graph");
			NodeParser np = new NodeParserImpl(new SequenceNodeFactoryImpl(),
					new BufferedReader(new FileReader(nodeFile)));
			EdgeParser ep = new EdgeParserImpl(new BufferedReader(new FileReader(edgeFile)));
			new Neo4jBatchBuilder(DB_PATH).constructGraph(np, ep);
			db = new Neo4jGraph(DB_PATH);
		} catch (IOException | URISyntaxException | ParseException e) {
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
