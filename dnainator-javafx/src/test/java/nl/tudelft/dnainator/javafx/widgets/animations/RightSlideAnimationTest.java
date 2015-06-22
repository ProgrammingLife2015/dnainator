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
 * This class tests the implementation of the {@link RightSlideAnimation}.
 * This animation causes a {@link Pane} to slide in a right way direction.
 */
@RunWith(JfxRunner.class)
public class RightSlideAnimationTest {

	private Pane pane;
	private RightSlideAnimation rsa;
	
	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		pane = new Pane();
		// CHECKSTYLE.OFF: MagicNumber
		rsa = new RightSlideAnimation(pane, 100.0, 1.0, Position.LEFT);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test setting the size of the pane.
	 */
	@Test
	public void testSetCurSize() {
		// CHECKSTYLE.OFF: MagicNumber
		rsa.setCurSize(0.2);
		assertEquals(rsa.newSize, rsa.size * 0.2, 0.001);
		
		rsa = new RightSlideAnimation(pane, 100.0, 1.0, Position.RIGHT);
		rsa.setCurSize(0.2);
		assertEquals(rsa.newSize, rsa.size * (1.0 - 0.2), 0.001);
		
		rsa = new RightSlideAnimation(pane, 100.0, 1.0, Position.TOP);
		rsa.setCurSize(0.2);
		assertEquals(rsa.newSize, rsa.size, 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test setting visibility of the pane.
	 */
	@Test
	public void testVisibility() {
		// CHECKSTYLE.OFF: MagicNumber
		pane.setVisible(true);
		rsa.setRate(-0.5);
		rsa.setVisibility(pane, Position.LEFT);
		assertFalse(pane.isVisible());
		
		pane.setVisible(true);
		rsa.setRate(0.5);
		rsa.setVisibility(pane, Position.LEFT);
		assertTrue(pane.isVisible());
	
		pane.setVisible(true);
		rsa.setRate(-0.5);
		rsa.setVisibility(pane, Position.RIGHT);
		assertTrue(pane.isVisible());
		
		pane.setVisible(true);
		rsa.setRate(0.5);
		rsa.setVisibility(pane, Position.RIGHT);
		assertFalse(pane.isVisible());
		// CHECKSTYLE.ON: MagicNumber
	}
}
