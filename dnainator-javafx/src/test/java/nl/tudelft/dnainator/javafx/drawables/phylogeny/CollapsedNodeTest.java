package nl.tudelft.dnainator.javafx.drawables.phylogeny;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

public class CollapsedNodeTest extends ApplicationTest {

	private CollapsedNode cn;
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
		cn = new CollapsedNode(spy);
	}
	
	/**
	 * Test the creation of a {@link CollapsedNode}.
	 */
	@Test
	public void testCreate() {
		assertEquals(2, cn.getChildren().size());
	}
	
	/**
	 * Test getting the default shape.
	 */
	@Test
	public void testGetShape() {
		Circle c = (Circle) cn.getShape();
		assertNotNull(c);
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(0, c.getCenterX(), 0.001);
		assertEquals(0, c.getCenterY(), 0.001);
		assertEquals(AbstractNode.DIM / 2, c.getRadius(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test what happens when a {@link CollapsedNode} is clicked with the mouse.
	 */
	@Test
	public void testOnMouseClicked() {
		cn.onMouseClicked();
		Mockito.verify(spy, Mockito.atLeastOnce()).getChildren();
		Mockito.verify(spy, Mockito.atLeastOnce()).bindMargins();
	}
}
