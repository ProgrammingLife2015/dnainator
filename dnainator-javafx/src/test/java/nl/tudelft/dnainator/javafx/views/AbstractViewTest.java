package nl.tudelft.dnainator.javafx.views;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.stage.Stage;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorMap;
import nl.tudelft.dnainator.javafx.widgets.Propertyable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

/**
 * This class tests the implementation of the {@link AbstractView}.
 * All views are extended from this abstract implementation.
 */
public class AbstractViewTest extends ApplicationTest {

	private AbstractView abstractView;
	@Mock private Graph graph;
	@Mock private ColorMap colorMap;
	@Mock private Group group;
	@Mock private Propertyable p;
	
	@Override
	public void start(Stage stage) throws Exception {
	}
	
	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		// CHECKSTYLE.OFF: MagicNumber
		Mockito.when(graph.getMaxBasePairs()).thenReturn(0);
		// CHECKSTYLE.ON: MagicNumber
		abstractView = new StrainView(colorMap, graph);
	}
	
	/**
	 * Tests the default set scale.
	 */
	@Test
	public void testGetScale() {
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(0.5, abstractView.getScale().getMxx(), 0.001);
		assertEquals(0.5, abstractView.getScale().getMyy(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Check whether three transforms were added for zooming, panning.
	 */
	@Test
	public void testSetTransforms() {
		abstractView.setTransforms(group);
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(3, group.getTransforms().size());
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Check whether panning changes the translation correctly.
	 */
	@Test
	public void testPan() {
		// CHECKSTYLE.OFF: MagicNumber
		abstractView.pan(new Point2D(1.0, 2.0));
		assertEquals(1.0, abstractView.translate.getX(), 0.001);
		assertEquals(2.0, abstractView.translate.getY(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Tests whether zooming in makes the scale larger.
	 */
	@Test
	public void testZoomIn() {
		double zoomPrevious = abstractView.scale.getMxx();
		// CHECKSTYLE.OFF: MagicNumber
		abstractView.zoomInScroll(1.0, 2.0);
		// CHECKSTYLE.ON: MagicNumber
		assertTrue(abstractView.scale.getMxx() > zoomPrevious);
		assertTrue(abstractView.scale.getMyy() > zoomPrevious);

		zoomPrevious = abstractView.scale.getMxx();
		abstractView.zoomIn();
		assertTrue(abstractView.scale.getMxx() > zoomPrevious);
		assertTrue(abstractView.scale.getMyy() > zoomPrevious);
	}
	
	/**
	 * Tests whether zooming out makes the scale smaller.
	 */
	@Test
	public void testZoomOut() {
		double zoomPrevious = abstractView.scale.getMxx();
		// CHECKSTYLE.OFF: MagicNumber
		abstractView.zoomOutScroll(1.0, 2.0);
		// CHECKSTYLE.ON: MagicNumber
		assertTrue(abstractView.scale.getMxx() < zoomPrevious);
		assertTrue(abstractView.scale.getMyy() < zoomPrevious);

		zoomPrevious = abstractView.scale.getMxx();
		abstractView.zoomOut();
		assertTrue(abstractView.scale.getMxx() < zoomPrevious);
		assertTrue(abstractView.scale.getMyy() < zoomPrevious);
	}
	
	/**
	 * Check wheter resetting the panning puts the translation back to 0.
	 */
	@Test
	public void resetPan() {
		// CHECKSTYLE.OFF: MagicNumber
		abstractView.pan(new Point2D(1.0, 2.0));
		assertEquals(1.0, abstractView.translate.getX(), 0.001);
		assertEquals(2.0, abstractView.translate.getY(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
		
		abstractView.resetTranslate();
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(0, abstractView.translate.getX(), 0.001);
		assertEquals(0, abstractView.translate.getY(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Check wheter resetting the zoom puts the scaling back to its original.
	 */
	@Test
	public void resetZoom() {
		double zoomPrevious = abstractView.scale.getMxx();
		abstractView.zoomIn();
		assertTrue(abstractView.scale.getMxx() > zoomPrevious);
		
		abstractView.resetZoom();
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(0, abstractView.scale.getTx(), 0.001);
		assertEquals(0, abstractView.scale.getTy(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test the last clicked {@link Propertyable}.
	 */
	@Test
	public void testLastClicked() {
		AbstractView.setLastClicked(p);
		assertEquals(p, AbstractView.getLastClicked());
		assertNotNull(AbstractView.lastClickedProperty());
	}
}
