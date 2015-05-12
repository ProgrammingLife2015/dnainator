package nl.tudelft.dnainator.ui.models;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;

public abstract class ModelItem extends Region {
	private Graph graph;
	private Node content;
	private List<ModelItem> children;

	public ModelItem() {
		this(Neo4jSingleton.getInstance().getDatabase());
	}

	public ModelItem(String DB_PATH) {
		this(Neo4jSingleton.getInstance().getDatabase(DB_PATH));
	}

	public ModelItem(Graph graph) {
		this.graph = graph;
		this.children = new ArrayList<>();
	}

	public Node getContent() {
		return content;
	}

	public void setContent(Node content) {
		this.content = content;
	}

	public List<ModelItem> getChildItems() {
		return children;
	}

	public void setChildItems(List<ModelItem> childItems) {
		this.children = childItems;
	}

	public void update(Bounds b) {
		System.out.println("model: " + getBoundsInParent());
		System.out.println(getClass().getSimpleName() + ": " + b.contains(getBoundsInParent()));

		if (b.contains(getBoundsInParent())) {
			getChildren().clear();
			getChildren().add(getContent());
		} else {
			getChildren().clear();
			getChildren().addAll(getChildItems());
			for (ModelItem m : children) {
				m.update(b);
			}
		}
	}
}
