package nl.tudelft.dnainator.javafx.widgets;

import static org.junit.Assert.assertEquals;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorMap;
import nl.tudelft.dnainator.javafx.views.StrainView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

/**
 * This class tests the implementation of {@link JumpTo}.
 * It is responsibile for the jump to {@link TextField}s in the {@link StrainView}.
 * These {@link TextField}s allow the user quick navigation through the {@link Graph}.
 */
public class JumpToTest extends ApplicationTest {

	@Mock private Graph graph;
	private JumpTo jumpTo;
	private StrainView strainView;
	private ColorMap cm;
	private TextField textField;
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
		cm = new ColorMap();
		strainView = new StrainView(cm, graph);
		jumpTo = new JumpTo(strainView);
		// CHECKSTYLE.OFF: MagicNumber
		textField = (TextField) jumpTo.getChildren().get(0);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test creation of a jump to node {@link TextField}.
	 */
	@Test
	public void testToggleJumpToNode() {
		Platform.runLater(() -> {
			jumpTo.toggleJumpNode();
			assertEquals(NODE, textField.getPromptText());
		});
	}
	
	/**
	 * Test creation of a jump to rank {@link TextField}.
	 */
	@Test
	public void testToggleJumpToRank() {
		Platform.runLater(() -> {
			jumpTo.toggleJumpRank();
			assertEquals(RANK, textField.getPromptText());
		});
	}
	
	/**
	 * Test creation of a jump to rank {@link TextField}.
	 */
	@Test
	public void testToggleJumpToAnnotation() {
		Platform.runLater(() -> {
			jumpTo.toggleJumpAnnotation();
			assertEquals(ANNOTATION, textField.getPromptText());
		});
	}
}
