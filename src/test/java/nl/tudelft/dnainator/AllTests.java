package nl.tudelft.dnainator;

import nl.tudelft.dnainator.graph.Neo4jGraphTest;
import nl.tudelft.dnainator.graph.impl.Neo4jSingletonTest;
import nl.tudelft.dnainator.parser.HeaderParserTest;
import nl.tudelft.dnainator.parser.buffered.DefaultEdgeParserTest;
import nl.tudelft.dnainator.parser.buffered.JFASTANodeParserTest;
import nl.tudelft.dnainator.ui.services.FileLoadServiceTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * Run all test classes at once.
 */
@RunWith(Suite.class)
@SuiteClasses({
	Neo4jGraphTest.class, Neo4jSingletonTest.class,
	HeaderParserTest.class, DefaultEdgeParserTest.class,
	JFASTANodeParserTest.class, FileLoadServiceTest.class
})
public class AllTests {

}
