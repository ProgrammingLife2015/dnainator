package nl.tudelft.dnainator.ui.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import nl.tudelft.dnainator.graph.Graph;

/**
 * Controller class for all graph interaction.
 */
public class GraphController {
	@FXML private Group graphRoot;
	private ObjectProperty<Graph> graph = new SimpleObjectProperty<Graph>(this, "g");

	/**
	 * @return	the graph property
	 */
	public final ObjectProperty<Graph> getGraphProperty() {
		return graph;
	}

	/**
	 * @return	the current graph
	 */
	public final Graph getGraph() {
		return graph.get();
	}

	/**
	 * @param graph	the new graph
	 */
	public void setGraph(Graph graph) {
		this.graph.set(graph);
	}

	@FXML
	private void onMouseClick(MouseEvent e) {
		// CHECKSTYLE.OFF: MagicNumber
		graphRoot.getChildren().add(new Circle(10));
		System.out.println(getGraph().getRank(0));
		// CHECKSTYLE.ON: MagicNumber
	}
}
