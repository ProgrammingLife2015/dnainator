package nl.tudelft.dnainator.javafx.widgets.dialogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;


/**
 * This class tests the implementation of the {@link HotkeyHelpDialog}.
 * Shows all hotkeys that are being used.
 */
public class HotkeyHelpDialogTest extends ApplicationTest {
	
	private HotkeyHelpDialog hhd;
	private Pane pane;
	
	// Used for the pane.
	@SuppressWarnings("unused")
	private Scene scene;
	private static final String TITLE = "Hotkeys";
	
	@Override
	public void start(Stage stage) throws Exception {
		pane = new Pane();
		scene = new Scene(pane);
	}
	
	/**
	 * Test creating a {@link HotkeyHelpDialog}.
	 * Ensure that there is a button.
	 * No menus are given.
	 */
	@Test
	public void testCreateNoMenus() {
		Platform.runLater(() -> {
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
		});
	}
	
	/**
	 * Test creating a {@link HotkeyHelpDialog}.
	 * Ensure that there is a button.
	 * Use a given menu.
	 */
	@Test
	public void testCreateMenus() {
		Platform.runLater(() -> {
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
		});
	}
}
