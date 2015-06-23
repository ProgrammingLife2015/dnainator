package nl.tudelft.dnainator.javafx.widgets.dialogs;

import static org.junit.Assert.assertEquals;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

/**
 * This class tests the implementation of the {@link ExceptionDialog}.
 * Shows a dialog whenever an exception is thrown.
 */
public class ExceptionDialogTest extends ApplicationTest {
	
	private ExceptionDialog ed;
	private Pane pane;
	@Mock private Throwable throwable;
	
	// Used for the pane.
	@SuppressWarnings("unused")
	private Scene scene;
	
	@Override
	public void start(Stage stage) throws Exception {
		pane = new Pane();
		scene = new Scene(pane);
	}
	
	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(throwable.getMessage()).thenReturn("error!");
	}
	
	/**
	 * Test creating a {@link AboutDialog}.
	 * Ensure that there is a button and that text is set correctly.
	 */
	@Test
	public void testCreateMenus() {
		Platform.runLater(() -> {
			ed = new ExceptionDialog(pane, throwable, "msg");
			// CHECKSTYLE.OFF: MagicNumber
			assertEquals(1, ed.getButtonTypes().size());
			// CHECKSTYLE.ON: MagicNumber
			assertEquals("msg", ed.getTitle());
			assertEquals("error!", ed.getHeaderText());
			assertEquals("The exception stacktrace was:", ed.getContentText());
		});
	}	
}
