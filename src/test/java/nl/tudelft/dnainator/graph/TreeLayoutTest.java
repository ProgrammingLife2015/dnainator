package nl.tudelft.dnainator.graph;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.Sink;
import org.graphstream.stream.sync.SinkTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test the layout algorithm that will calculate our graphstream layout.
 */
@RunWith(MockitoJUnitRunner.class)
public class TreeLayoutTest {
	@Mock private Graph g;
	@Mock private SinkTime t;
	@Mock private Sink sink;
	@Mock private Node node;
	
	private TreeLayout layout;
	
	/**
	 * Generic initialization.
	 */
	@Before
	public void setup() {
		layout = new TreeLayout(g, t);
	}

	/**
	 * Test whether we construct the correct layout.
	 */
	@Test
	public void testConstructor_Layout() {
		assertEquals("TreeLayout", layout.getLayoutAlgorithmName());
	}
	
	/**
	 * Test whether the layout actually updates the Node position after adding.
	 */
	@Test
	public void testSinkNodeAdded() {
		Mockito.when(node.getId()).thenReturn("0");
		Mockito.when(node.getIndex()).thenReturn(0);
		Mockito.when(g.getNode(anyString())).thenReturn(node);
		
		layout.addSink(sink);
		layout.nodeAdded("Test", 1, "Test");
		
		Mockito.verify(sink, atLeastOnce()).nodeAttributeChanged(anyString(), anyLong(),
																 anyString(), anyString(),
																 any(), any());
	}

}
