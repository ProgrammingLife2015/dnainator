package nl.tudelft.dnainator.javafx.controllers;

import org.junit.AfterClass;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Test the file open controller by simulating user interaction.
 */
public class FileOpenControllerTest extends ApplicationTest {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private FileOpenController control;
	private static File junit;
	private static File edge;
	private static File node;
	
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/openpane.fxml"));
		AnchorPane openpane = loader.load();
		Scene scene = new Scene(openpane, WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.show();
		
		control = loader.getController();
		
		junit = new File("junittestfx");
		node = new File(junit, "test.node.graph");
		edge = new File(junit, "test.edge.graph");
		junit.mkdir();
		node.createNewFile();
		edge.createNewFile();
	}

	/**
	 * Test clicking the node field.
	 * @throws IOException
	 */
	@Test
	public void testNodeField() {
		control.toggle();

		sleep(1, TimeUnit.SECONDS).clickOn("#nodeField");
		type("JUNITTESTFX")
		.type(KeyCode.ENTER, 2)
		.sleep(1, TimeUnit.SECONDS);

		FxAssert.verifyThat("#nodeField", NodeMatchers.hasText(node.getAbsolutePath()));
		FxAssert.verifyThat("#edgeField", NodeMatchers.hasText(edge.getAbsolutePath()));
	}

	/**
	 * Test clicking the edge field.
	 */
	@Test
	public void testEdgeField() {
		control.toggle();

		sleep(1, TimeUnit.SECONDS).clickOn("#edgeField");
		type("JUNITTESTFX")
		.type(KeyCode.ENTER, 2)
		.sleep(1, TimeUnit.SECONDS);

		FxAssert.verifyThat("#nodeField", NodeMatchers.hasText(node.getAbsolutePath()));
		FxAssert.verifyThat("#edgeField", NodeMatchers.hasText(edge.getAbsolutePath()));
	}

	private FileOpenControllerTest type(String s) {
		s.chars().forEachOrdered(e -> type(KeyCode.getKeyCode(Character.toString((char) e))));
		return this;
	}

	/**
	 * Clean up.
	 */
	@AfterClass
	public static void delete() {
		junit.delete();
	}
}
