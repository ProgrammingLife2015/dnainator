package nl.tudelft.dnainator;

import nl.tudelft.dnainator.graph.TreeGraphTest;
import nl.tudelft.dnainator.graph.TreeLayoutTest;
import nl.tudelft.dnainator.graph.TreeGeneratorTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * Run all test classes at once.
 */
@RunWith(Suite.class)
@SuiteClasses({
	TreeGraphTest.class,
	TreeLayoutTest.class,
	TreeGeneratorTest.class
})
public class AllTests {

}
