package nl.tudelft.dnainator.graph;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.Matchers.lessThan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;

import nl.tudelft.dnainator.core.DefaultSequenceFactory;
import nl.tudelft.dnainator.core.Edge;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.buffered.DefaultEdgeParser;
import nl.tudelft.dnainator.parser.buffered.JFASTANodeParser;
import nl.tudelft.dnainator.parser.exceptions.InvalidEdgeFormatException;
import nl.tudelft.dnainator.parser.exceptions.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * Test Neo4j graph implementation.
 */
public class Neo4jGraphTest {
	private Neo4jGraphDatabase db;
	private File nodeFile;
	private File edgeFile;

	/**
	 * Setup the database and construct the graph.
	 */
	@Before
	public void setUp() {
		try {
			db = Neo4jGraphDatabase.getInstance();
			nodeFile = new File(getClass().getResource("/strains/topo.node.graph").toURI());
			edgeFile = new File(getClass().getResource("/strains/topo.edge.graph").toURI());
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
			try (Transaction tx = db.getDB().beginTx()) {
				for (Node n : db.topologicalOrder()) {
					order.add(Integer.parseInt((String) n.getProperty("id")));
				}
			}
			while (ep.hasNext()) {
				Edge<String> next = ep.next();
				int source = Integer.parseInt(next.getSource());
				int dest = Integer.parseInt(next.getDest());
				System.out.println(source + " -> " + dest);
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

}
