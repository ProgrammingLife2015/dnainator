package nl.tudelft.dnainator.javafx.widgets.dialogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;

/**
 * This class tests the implementation of the {@link ExceptionDialog}.
 * Shows a dialog whenever an exception is thrown.
 */
@RunWith(JfxRunner.class)
public class ExceptionDialogTest {
	
	private ExceptionDialog ed;
	private Pane pane;
	@Mock private Throwable throwable;
	
	// Used for the pane.
	@SuppressWarnings("unused")
	private Scene scene;
	
	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(throwable.getMessage()).thenReturn("error!");
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
		ed = new ExceptionDialog(pane, throwable, "msg");
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(1, ed.getButtonTypes().size());
		// CHECKSTYLE.ON: MagicNumber
		assertEquals("msg", ed.getTitle());
		assertEquals("error!", ed.getHeaderText());
		assertEquals("The exception stacktrace was:", ed.getContentText());
	}
	
}
