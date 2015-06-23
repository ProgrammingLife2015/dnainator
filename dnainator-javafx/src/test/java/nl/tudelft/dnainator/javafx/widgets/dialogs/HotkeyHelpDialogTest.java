package nl.tudelft.dnainator.javafx.widgets.dialogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;

import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;

/**
 * This class tests the implementation of the {@link HotkeyHelpDialog}.
 * Shows all hotkeys that are being used.
 */
@RunWith(JfxRunner.class)
public class HotkeyHelpDialogTest {
	
	private HotkeyHelpDialog hhd;
	private Pane pane;
	
	// Used for the pane.
	@SuppressWarnings("unused")
	private Scene scene;
	private static final String TITLE = "Hotkeys";
	
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
	 * Test creating a {@link HotkeyHelpDialog}.
	 * Ensure that there is a button.
	 * No menus are given.
	 */
	@Test
	@TestInJfxThread
	public void testCreateNoMenus() {
		hhd = new HotkeyHelpDialog(pane);
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(1, hhd.getButtonTypes().size());
		assertEquals(300, hhd.getDialogPane().getPrefWidth(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
		assertEquals(TITLE, hhd.getTitle());
		assertEquals(TITLE, hhd.getHeaderText());
		assertEquals(Modality.NONE, hhd.getModality());
		
		GridPane gp = (GridPane) hhd.getDialogPane().getContent();
		assertTrue(gp.getChildren().isEmpty());
	}
	
	/**
	 * Test creating a {@link HotkeyHelpDialog}.
	 * Ensure that there is a button.
	 * Use a given menu.
	 */
	@Test
	@TestInJfxThread
	public void testCreateMenus() {
		Menu menu = new Menu();
		MenuItem mi = new MenuItem();
		mi.setAccelerator(new KeyCodeCombination(KeyCode.F1, KeyCombination.SHORTCUT_ANY));
		menu.getItems().add(mi);
		
		hhd = new HotkeyHelpDialog(pane, menu);
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(1, hhd.getButtonTypes().size());
		assertEquals(300, hhd.getDialogPane().getPrefWidth(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
		assertEquals(TITLE, hhd.getTitle());
		assertEquals(TITLE, hhd.getHeaderText());
		assertEquals(Modality.NONE, hhd.getModality());
		
		GridPane gp = (GridPane) hhd.getDialogPane().getContent();
		assertFalse(gp.getChildren().isEmpty());
	}
	
}
