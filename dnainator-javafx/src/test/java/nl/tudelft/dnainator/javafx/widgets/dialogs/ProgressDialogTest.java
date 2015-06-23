package nl.tudelft.dnainator.javafx.widgets.dialogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;


import org.junit.Test;
import org.mockito.Mock;
import org.testfx.framework.junit.ApplicationTest;


/**
 * This class tests the implementation of the {@link ProgressDialog}.
 * Shows all hotkeys that are being used.
 */
public class ProgressDialogTest extends ApplicationTest {
	
	private ProgressDialog pd;
	private Window window;
	@Mock private Service<?> service;
	private Scene scene;
	private static final String TITLE = "DNAinator";
	
	@Override
	public void start(Stage stage) throws Exception {
		scene = new Scene(new Pane());
		window = scene.getWindow();
		
		Platform.runLater(() -> pd = new ProgressDialog(window, service));
	}
	
	/**
	 * Test creating a {@link HotkeyHelpDialog}.
	 * Ensure that there is a button.
	 * No menus are given.
	 */
	@Test
	public void testCreate() {
		Platform.runLater(() -> {
			// CHECKSTYLE.OFF: MagicNumber
			assertEquals(1, pd.getButtonTypes().size());
			// CHECKSTYLE.ON: MagicNumber
			assertEquals(TITLE, pd.getTitle());
			assertEquals("Loading...", pd.getHeaderText());
			assertNotNull(pd.getDialogPane().getContent());
		});
	}
	
	@Test
	public void testSetProgress() {
		Platform.runLater(() -> {
			ProgressBar pb = (ProgressBar) pd.getDialogPane().getContent();
			
			// CHECKSTYLE.OFF: MagicNumber
			pd.setProgress(0.2);
			assertEquals(0.2, pb.getProgress(), 0.001);
			// CHECKSTYLE.ON: MagicNumber
		});
	}
}
