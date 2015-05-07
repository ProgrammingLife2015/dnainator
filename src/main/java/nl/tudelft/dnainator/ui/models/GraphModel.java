package nl.tudelft.dnainator.ui.models;

import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nl.tudelft.dnainator.core.Edge;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.graph.Neo4jSingleton;

/**
 * The bridging class between an implementation of {@link nl.tudelft.dnainator.graph.Graph}
 * and JavaFX. As the name implies, the GraphModel defines the Model part of the MVC pattern.
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
	}

	/**
	 * @param n The rank whose {@link SequenceNode}s to return.
	 * @return All {@link SequenceNode}s at rank <code>n</code>/
	 */
	public List<SequenceNode> getRank(int n) {
		return Neo4jSingleton.getInstance().getDatabase().getRank(n);
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
