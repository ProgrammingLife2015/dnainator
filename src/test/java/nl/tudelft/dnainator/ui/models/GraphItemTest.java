package nl.tudelft.dnainator.ui.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
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
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(0, gi.getLocalToRoot().getTx(), .001);
		assertEquals(0, gi.getLocalToRoot().getTy(), .001);
		// CHECKSTYLE.ON: MagicNumber
		assertEquals(graph, gi.getGraph());

		verify(graph, never()).getRank(anyInt());
		verify(graph, never()).getRanks();
	}

	/**
	 * Test setting the root transform.
	 */
	@Test
	public void testSetRoot() {
		// CHECKSTYLE.OFF: MagicNumber
		gi.setLocalToRoot(new Translate(5, 10));
		assertEquals(5, gi.getLocalToRoot().getTx(), .001);
		assertEquals(10, gi.getLocalToRoot().getTy(), .001);
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Test binding the root transform.
	 */
	@Test
	public void testBindRoot() {
		ObjectProperty<Transform> parent = new SimpleObjectProperty<>(new Translate());
		gi.bindLocalToRoot(parent);

		// CHECKSTYLE.OFF: MagicNumber
		parent.set(new Translate(5, 5));
		assertEquals(5, gi.getLocalToRoot().getTx(), .001);
		assertEquals(5, gi.getLocalToRoot().getTy(), .001);
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Test setting the content.
	 */
	@Test
	public void testSetContent() {
		assertEquals(1, gi.getContent().getChildren().size());
		gi.getContent().getChildren().add(new ClusterItem(graph, gi.localToRootProperty()));
		assertEquals(2, gi.getContent().getChildren().size());
	}

	/**
	 * Test whether the localToRoot property updates correctly when binding.
	 */
	@Test
	public void testLocalToRoot() {
		ObjectProperty<Transform> parent = new SimpleObjectProperty<>(new Translate());
		gi.bindLocalToRoot(parent);

		Bounds local = new Rectangle().getBoundsInLocal();
		assertEquals(new Rectangle().getBoundsInLocal(), gi.localToRoot(local));

		// CHECKSTYLE.OFF: MagicNumber
		parent.set(new Translate(10, 10));
		assertEquals(new Rectangle(10, 10, 0, 0).getBoundsInLocal(), gi.localToRoot(local));
		assertEquals(new Point2D(10, 10), gi.localToRoot(new Point2D(0, 0)));

		parent.set(new Translate(100, 10));
		assertEquals(new Rectangle(100, 10, 0, 0).getBoundsInLocal(), gi.localToRoot(local));
		assertEquals(new Point2D(100, 10), gi.localToRoot(new Point2D(0, 0)));
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Test whether this item is in a specified viewport.
	 * The drawable we're testing currently extends to x = 40000, y = 20.
	 */
	@Test
	public void testInViewPort() {
		ObjectProperty<Transform> parent = new SimpleObjectProperty<>(new Translate());
		gi.bindLocalToRoot(parent);

		// CHECKSTYLE.OFF: MagicNumber
		Bounds viewport = new Rectangle(640, 480).getBoundsInLocal();
		assertEquals(true, gi.isInViewport(viewport));

		parent.set(new Translate(0, -20));
		assertEquals(true, gi.isInViewport(viewport));

		parent.set(new Translate(0, -21));
		assertEquals(false, gi.isInViewport(viewport));

		parent.set(new Translate(640, 0));
		assertEquals(true, gi.isInViewport(viewport));

		parent.set(new Translate(641, 0));
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
