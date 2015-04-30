package nl.tudelft.dnainator.graph;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;

import org.graphstream.stream.Sink;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test generation of a graph representing a binary tree.
 */
@RunWith(MockitoJUnitRunner.class)
public class TreeGeneratorTest {
	@Mock private Sink sink;
	private TreeGenerator g;
	
	/**
	 * Generic initialization.
	 */
	@Before
	public void setup() {
		g = new TreeGenerator();
		g.addSink(sink);
	}
	
	/**
	 * Test the begin call of a generator.
	 */
	@Test
	public void testBegin() {
		g.begin();
		Mockito.verify(sink, atLeastOnce()).nodeAdded(any(), anyLong(), any());
	}
	
	/**
	 * Test 2 consecutive next calls of a generator.
	 */
	@Test
	public void testNext() {
		// CHECKSTYLE.OFF: MagicNumber
		g.begin();
		g.nextEvents();
		Mockito.verify(sink, times(2)).nodeAdded(any(), anyLong(), any());
		Mockito.verify(sink, times(1)).edgeAdded(any(), anyLong(), any(),
												 any(), any(), anyBoolean());
		
		g.nextEvents();
		Mockito.verify(sink, times(3)).nodeAdded(any(), anyLong(), any());
		Mockito.verify(sink, times(2)).edgeAdded(any(), anyLong(), any(),
												 any(), any(), anyBoolean());
		// CHECKSTYLE.ON: MagicNumber
	}

}
