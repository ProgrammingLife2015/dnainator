package nl.tudelft.dnainator.graph;

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
	 * Test the call to gen.begin().
	 */
	@Test
	public void testConstructor_GenBegin() {
		new DNAGraph("Test", gen);
		Mockito.verify(gen, times(1)).begin();
		Mockito.verify(gen, atLeastOnce()).nextEvents();
		Mockito.verify(gen, times(1)).end();
	}
}
