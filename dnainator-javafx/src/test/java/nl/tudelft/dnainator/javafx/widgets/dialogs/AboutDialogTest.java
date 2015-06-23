package nl.tudelft.dnainator.javafx.widgets.dialogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

/**
 * This class tests the implementation of the {@link AboutDialog}.
 * Shows a dialog about the application's information.
 */
public class AboutDialogTest extends ApplicationTest {
	
	private AboutDialog ad;
	private Pane pane;
	
	// Used for the pane.
	@SuppressWarnings("unused")
	private Scene scene;
	
	@Override
	public void start(Stage stage) throws Exception {
		pane = new Pane();
		scene = new Scene(pane);
	}
	
	/**
	 * Test creating a {@link AboutDialog}.
	 * Ensure that there is a button and that text is set correctly.
	 */
	@Test
	public void testCreateMenus() {
		Platform.runLater(() -> {
			ad = new AboutDialog(pane);
			// CHECKSTYLE.OFF: MagicNumber
			assertEquals(1, ad.getButtonTypes().size());
			// CHECKSTYLE.ON: MagicNumber
			assertEquals("About DNAinator", ad.getTitle());
			assertEquals("DNAinator\nDNA network visualization tool", ad.getHeaderText());
			assertTrue(ad.isResizable());
			assertNotNull(ad.getGraphic());
		});
	}
}
