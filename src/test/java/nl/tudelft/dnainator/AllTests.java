package nl.tudelft.dnainator;

import nl.tudelft.dnainator.graph.DNAGraphTest;
import nl.tudelft.dnainator.graph.DNALayoutTest;
import nl.tudelft.dnainator.graph.TreeGeneratorTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * Run all test classes at once.
 */
@RunWith(Suite.class)
@SuiteClasses({
	DNAGraphTest.class,
	DNALayoutTest.class,
	TreeGeneratorTest.class
})
public class AllTests {

}
