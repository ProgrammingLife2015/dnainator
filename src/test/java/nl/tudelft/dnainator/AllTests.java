package nl.tudelft.dnainator;

import nl.tudelft.dnainator.graph.impl.Neo4jGraphTest;
import nl.tudelft.dnainator.graph.impl.Neo4jSingletonTest;
import nl.tudelft.dnainator.parser.HeaderParserTest;
import nl.tudelft.dnainator.parser.buffered.EdgeParserTest;
import nl.tudelft.dnainator.parser.buffered.NodeParserTest;
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
	HeaderParserTest.class, EdgeParserTest.class,
	NodeParserTest.class, FileLoadServiceTest.class
})
public class AllTests {

}
