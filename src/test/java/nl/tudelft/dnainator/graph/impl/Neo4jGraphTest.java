package nl.tudelft.dnainator.graph.impl;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Edge;
import nl.tudelft.dnainator.core.impl.SequenceNodeFactoryImpl;
import nl.tudelft.dnainator.core.impl.SequenceNodeImpl;
import nl.tudelft.dnainator.graph.query.GraphQueryDescription;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.exceptions.InvalidEdgeFormatException;
import nl.tudelft.dnainator.parser.exceptions.ParseException;
import nl.tudelft.dnainator.parser.impl.EdgeParserImpl;
import nl.tudelft.dnainator.parser.impl.NodeParserImpl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * Test Neo4j graph implementation.
 */
public class Neo4jGraphTest {
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
				= new File(Neo4jGraphTest.class.getResource("/strains/topo.node.graph").toURI());
			edgeFile
				= new File(Neo4jGraphTest.class.getResource("/strains/topo.edge.graph").toURI());
			//nodeFile = new File("10_strains_graph/simple_graph.node.graph");
			//edgeFile = new File("10_strains_graph/simple_graph.edge.graph");
			NodeParser np = new NodeParserImpl(new SequenceNodeFactoryImpl(),
					new BufferedReader(new FileReader(nodeFile)));
			EdgeParser ep = new EdgeParserImpl(new BufferedReader(new FileReader(edgeFile)));
			db.constructGraph(np, ep);
		} catch (IOException e) {
			fail("Couldn't initialize DB");
		} catch (ParseException e) {
			fail("Couldn't parse file: " + e.getMessage());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test looking up a single node.
	 */
	@Test
	public void testNodeLookup() {
		// CHECKSTYLE.OFF: MagicNumber
		SequenceNode node1 = new SequenceNodeImpl("2", "ASDF", 1, 5, "TATA");
		SequenceNode node2 = new SequenceNodeImpl("3", "ASDF", 5, 9, "TATA");
		SequenceNode node3 = new SequenceNodeImpl("5", "ASDF", 4, 8, "TATA");
		assertEquals(node1, db.getNode("2"));
		assertEquals(node2, db.getNode("3"));
		assertEquals(node3, db.getNode("5"));
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Test looking up one of the roots of the tree.
	 */
	@Test
	public void testRootLookup() {
		// CHECKSTYLE.OFF: MagicNumber
		SequenceNode root = new SequenceNodeImpl("5", "ASDF", 4, 8, "TATA");
		assertEquals(root, db.getRootNode());
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Unit-test the topological ordering.
	 */
	@Test
	public void testTopologicalOrder() {
		LinkedList<Integer> order = new LinkedList<>();
		try {
			EdgeParser ep = new EdgeParserImpl(new BufferedReader(new FileReader(edgeFile)));

			try (Transaction tx = db.getService().beginTx()) {
				for (Node n : db.topologicalOrder()) {
					order.add(Integer.parseInt((String) n.getProperty("id")));
				}
			}
			while (ep.hasNext()) {
				Edge<String> next = ep.next();
				int source = Integer.parseInt(next.getSource());
				int dest = Integer.parseInt(next.getDest());
				assertThat(order.indexOf(source), lessThan(order.indexOf(dest)));
			}
		} catch (NumberFormatException e) {
			fail("Number format incorrect " + e.getMessage());
			e.printStackTrace();
		} catch (InvalidEdgeFormatException e) {
			fail("Edge format incorrect " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			fail("Error during I/O " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Tests the rank attributes for correctness.
	 */
	@Test
	public void testRanks() {
		Set<String> rank0Expect = new HashSet<>();
		Collections.addAll(rank0Expect, "7", "5", "3");
		Set<String> rank0Actual = new HashSet<>();
		db.getRank(0).forEach(e -> rank0Actual.add(e.getId()));
		assertEquals(rank0Expect, rank0Actual);

		Set<String> rank1Expect = new HashSet<>();
		Collections.addAll(rank1Expect, "11", "8");
		Set<String> rank1Actual = new HashSet<>();
		db.getRank(1).forEach(e -> rank1Actual.add(e.getId()));
		assertEquals(rank1Expect, rank1Actual);

		Set<String> rank2Expect = new HashSet<>();
		Collections.addAll(rank2Expect, "2", "9", "10");
		Set<String> rank2Actual = new HashSet<>();
		db.getRank(2).forEach(e -> rank2Actual.add(e.getId()));
		assertEquals(rank2Expect, rank2Actual);
	}

	/**
	 * Test querying of ranks.
	 */
	@Test
	public void testQueryRanks() {
		// Query for ranks from 0 up to but not including rank 2.
		GraphQueryDescription qd = new GraphQueryDescription()
			.fromRank(0)
			.toRank(2);
		Set<String> expect = new HashSet<>();
		Collections.addAll(expect, "7", "5", "3", "11", "8");
		Set<String> actual = new HashSet<>();
		db.queryNodes(qd).forEach(e -> actual.add(e.getId()));
		assertEquals(expect, actual);
	}

	/**
	 * Test querying of IDs.
	 */
	@Test
	public void testQueryIds() {
		GraphQueryDescription qd = new GraphQueryDescription()
			.hasId("2");
		Set<String> expect = new HashSet<>();
		Collections.addAll(expect, "2");
		Set<String> actual = db.queryNodes(qd).stream()
				.map(sn -> sn.getId())
				.collect(Collectors.toSet());
		assertEquals(expect, actual);

		// Also test for multiple ids (reusing the old one)
		qd = qd.hasId("3");
		Collections.addAll(expect, "3");
		actual = db.queryNodes(qd).stream()
				.map(sn -> sn.getId())
				.collect(Collectors.toSet());
		assertEquals(expect, actual);

		// Search for non-existent id.
		qd = new GraphQueryDescription()
			.hasId("42");
		expect = new HashSet<>(); // Empty result.
		actual = db.queryNodes(qd).stream()
				.map(sn -> sn.getId())
				.collect(Collectors.toSet());
		assertEquals(expect, actual);
	}

	/**
	 * Test the filtering functionality.
	 */
	@Test
	public void testQueryFilter() {
		// CHECKSTYLE.OFF: MagicNumber
		GraphQueryDescription qd = new GraphQueryDescription()
			.filter((sn) -> Integer.parseInt(sn.getId()) > 8);
		// CHECKSTYLE.ON: MagicNumber
		Set<String> expect = new HashSet<>();
		Collections.addAll(expect, "9", "10", "11");
		Set<String> actual = db.queryNodes(qd).stream()
				.map(sn -> sn.getId())
				.collect(Collectors.toSet());
		assertEquals(expect, actual);
	}

	/**
	 * Test querying the source string.
	 */
	@Test
	public void testQuerySources() {
		GraphQueryDescription qd = new GraphQueryDescription()
			.containsSource("ASDF");
		Set<String> expect = new HashSet<>();
		Collections.addAll(expect, "2", "5", "3", "7", "8");
		Set<String> actual = db.queryNodes(qd).stream()
				.map(sn -> sn.getId())
				.collect(Collectors.toSet());
		assertEquals(expect, actual);

		// Also test for multiple sources (reusing the old one)
		qd = qd.containsSource("ASD");
		Collections.addAll(expect, "9", "10", "11");
		actual = db.queryNodes(qd).stream()
				.map(sn -> sn.getId())
				.collect(Collectors.toSet());
		assertEquals(expect, actual);

		// Search non-existing source.
		qd = new GraphQueryDescription()
			.containsSource("FDSA");
		// Expect an empty result
		expect = new HashSet<>();
		actual = db.queryNodes(qd).stream()
				.map(sn -> sn.getId())
				.collect(Collectors.toSet());
		assertEquals(expect, actual);
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
