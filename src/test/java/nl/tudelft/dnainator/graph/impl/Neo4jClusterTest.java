package nl.tudelft.dnainator.graph.impl;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;

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
			db = Neo4jSingleton.getInstance().getDatabase(DB_PATH);
			nodeFile
				= new File(Neo4jGraphTest.class.getResource("/strains/cluster.node.graph").toURI());
			edgeFile
				= new File(Neo4jGraphTest.class.getResource("/strains/cluster.edge.graph").toURI());
//			nodeFile = new File("10_strains_graph/simple_graph.node.graph");
//			edgeFile = new File("10_strains_graph/simple_graph.edge.graph");
			NodeParser np = new NodeParserImpl(new SequenceNodeFactoryImpl(),
					new BufferedReader(new FileReader(nodeFile)));
			EdgeParser ep = new EdgeParserImpl(new BufferedReader(new FileReader(edgeFile)));
			db.constructGraph(np, ep);
		} catch (IOException | URISyntaxException | ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() {
		Set<String> expected;
		
		// CHECKSTYLE.OFF: MagicNumber
		expected = Sets.newSet("1", "2", "3");
		assertEquals(expected, db.getCluster("1", 10).stream().collect(Collectors.toSet()));
		
		expected = Sets.newSet("4", "5", "6", "7");
		assertEquals(expected, db.getCluster("4", 10).stream().collect(Collectors.toSet()));
		
		expected = Sets.newSet("4", "5", "6", "7", "8");
		assertEquals(expected, db.getCluster("4", 12).stream().collect(Collectors.toSet()));
		
		expected = Sets.newSet("1", "2", "3", "4", "5", "6", "7", "8");
		assertEquals(expected, db.getCluster("1", 12).stream().collect(Collectors.toSet()));
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Clean up after ourselves.
	 * @throws IOException when the database could not be deleted
	 */
	@AfterClass
	public static void cleanUp() throws IOException {
		Neo4jSingleton.getInstance().stopDatabase(DB_PATH);
	}

}
