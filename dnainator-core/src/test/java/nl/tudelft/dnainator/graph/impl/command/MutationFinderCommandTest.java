package nl.tudelft.dnainator.graph.impl.command;

import nl.tudelft.dnainator.annotation.impl.AnnotationCollectionImpl;
import nl.tudelft.dnainator.annotation.impl.AnnotationImpl;
import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.core.impl.SequenceNodeFactoryImpl;
import nl.tudelft.dnainator.graph.impl.Neo4jBatchBuilder;
import nl.tudelft.dnainator.graph.impl.Neo4jGraph;
import nl.tudelft.dnainator.graph.impl.NodeLabels;
import nl.tudelft.dnainator.graph.impl.command.MutationFinderCommand;
import nl.tudelft.dnainator.graph.impl.properties.SequenceProperties;
import nl.tudelft.dnainator.parser.EdgeParser;
import nl.tudelft.dnainator.parser.NodeParser;
import nl.tudelft.dnainator.parser.TreeParser;
import nl.tudelft.dnainator.parser.exceptions.ParseException;
import nl.tudelft.dnainator.parser.impl.EdgeParserImpl;
import nl.tudelft.dnainator.parser.impl.NodeParserImpl;
import nl.tudelft.dnainator.tree.TreeNode;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.io.fs.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MutationFinderCommandTest {
	private static final String DB_PATH = "target/neo4j-tree-junit";
	private static Neo4jGraph db;
	private static InputStream nodeFile;
	private static InputStream edgeFile;
	private static AnnotationImpl first;
	private static AnnotationImpl middle;
	private static AnnotationImpl last;

	/**
	 * Setup the database and construct the graph.
	 * @throws URISyntaxException 
	 */
	@BeforeClass
	public static void setUp() throws URISyntaxException {
		try {
			FileUtils.deleteRecursively(new File(DB_PATH));
			nodeFile = getNodeFile();
			edgeFile = getEdgeFile();
			NodeParser np = new NodeParserImpl(new SequenceNodeFactoryImpl(),
					new BufferedReader(new InputStreamReader(nodeFile, "UTF-8")));
			EdgeParser ep = new EdgeParserImpl(new BufferedReader(
							new InputStreamReader(edgeFile, "UTF-8")));
			TreeNode phylo = new TreeParser(getTreeFile()).parse();
			db = (Neo4jGraph) new Neo4jBatchBuilder(DB_PATH, new AnnotationCollectionImpl(), phylo)
				.constructGraph(np, ep)
				.build();
		} catch (IOException e) {
			fail("Couldn't initialize DB");
		} catch (ParseException e) {
			fail("Couldn't parse file: " + e.getMessage());
		}
		//CHECKSTYLE.OFF: MagicNumber
		first = new AnnotationImpl("first", 0, 10, true);
		middle = new AnnotationImpl("middle", 5, 25, true);
		last = new AnnotationImpl("last", 20, 30, true);
		//CHECKSTYLE.ON: MagicNumber
		db.addAnnotation(first);
		db.addAnnotation(middle);
		db.addAnnotation(last);
	}

	private static InputStream getNodeFile() {
		return MutationFinderCommandTest.class.getResourceAsStream("/strains/advancedtopo.node.graph");
	}

	private static InputStream getEdgeFile() {
		return MutationFinderCommandTest.class.getResourceAsStream("/strains/advancedtopo.edge.graph");
	}

	private static File getTreeFile() throws URISyntaxException {
		return new File(MutationFinderCommandTest.class.getResource("/strains/advancedtopo.nwk")
				.toURI());
	}

	/**
	 * Test returning a source set.
	 * @param expected
	 * @param actual
	 */
	@Test
	public void testIndependentMutations() {
		db.execute(e -> {
			Node node = e.findNode(NodeLabels.NODE, SequenceProperties.ID.name(), "6");
			new MutationFinderCommand(node).execute(e);
		});
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
