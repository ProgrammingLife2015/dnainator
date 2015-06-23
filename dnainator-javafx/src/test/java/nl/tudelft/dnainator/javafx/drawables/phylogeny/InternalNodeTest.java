package nl.tudelft.dnainator.javafx.drawables.phylogeny;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

public class InternalNodeTest extends ApplicationTest {

	private InternalNode in;
	private InternalNode spy;
	
	@Override
	public void start(Stage stage) throws Exception {
	}
	
	/**
	 * Creates new database service.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		in = new InternalNode(new ArrayList<>());
		spy = Mockito.spy(in);

	}
	
	/**
	 * Test the creation of a {@link CollapsedNode}.
	 */
	@Test
	public void testCreate() {
		assertEquals(1, in.getChildren().size());
	}
	
	/**
	 * Test whether something was done with the positioning of an {@link InternalNode}. 
	 */
	@Test
	public void testChildPositioning() {
		ArrayList<AbstractNode> nodes = new ArrayList<>();
		InternalNode child = new InternalNode(new ArrayList<>());
		InternalNode spy = Mockito.spy(child);
		nodes.add(spy);
		
		new InternalNode(nodes);
		Mockito.verify(spy, Mockito.atLeastOnce()).marginProperty();
	}
	
	/**
	 * Test what happens when a {@link CollapsedNode} is clicked with the mouse.
	 */
	@Test
	public void testOnMouseClicked() {
		spy.onMouseClicked();
		Mockito.verify(spy, Mockito.atLeastOnce()).getChildren();
	}
}
