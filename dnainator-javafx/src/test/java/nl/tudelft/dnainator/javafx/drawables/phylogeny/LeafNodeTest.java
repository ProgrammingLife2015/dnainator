package nl.tudelft.dnainator.javafx.drawables.phylogeny;

import static org.junit.Assert.assertEquals;
import javafx.stage.Stage;
import nl.tudelft.dnainator.javafx.exceptions.AllColorsInUseException;
import nl.tudelft.dnainator.javafx.ColorMap;
import nl.tudelft.dnainator.tree.TreeNode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit.ApplicationTest;

/**
 * Test the {@link LeafNode} of the phylogenetic tree.
 */
public class LeafNodeTest extends ApplicationTest {

	private LeafNode ln;
	private TreeNode tn;
	private ColorMap cs;
	private ColorMap csSpy;
	
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
		cs = new ColorMap();
		csSpy = Mockito.spy(cs);
		ln = new LeafNode(tn, csSpy);
	}
	
	/**
	 * Test the creation of a {@link LeafNode}.
	 */
	@Test
	public void testCreate() {
		assertEquals(2, ln.getChildren().size());
	}
	
	/**
	 * Test what happens when a {@link LeafNode} is clicked with the mouse.
	 * @throws AllColorsInUseException when all colors are in use.
	 */
	@Test
	public void testOnMouseClicked() throws AllColorsInUseException {
		Mockito.doNothing().when(csSpy).askColor(Mockito.anyString());
		ln.onMouseClicked();
		Mockito.verify(csSpy, Mockito.atLeastOnce()).askColor(Mockito.anyString());
	}
}
