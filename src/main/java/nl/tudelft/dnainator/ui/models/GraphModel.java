package nl.tudelft.dnainator.ui.models;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nl.tudelft.dnainator.core.DefaultSequenceNode;
import nl.tudelft.dnainator.core.Edge;
import nl.tudelft.dnainator.core.SequenceNode;

/**
 * The bridging class between an implementation of {@link Graph} and JavaFX.
 * As the name implies, the GraphModel defines the Model part of the MVC pattern.
 */
public class GraphModel {
	private ListProperty<SequenceNode> nodesProperty;
	private ListProperty<Edge<Integer>> edgesProperty;

	/**
	 * Instantiates a new GraphModel object.
	 */
	public GraphModel() {
		nodesProperty = new SimpleListProperty<>(this, "nodes");
		edgesProperty = new SimpleListProperty<>(this, "edges");

		nodesProperty.set(FXCollections.observableArrayList());
		edgesProperty.set(FXCollections.observableArrayList());

		// CHECKSTYLE.OFF: MagicNumber
		getNodes().add(new DefaultSequenceNode("1", "Cat, Dog", 5, 7, "A"));
		getNodes().add(new DefaultSequenceNode("2", "Dog", 8, 10, "C"));
		getNodes().add(new DefaultSequenceNode("3", "Cat", 8, 9, "G"));
		getNodes().add(new DefaultSequenceNode("4", "Cat, Dog", 10, 13, "T"));

		getEdges().add(new Edge<Integer>(1, 2));
		getEdges().add(new Edge<Integer>(1, 3));
		getEdges().add(new Edge<Integer>(2, 4));
		getEdges().add(new Edge<Integer>(3, 4));
		// CHECKSTYLE.ON: MagicNumber
	}

	/**
	 * @return The {@link ObservableList} of nodes.
	 */
	public final ObservableList<SequenceNode> getNodes() {
		return nodesProperty.get();
	}

	/**
	 * Sets the nodes property to the new {@link ObservableList} of nodes.
	 * @param l The new {@link ObservableList} of nodes.
	 */
	public final void setNodes(ObservableList<SequenceNode> l) {
		nodesProperty.set(l);
	}

	/**
	 * @return The nodes property.
	 */
	public ListProperty<SequenceNode> nodesProperty() {
		return nodesProperty;
	}

	/**
	 * @return The {@link ObservableList} of edges.
	 */
	public final ObservableList<Edge<Integer>> getEdges() {
		return edgesProperty.get();
	}

	/**
	 * Sets the edges property to the new {@link ObservableList} of edges.
	 * @param l The new {@link ObservableList} of edges.
	 */
	public final void setEdges(ObservableList<Edge<Integer>> l) {
		edgesProperty.set(l);
	}

	/**
	 * @return The edges property.
	 */
	public ListProperty<Edge<Integer>> edgesProperty() {
		return edgesProperty;
	}
}
