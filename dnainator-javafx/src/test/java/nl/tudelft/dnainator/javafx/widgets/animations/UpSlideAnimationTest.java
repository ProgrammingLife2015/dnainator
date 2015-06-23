package nl.tudelft.dnainator.javafx.widgets.animations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import nl.tudelft.dnainator.javafx.widgets.animations.TransitionAnimation.Position;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

/**
 * This class tests the implementation of the {@link UpSlideAnimation}.
 * This animation causes a {@link Pane} to slide in a upward direction.
 */
public class UpSlideAnimationTest extends ApplicationTest {

	private Pane pane;
	private UpSlideAnimation usa;
	
	@Override
	public void start(Stage stage) throws Exception {
		pane = new Pane();
	}
	
	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		// CHECKSTYLE.OFF: MagicNumber
		usa = new UpSlideAnimation(pane, 100.0, 1.0, Position.TOP);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test setting the size of the pane.
	 */
	@Test
	public void testSetCurSize() {
		// CHECKSTYLE.OFF: MagicNumber
		usa.setCurSize(0.2);
		assertEquals(usa.newSize, usa.size * (1.0 - 0.2), 0.001);
		
		usa = new UpSlideAnimation(pane, 100.0, 1.0, Position.BOTTOM);
		usa.setCurSize(0.2);
		assertEquals(usa.newSize, usa.size * 0.2, 0.001);
		
		usa = new UpSlideAnimation(pane, 100.0, 1.0, Position.RIGHT);
		usa.setCurSize(0.2);
		assertEquals(usa.newSize, usa.size, 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test setting visibility of the pane.
	 */
	@Test
	public void testVisibility() {
		// CHECKSTYLE.OFF: MagicNumber
		pane.setVisible(true);
		usa.setRate(-0.5);
		usa.setVisibility(pane, Position.TOP);
		assertTrue(pane.isVisible());
		
		pane.setVisible(true);
		usa.setRate(0.5);
		usa.setVisibility(pane, Position.TOP);
		assertFalse(pane.isVisible());
	
		pane.setVisible(true);
		usa.setRate(-0.5);
		usa.setVisibility(pane, Position.BOTTOM);
		assertFalse(pane.isVisible());
		
		pane.setVisible(true);
		usa.setRate(0.5);
		usa.setVisibility(pane, Position.BOTTOM);
		assertTrue(pane.isVisible());
		// CHECKSTYLE.ON: MagicNumber
	}
}
