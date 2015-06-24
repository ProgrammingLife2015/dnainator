package nl.tudelft.dnainator.javafx.drawables.phylogeny;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

/**
 * Test the {@link AbstractNode} which is an abstraction of all nodes in the phylogenetic tree.
 */
public class AbstractNodeTest extends ApplicationTest {

	private AbstractNode an;
	private ObservableList<Node> nodes;
	
	@Override
	public void start(Stage stage) throws Exception {
	}
	
	/**
	 * Set up mocks and common variables.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		an = Mockito.mock(AbstractNode.class, Mockito.CALLS_REAL_METHODS);
		nodes = FXCollections.observableList(new ArrayList<>());
		Mockito.when(an.getChildren()).thenReturn(nodes);
	}
	
	/**
	 * Test getting the default shape.
	 */
	@Test
	public void testGetShape() {
		Rectangle r = (Rectangle) an.getShape();
		assertNotNull(r);
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(0 - AbstractNode.DIM / 2, r.getX(), 0.001);
		assertEquals(0 - AbstractNode.DIM / 2, r.getY(), 0.001);
		assertEquals(AbstractNode.DIM, r.getHeight(), 0.001);
		assertEquals(AbstractNode.DIM, r.getWidth(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test adding and removing a css style on the children of the {@link AbstractNode}.
	 */
	@Test
	public void testAddAndRemoveStyle() {
		// Add style.
		an.getChildren().add(an.getShape());
		an.addStyle("some style");
		assertFalse(an.getChildren().get(0).getStyleClass().isEmpty());
		
		// Remove the added style.
		an.removeStyle("some style");
		assertTrue(an.getChildren().get(0).getStyleClass().isEmpty());
	}	
}
