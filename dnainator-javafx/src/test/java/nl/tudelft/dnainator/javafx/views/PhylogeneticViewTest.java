package nl.tudelft.dnainator.javafx.views;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import javafx.geometry.Point2D;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.tree.TreeNode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.saxsys.javafx.test.JfxRunner;

/**
 * This class tests the implementation of the {@link PhylogeneticView}.
 * This is the view responsible for everything related to the phylogenetic tree.
 */
@RunWith(JfxRunner.class)
public class PhylogeneticViewTest {

	private PhylogeneticView phyloView;
	private TreeNode tn;
	@Mock private ColorServer colorServer;
	
	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		tn = new TreeNode(null);
		MockitoAnnotations.initMocks(this);
		phyloView = new PhylogeneticView(colorServer, tn);
	}
	
	/**
	 * Tests creating a {@link PhylogeneticView} with a tree containing two children.
	 */
	@Test
	public void testCreate() {
		tn.addChild(new TreeNode(tn));
		phyloView = new PhylogeneticView(colorServer, tn);
		assertEquals(2, tn.getChildren().size());
	}
	
	/**
	 * Test zooming on the {@link PhylogeneticView}.
	 * Zooming out of bounds should not change the zoom.
	 */
	@Test
	public void testZoom() {
		double zoomPrevious = phyloView.scale.getMxx();
		
		// CHECKSTYLE.OFF: MagicNumber
		phyloView.zoom(1.0, new Point2D(1.0, 2.0));
		// CHECKSTYLE.ON: MagicNumber
		
		assertTrue(phyloView.scale.getMxx() > zoomPrevious);
		assertTrue(phyloView.scale.getMyy() > zoomPrevious);
		
		zoomPrevious = phyloView.scale.getMxx();
		// CHECKSTYLE.OFF: MagicNumber
		phyloView.zoom(Double.MAX_VALUE, new Point2D(1.0, 2.0));
		// CHECKSTYLE.ON: MagicNumber
		
		assertEquals(zoomPrevious, phyloView.scale.getMxx(), 0.001);
		assertEquals(zoomPrevious, phyloView.scale.getMyy(), 0.001);
	}
	
	/**
	 * Test the default scale of the {@link PhylogeneticView}.
	 */
	@Test
	public void testGetScale() {
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(0.1, phyloView.getScale().getMxx(), 0.001);
		assertEquals(0.1, phyloView.getScale().getMyy(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}
}
