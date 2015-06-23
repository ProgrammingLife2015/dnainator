package nl.tudelft.dnainator.javafx.controllers;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Test the file open controller by simulating user interaction.
 */
public class FileOpenControllerTest extends ApplicationTest {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private DummyFileOpenController control;
	
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/openpane.fxml"));
		loader.setControllerFactory(new Callback<Class<?>, Object>() {
			public Object call(Class<?> p) {
				return new DummyFileOpenController();
			}
		});
		AnchorPane openpane = loader.load();
		Scene scene = new Scene(openpane, WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.show();
		
		control = loader.getController();
	}

	/**
	 * Test clicking the node field.
	 * @throws IOException
	 */
	@Test
	public void testNodeField() {
		clickFieldAndNavigateFileChooser("#nodeField");

		FxAssert.verifyThat("#nodeField", verify(FileOpenController.NODE));
		FxAssert.verifyThat("#edgeField", verify(FileOpenController.EDGE));
	}

	/**
	 * Test clicking the edge field.
	 */
	@Test
	public void testEdgeField() {
		clickFieldAndNavigateFileChooser("#edgeField");

		FxAssert.verifyThat("#nodeField", verify(FileOpenController.NODE));
		FxAssert.verifyThat("#edgeField", verify(FileOpenController.EDGE));
	}

	/**
	 * Test clicking the tree field.
	 */
	@Test
	public void testTreeField() {
		clickFieldAndNavigateFileChooser("#newickField");

		FxAssert.verifyThat("#newickField", verify(FileOpenController.NEWICK));
	}

	/**
	 * Test clicking the gff field.
	 */
	@Test
	public void testGffField() {
		clickFieldAndNavigateFileChooser("#gffField");

		FxAssert.verifyThat("#gffField", verify(FileOpenController.GFF));
	}

	private void clickFieldAndNavigateFileChooser(String field) {
		control.toggle();
		sleep(1, TimeUnit.SECONDS)
		.clickOn(field);
	}

	private Matcher<Node> verify(String node) {
		return NodeMatchers.hasText(control.getMap().get(node));
	}
}
