package nl.tudelft.dnainator.graph.impl;

import nl.tudelft.dnainator.annotation.impl.AnnotationCollectionImpl;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static nl.tudelft.dnainator.graph.impl.Neo4jTestUtils.assertUnorderedIDEquals;

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

	/**
	 * Test returning various clusters from the sample graph.
	 */
	@Test
	public void test() {
		// CHECKSTYLE.OFF: MagicNumber
		Map<Integer, List<Cluster>> clusters = db.getAllClusters(0, Integer.MAX_VALUE, 11);

		// first bubble is not clustered because one node has length greater than 11.
		assertUnorderedIDEquals(Sets.newSet("1"), clusters.get(0).get(0).getNodes());

		// 2 and 3 Expected on rank 1
		assertUnorderedIDEquals(Sets.newSet("2"), clusters.get(1).get(1).getNodes());
		assertUnorderedIDEquals(Sets.newSet("3"), clusters.get(1).get(0).getNodes());

		// Source node is not collapsed.
		assertUnorderedIDEquals(Sets.newSet("4"), clusters.get(2).get(0).getNodes());

		// Collapsed bubble.
		assertUnorderedIDEquals(Sets.newSet("5", "6", "7"), clusters.get(3).get(0).getNodes());

		// Sink node is not collapsed.
		assertUnorderedIDEquals(Sets.newSet("8"), clusters.get(5).get(0).getNodes());
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
