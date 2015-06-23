package nl.tudelft.dnainator.javafx.widgets;

import static org.junit.Assert.assertEquals;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.views.StrainView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

/**
 * This class tests the implementation of {@link StrainControl}.
 * It is responsibile for the jump to {@link Textfield}s in the {@link StrainView}.
 * These {@link Textfield}s allow the user quick navigation through the {@link Graph}.
 */
public class StrainControlTest extends ApplicationTest {

	@Mock private Graph graph;
	private StrainControl strainControl;
	private StrainView strainView;
	private ColorServer cs;
	private TextField jumpTo;
	private static final String NODE = "Jump to node...";
	private static final String RANK = "Jump to rank...";
	private static final String ANNOTATION = "Jump to annotation...";
	
	@Override
	public void start(Stage stage) throws Exception {
	}
	
	/**
	 * Set up common variables.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		cs = new ColorServer();
		strainView = new StrainView(cs, graph);
		strainControl = new StrainControl(strainView);
		// CHECKSTYLE.OFF: MagicNumber
		jumpTo = (TextField) strainControl.getChildren().get(0);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test creation of a jump to node {@link TextField}.
	 */
	@Test
	public void testToggleJumpToNode() {
		Platform.runLater(() -> {
			strainControl.toggleJumpNode();
			assertEquals(NODE, jumpTo.getPromptText());
		});
	}
	
	/**
	 * Test creation of a jump to rank {@link TextField}.
	 */
	@Test
	public void testToggleJumpToRank() {
		Platform.runLater(() -> {
			strainControl.toggleJumpRank();
			assertEquals(RANK, jumpTo.getPromptText());
		});
	}
	
	/**
	 * Test creation of a jump to rank {@link TextField}.
	 */
	@Test
	public void testToggleJumpToAnnotation() {
		Platform.runLater(() -> {
			strainControl.toggleJumpAnnotation();
			assertEquals(ANNOTATION, jumpTo.getPromptText());
		});
	}
}
