package nl.tudelft.dnainator.ui.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import nl.tudelft.dnainator.graph.Graph;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Test the GraphItem of the model.
 * Also test its abstract base classes.
 */
@RunWith(MockitoJUnitRunner.class)
public class GraphItemTest {
	@Mock private Graph graph;
	@Mock private Node node;
	private GraphItem gi;

	/**
	 * Initialize the graphitem.
	 */
	@Before
	public void setup() {
		gi = new GraphItem(graph);
	}

	/**
	 * Test construction of a graphitem.
	 * Should not query the model for anything yet.
	 */
	@Test
	public void testConstruction() {
		assertEquals(graph, gi.getGraph());

		verify(graph, never()).getRank(anyInt());
		verify(graph, never()).getRanks();
	}

	/**
	 * Test setting the content.
	 */
	@Test
	public void testSetContent() {
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(4, gi.getContent().getChildren().size());
		gi.getContent().getChildren().add(new GraphItem(graph));
		assertEquals(5, gi.getContent().getChildren().size());
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Test whether this item is in a specified viewport.
	 * The drawable we're testing currently extends to x = 40000, y = 20.
	 */
	@Test
	public void testInViewPort() {
		// CHECKSTYLE.OFF: MagicNumber
		Bounds viewport = new Rectangle(640, 480).getBoundsInLocal();
		assertEquals(true, gi.isInViewport(viewport));

		gi.getContent().setTranslateX(0);
		gi.getContent().setTranslateY(-1);
		assertEquals(false, gi.isInViewport(viewport));

		gi.getContent().setTranslateX(0);
		gi.getContent().setTranslateY(480);
		assertEquals(true, gi.isInViewport(viewport));

		gi.getContent().setTranslateX(0);
		gi.getContent().setTranslateY(481);
		assertEquals(false, gi.isInViewport(viewport));

		gi.getContent().setTranslateX(640);
		gi.getContent().setTranslateY(0);
		assertEquals(true, gi.isInViewport(viewport));

		gi.getContent().setTranslateX(641);
		gi.getContent().setTranslateY(0);
		assertEquals(false, gi.isInViewport(viewport));
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Toggle visibility of this item.
	 */
	@Test
	public void testToggle() {
		// CHECKSTYLE.OFF: MagicNumber
		// TODO
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Test whether this item is updated correctly, given a specified viewport.
	 * The drawable we're testing currently extends to x = 40000, y = 20.
	 */
	@Test
	public void testUpdate() {
		// CHECKSTYLE.OFF: MagicNumber
		gi.update(new Rectangle(20000, 10000).getBoundsInLocal());
		assertTrue(gi.getContent().isVisible());
		verify(graph, never()).getRank(anyInt());

		gi.update(new Rectangle(5000, 2500).getBoundsInLocal());
		assertFalse(gi.getContent().isVisible());
		verify(graph, never()).getRank(anyInt());

		gi.update(new Rectangle(1000, 500).getBoundsInLocal());
		assertFalse(gi.getContent().isVisible());
		verify(graph, Mockito.atLeastOnce()).getRank(anyInt());

		gi.update(new Rectangle(20000, 10000).getBoundsInLocal());
		assertTrue(gi.getContent().isVisible());
		verify(graph, Mockito.atLeastOnce()).getRank(anyInt());
		// CHECKSTYLE.ON: MagicNumber
	}
}
