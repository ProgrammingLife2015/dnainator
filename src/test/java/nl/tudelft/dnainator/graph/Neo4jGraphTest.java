package nl.tudelft.dnainator.graph;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
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

import nl.tudelft.dnainator.core.DefaultSequenceFactory;
import nl.tudelft.dnainator.core.Edge;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.buffered.DefaultEdgeParser;
import nl.tudelft.dnainator.parser.buffered.JFASTANodeParser;
import nl.tudelft.dnainator.parser.exceptions.InvalidEdgeFormatException;
import nl.tudelft.dnainator.parser.exceptions.ParseException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * Test Neo4j graph implementation.
 */
public class Neo4jGraphTest {
	private static final String DB_PATH = "target/neo4j-junit";
	private static Neo4jGraphDatabase db;
	private static File nodeFile;
	private static File edgeFile;

	/**
	 * Setup the database and construct the graph.
	 */
	@BeforeClass
	public static void setUp() {
		try {
			Neo4jGraphDatabase db = Neo4jSingleton.getInstance().getDatabase(DB_PATH);
			nodeFile
				= new File(Neo4jGraphTest.class.getResource("/strains/topo.node.graph").toURI());
			edgeFile
				= new File(Neo4jGraphTest.class.getResource("/strains/topo.edge.graph").toURI());
			//nodeFile = new File("10_strains_graph/simple_graph.node.graph");
			//edgeFile = new File("10_strains_graph/simple_graph.edge.graph");
			NodeParser np = new JFASTANodeParser(new DefaultSequenceFactory(),
					new BufferedReader(new FileReader(nodeFile)));
			EdgeParser ep = new DefaultEdgeParser(new BufferedReader(new FileReader(edgeFile)));
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
	 * Unit-test the topological ordering.
	 */
	@Test
	public void testTopologicalOrder() {
		LinkedList<Integer> order = new LinkedList<>();
		try {
			EdgeParser ep = new DefaultEdgeParser(new BufferedReader(new FileReader(edgeFile)));

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
}
