package nl.tudelft.dnainator.graph;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;

import org.graphstream.algorithm.generator.RandomGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test construction and generation of the graph.
 * This class will hold more tests later.
 */
@RunWith(MockitoJUnitRunner.class)
public class TreeGraphTest {
	@Mock private RandomGenerator gen;
	
	/**
	 * Test the constructor.
	 */
	@Test
	public void testConstructor() {
		TreeGraph g = new TreeGraph("Test", gen);
		assertEquals("Test", g.getId());
	}
	
	/**
	 * Test the default constructor.
	 */
	@Test
	public void testDefaultConstructor() {
		TreeGraph g = new TreeGraph();
		assertEquals("Tree", g.getId());
	}
	
	/**
	 * Test the calls to the generator.
	 * We expect at least a call to begin, nextEvents, and end.
	 */
	@Test
	public void testGenBegin() {
		new TreeGraph("Test", gen);
		Mockito.verify(gen, times(1)).begin();
		Mockito.verify(gen, atLeastOnce()).nextEvents();
		Mockito.verify(gen, times(1)).end();
	}
}
