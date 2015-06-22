package nl.tudelft.dnainator.javafx.widgets.animations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import javafx.scene.layout.Pane;
import nl.tudelft.dnainator.javafx.widgets.animations.TransitionAnimation.Position;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import de.saxsys.javafx.test.JfxRunner;

/**
 * This class tests the implementation of the {@link DownSlideAnimation}.
 * This animation causes a {@link Pane} to slide in a downward direction.
 */
@RunWith(JfxRunner.class)
public class DownSlideAnimationTest {

	private Pane pane;
	private DownSlideAnimation dsa;
	
	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		pane = new Pane();
		// CHECKSTYLE.OFF: MagicNumber
		dsa = new DownSlideAnimation(pane, 100.0, 1.0, Position.TOP);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test setting the size of the pane.
	 */
	@Test
	public void testSetCurSize() {
		// CHECKSTYLE.OFF: MagicNumber
		dsa.setCurSize(0.2);
		assertEquals(dsa.newSize, dsa.size * 0.2, 0.001);
		
		dsa = new DownSlideAnimation(pane, 100.0, 1.0, Position.BOTTOM);
		dsa.setCurSize(0.2);
		assertEquals(dsa.newSize, dsa.size * (1.0 - 0.2), 0.001);
		
		dsa = new DownSlideAnimation(pane, 100.0, 1.0, Position.RIGHT);
		dsa.setCurSize(0.2);
		assertEquals(dsa.newSize, dsa.size, 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test setting visibility of the pane.
	 */
	@Test
	public void testVisibility() {
		// CHECKSTYLE.OFF: MagicNumber
		pane.setVisible(true);
		dsa.setRate(-0.5);
		dsa.setVisibility(pane, Position.TOP);
		assertFalse(pane.isVisible());
		
		pane.setVisible(true);
		dsa.setRate(0.5);
		dsa.setVisibility(pane, Position.TOP);
		assertTrue(pane.isVisible());
	
		pane.setVisible(true);
		dsa.setRate(-0.5);
		dsa.setVisibility(pane, Position.BOTTOM);
		assertTrue(pane.isVisible());
		
		pane.setVisible(true);
		dsa.setRate(0.5);
		dsa.setVisibility(pane, Position.BOTTOM);
		assertFalse(pane.isVisible());
		// CHECKSTYLE.ON: MagicNumber
	}
}
