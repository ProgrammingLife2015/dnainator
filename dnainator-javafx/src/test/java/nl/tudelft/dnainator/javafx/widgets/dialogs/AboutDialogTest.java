package nl.tudelft.dnainator.javafx.widgets.dialogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;

/**
 * This class tests the implementation of the {@link AboutDialog}.
 * Shows a dialog about the application's information.
 */
@RunWith(JfxRunner.class)
public class AboutDialogTest {
	
	private AboutDialog ad;
	private Pane pane;
	
	// Used for the pane.
	@SuppressWarnings("unused")
	private Scene scene;
	
	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		pane = new Pane();
		scene = new Scene(pane);
	}
	
	/**
	 * Test creating a {@link AboutDialog}.
	 * Ensure that there is a button and that text is set correctly.
	 */
	@Test
	@TestInJfxThread
	public void testCreateMenus() {
		ad = new AboutDialog(pane);
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(1, ad.getButtonTypes().size());
		// CHECKSTYLE.ON: MagicNumber
		assertEquals("About DNAinator", ad.getTitle());
		assertEquals("DNAinator\nDNA network visualization tool", ad.getHeaderText());
		assertTrue(ad.isResizable());
		assertNotNull(ad.getGraphic());
	}
	
}
