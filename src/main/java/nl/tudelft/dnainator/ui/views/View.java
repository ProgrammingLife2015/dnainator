package nl.tudelft.dnainator.ui.views;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;
import nl.tudelft.dnainator.ui.widgets.ExceptionDialog;

import org.neo4j.io.fs.FileUtils;

/**
 * This class is the View part of the MVC pattern.
 */
public class View extends Pane {
	/**
	 * Creates a new view instance.
	 */
	public View() {
		try {
			FileUtils.deleteRecursively(new File(Neo4jSingleton.DB_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		loadFXML();
		getStyleClass().add("view");
	}

	private void loadFXML() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/fxml/view.fxml"));
		fxmlLoader.setRoot(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			new ExceptionDialog(e, "Can not load the View!");
		}
	}
}
