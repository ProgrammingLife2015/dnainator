package nl.tudelft.dnainator.javafx.drawables.phylogeny;

import static org.junit.Assert.assertEquals;

import javafx.stage.Stage;
import nl.tudelft.dnainator.javafx.AllColorsInUseException;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.tree.TreeNode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

public class LeafNodeTest extends ApplicationTest {

	private LeafNode ln;
	private TreeNode tn;
	private ColorServer cs;
	private ColorServer csSpy;
	
	@Override
	public void start(Stage stage) throws Exception {
	}
	
	/**
	 * Creates new database service.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		tn = new TreeNode(null);
		cs = new ColorServer();
		csSpy = Mockito.spy(cs);
		ln = new LeafNode(tn, csSpy);
	}
	
	/**
	 * Test the creation of a {@link CollapsedNode}.
	 */
	@Test
	public void testCreate() {
		assertEquals(2, ln.getChildren().size());
	}
	
	/**
	 * Test what happens when a {@link CollapsedNode} is clicked with the mouse.
	 */
	@Test
	public void testOnMouseClicked() throws AllColorsInUseException {
		Mockito.doNothing().when(csSpy).askColor(Mockito.anyString());
		ln.onMouseClicked();
		Mockito.verify(csSpy, Mockito.atLeastOnce()).askColor(Mockito.anyString());
	}
}
