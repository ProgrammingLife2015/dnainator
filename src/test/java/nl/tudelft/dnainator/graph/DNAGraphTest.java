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
public class DNAGraphTest {
	@Mock private RandomGenerator gen;
	
	/**
	 * Test the constructor.
	 */
	@Test
	public void testConstructor() {
		DNAGraph g = new DNAGraph("Test", gen);
		assertEquals("Test", g.getId());
	}
	
	/**
	 * Test the default constructor.
	 */
	@Test
	public void testDefaultConstructor() {
		DNAGraph g = new DNAGraph();
		assertEquals("Tree", g.getId());
	}
	
	/**
	 * Test the calls to the generator.
	 * We expect at least a call to begin, nextEvents, and end.
	 */
	@Test
	public void testGenBegin() {
		new DNAGraph("Test", gen);
		Mockito.verify(gen, times(1)).begin();
		Mockito.verify(gen, atLeastOnce()).nextEvents();
		Mockito.verify(gen, times(1)).end();
	}
}
