package nl.tudelft.dnainator.javafx.controllers;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
	 * Test creation.
	 */
	@Test
	public void testCreate() {
		assertNotNull(control.graphProperty());
		assertNull(control.graphProperty().get());
	}

	/**
	 * Test clicking the node field.
	 * @throws IOException
	 */
	@Test
	public void testNodeField() {
		togglePane().clickOn("#nodeField");

		FxAssert.verifyThat("#nodeField", verify(FileOpenController.NODE));
		FxAssert.verifyThat("#edgeField", verify(FileOpenController.EDGE));
	}

	/**
	 * Test clicking the edge field.
	 */
	@Test
	public void testEdgeField() {
		togglePane().clickOn("#edgeField");

		FxAssert.verifyThat("#nodeField", verify(FileOpenController.NODE));
		FxAssert.verifyThat("#edgeField", verify(FileOpenController.EDGE));
	}

	/**
	 * Test clicking the tree field.
	 */
	@Test
	public void testTreeField() {
		togglePane().clickOn("#newickField");

		FxAssert.verifyThat("#newickField", verify(FileOpenController.NEWICK));
	}

	/**
	 * Test clicking the gff field.
	 */
	@Test
	public void testGffField() {
		togglePane().clickOn("#gffField");

		FxAssert.verifyThat("#gffField", verify(FileOpenController.GFF));
	}

	/**
	 * Test clicking the dr field.
	 */
	@Test
	public void testDRField() {
		togglePane().clickOn("#drField");

		FxAssert.verifyThat("#drField", verify(FileOpenController.DR));
	}

	/**
	 * Test enabling the load button.
	 */
	@Test
	public void testEnable() {
		togglePane();

		FxAssert.verifyThat("#openButton", NodeMatchers.isDisabled());

		clickOn("#nodeField")
		.clickOn("#newickField")
		.clickOn("#gffField");

		FxAssert.verifyThat("#openButton", NodeMatchers.isEnabled());
	}

	/**
	 * Test loading.
	 * FIXME: Doesn't work on travis?
	 */
//	@Test
//	public void testLoad() {
//		togglePane();
//
//		FxAssert.verifyThat("#openButton", NodeMatchers.isDisabled());
//
//		clickOn("#nodeField")
//		.clickOn("#newickField")
//		.clickOn("#gffField")
//		.clickOn("#openButton")
//		.sleep(2, TimeUnit.SECONDS)
//		.type(KeyCode.ENTER)
//		.type(KeyCode.ESCAPE);
//
//		FxAssert.verifyThat("#container", NodeMatchers.isInvisible());
//	}

	private FxRobot togglePane() {
		control.toggle();
		return sleep(1, TimeUnit.SECONDS);
	}

	private Matcher<Node> verify(String node) {
		return NodeMatchers.hasText(control.getMap().get(node));
	}
}
