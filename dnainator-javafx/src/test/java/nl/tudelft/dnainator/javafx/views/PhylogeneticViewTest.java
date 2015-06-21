package nl.tudelft.dnainator.javafx.views;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.VerticalDirection;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.Window;
import nl.tudelft.dnainator.core.impl.TreeNode;
import nl.tudelft.dnainator.javafx.ColorMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the implementation of the {@link PhylogeneticView}.
 * This is the view responsible for everything related to the phylogenetic tree.
 */
@RunWith(MockitoJUnitRunner.class)
public class PhylogeneticViewTest extends ApplicationTest {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private Window window;
	private PhylogeneticView view;
	@Mock private ColorMap colorMap;
	
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view.fxml"));
		view = new PhylogeneticView(new ColorMap(), createTree());
		loader.setRoot(view);
		loader.load();

		Scene scene = new Scene(view, WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.show();

		window = scene.getWindow();
	}
	
	/**
	 * Tests creating a {@link PhylogeneticView} with a tree containing two children.
	 */
	@Test
	public void testCreate() {
		TreeNode tree = createTree();
		view = new PhylogeneticView(colorMap, tree);
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(2, tree.getChildren().size());
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * Test zooming in by scrolling the mouse.
	 */
	@Test
	public void testOnScroll() {
		double zoomPrevious;

		zoomPrevious = view.scale.getMxx();
		moveTo(view).scroll(VerticalDirection.UP).sleep(1, TimeUnit.SECONDS);
		assertTrue(view.scale.getMxx() > zoomPrevious);
		assertTrue(view.scale.getMyy() > zoomPrevious);

		zoomPrevious = view.scale.getMxx();
		moveTo(view).scroll(VerticalDirection.DOWN).sleep(1, TimeUnit.SECONDS);
		assertTrue(view.scale.getMxx() < zoomPrevious);
		assertTrue(view.scale.getMyy() < zoomPrevious);
	}

	/**
	 * Test panning by dragging the screen.
	 */
	@Test
	public void testOnMouseDragged() {
		double prevX;

		prevX = view.translate.getX();
		drag(window.getX(), window.getY(), MouseButton.PRIMARY)
		.sleep(1, TimeUnit.MICROSECONDS)
		.dropTo(window.getX() + 2, window.getY())
		.sleep(1, TimeUnit.SECONDS);
		assertTrue(view.translate.getX() > prevX);

		prevX = view.translate.getX();
		drag(window.getX() + 2, window.getY(), MouseButton.PRIMARY)
		.sleep(1, TimeUnit.MICROSECONDS)
		.dropTo(window.getX(), window.getY())
		.sleep(1, TimeUnit.SECONDS);
		assertTrue(view.translate.getX() < prevX);
	}

	/**
	 * Test panning and zooming by using the keyboard.
	 */
	@Test
	public void testOnKeyPressed() {
		double prevX, prevY, prevZoom;
		clickOn(view);
		prevX = view.translate.getX();
		type(KeyCode.RIGHT);
		assertTrue(view.translate.getX() < prevX);

		prevX = view.translate.getX();
		type(KeyCode.LEFT);
		assertTrue(view.translate.getX() > prevX);

		prevY = view.translate.getY();
		type(KeyCode.DOWN);
		assertTrue(view.translate.getY() < prevY);

		prevY = view.translate.getY();
		type(KeyCode.UP);
		assertTrue(view.translate.getY() > prevY);

		prevZoom = view.scale.getMxx();
		type(KeyCode.EQUALS);
		assertTrue(view.scale.getMxx() > prevZoom);
		assertTrue(view.scale.getMyy() > prevZoom);

		prevZoom = view.scale.getMxx();
		type(KeyCode.MINUS);
		assertTrue(view.scale.getMxx() < prevZoom);
		assertTrue(view.scale.getMyy() < prevZoom);
	}

	/**
	 * Test zooming on the {@link PhylogeneticView}.
	 * Zooming out of bounds should not change the zoom.
	 */
	@Test
	public void testZoom() {
		double zoomPrevious = view.scale.getMxx();
		
		// CHECKSTYLE.OFF: MagicNumber
		view.zoom(1.0, new Point2D(1.0, 2.0));
		// CHECKSTYLE.ON: MagicNumber
		
		assertTrue(view.scale.getMxx() > zoomPrevious);
		assertTrue(view.scale.getMyy() > zoomPrevious);
		
		zoomPrevious = view.scale.getMxx();
		// CHECKSTYLE.OFF: MagicNumber
		view.zoom(Double.MAX_VALUE, new Point2D(1.0, 2.0));
		assertEquals(zoomPrevious, view.scale.getMxx(), 0.001);
		assertEquals(zoomPrevious, view.scale.getMyy(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}
	
	/**
	 * Test the default scale of the {@link PhylogeneticView}.
	 */
	@Test
	public void testGetScale() {
		// CHECKSTYLE.OFF: MagicNumber
		assertEquals(0.1, view.getScale().getMxx(), 0.001);
		assertEquals(0.1, view.getScale().getMyy(), 0.001);
		// CHECKSTYLE.ON: MagicNumber
	}

	private TreeNode createTree() {
		TreeNode tree, child;
		tree = new TreeNode(null);
		child = new TreeNode(tree);
		child.setName("A");
		child = new TreeNode(tree);
		child.setName("B");

		return tree;
	}
}
