package nl.tudelft.dnainator.javafx.views;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import de.saxsys.javafx.test.JfxRunner;

/**
 * This class tests the implementation of the {@link StrainView}.
 * This is the view responsible for everything related to the {@link Graph}.
 * This includes controls used in the {@link StrainView}.
 */
@RunWith(JfxRunner.class)
public class StrainViewTest {
	
	private StrainView strainView;
	@Mock private Graph graph;
	@Mock private ColorServer colorServer;
	@Mock private EnrichedSequenceNode esn;
	
	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		strainView = new StrainView(colorServer, graph);
	}
	
	/**
	 * Test whether three children; strain, minimap and straincontrol, are added.
	 */
	@Test
	public void testCreate() {
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(3, strainView.getChildren().size());
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test jumping to a rank.
	 */
	@Test
	public void testGotoRank() {
		// CHECKSTYLE.OFF: MagicNumber
		Mockito.when(graph.getMaxRank()).thenReturn(5);
		strainView.gotoRank(4);
		
		assertEquals(-4 * 100 * strainView.scale.getMxx(), strainView.translate.getX(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test jumping to a node (given its id).
	 */
	@Test
	public void testGotoNode() {
		Mockito.when(graph.getNode(Mockito.anyString())).thenReturn(esn);
		Mockito.when(esn.getRank()).thenReturn(6);
		strainView.gotoNode("some node");
		
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(-6 * 100 * strainView.scale.getMxx(), strainView.translate.getX(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
		
		Mockito.when(graph.getNode(Mockito.anyString())).thenReturn(null);
		strainView.gotoNode("some node");
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(-6 * 100 * strainView.scale.getMxx(), strainView.translate.getX(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test if toggling the minimap changes its visibility.
	 */
	@Test
	public void testToggleMinimap() {
		assertTrue(strainView.getChildren().get(2).isVisible());
		strainView.toggleMinimap();
		assertFalse(strainView.getChildren().get(2).isVisible());
		strainView.toggleMinimap();
		assertTrue(strainView.getChildren().get(2).isVisible());
	}
	
	/**
	 * Test getting the strain control.
	 */
	@Test
	public void testGetStrainControl() {
		assertNotNull(strainView.getStrainControl());
	}
	
	/**
	 * Test if toggling the strain controls changes its visibility.
	 */
	@Test
	public void testToggleStrainControl() {
		assertTrue(strainView.getChildren().get(1).isVisible());
		strainView.toggleStrainControl();
		assertFalse(strainView.getChildren().get(1).isVisible());
		strainView.toggleStrainControl();
		assertTrue(strainView.getChildren().get(1).isVisible());
	}
}
