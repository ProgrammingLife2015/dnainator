package nl.tudelft.dnainator.javafx.controllers;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeUnit;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static org.junit.Assert.assertNotNull;

/**
 * Test the welcome screen.
 */
public class WelcomeControllerTest extends ApplicationTest {
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private WelcomeController control;

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/welcome.fxml"));
		AnchorPane welcome = loader.load();
		Scene scene = new Scene(welcome, WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.show();

		control = loader.getController();
	}

	/**
	 * Test creation of the welcome screen.
	 */
	@Test
	public void testCreate() {
		sleep(1, TimeUnit.SECONDS);
		assertNotNull(control.currentDBProperty());
	}
}
