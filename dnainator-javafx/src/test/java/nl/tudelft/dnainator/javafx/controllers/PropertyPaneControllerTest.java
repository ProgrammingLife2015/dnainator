package nl.tudelft.dnainator.javafx.controllers;

import nl.tudelft.dnainator.core.PropertyType;

import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.ParentMatchers;

import scala.util.Random;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Test the property pane.
 */
public class PropertyPaneControllerTest extends ApplicationTest {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final int LENGTH = 500;
	private static final int LETTERS = 26;
	private PropertyPaneController control;
	private Random random;

	/**
	 * A property enum used for this test only.
	 */
	private enum TestProperties implements PropertyType {
		TITLE("Title");

		private String description;
		private TestProperties(String description) {
			this.description = description;
		}

		@Override
		public String description() {
			return description;
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/propertypane.fxml"));
		VBox openpane = loader.load();
		Scene scene = new Scene(openpane, WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.show();

		control = loader.getController();
		random = new Random(System.nanoTime());
	}

	/**
	 * Test showing a property in the property pane.
	 */
	@Test
	public void testField() {
		togglePane();

		FxAssert.verifyThat("#propertyPane", ParentMatchers.hasChildren(0));

		Map<PropertyType, String> prop = Collections.singletonMap(TestProperties.TITLE, "value");
		Platform.runLater(() -> control.update(() -> prop));
		sleep(1, TimeUnit.SECONDS);

		FxAssert.verifyThat("#propertyPane", NodeMatchers.hasChild("Title"));

		togglePane();
	}

	/**
	 * Test showing a large property in the property pane.
	 */
	@Test
	public void testLargeField() {
		togglePane();

		FxAssert.verifyThat("#propertyPane", ParentMatchers.hasChildren(0));

		Map<PropertyType, String> prop = Collections.singletonMap(TestProperties.TITLE, generate());
		Platform.runLater(() -> control.update(() -> prop));
		sleep(1, TimeUnit.SECONDS);

		FxAssert.verifyThat("#propertyPane", NodeMatchers.hasChild("Title"));

		togglePane();
	}

	private String generate() {
		StringBuilder sb = new StringBuilder();
		IntStream.range(0, LENGTH).map(e -> e * random.nextInt(LETTERS)).forEach(c -> sb.append(c));
		return sb.toString();
	}

	private FxRobot togglePane() {
		control.toggle();
		return sleep(1, TimeUnit.SECONDS);
	}
}
