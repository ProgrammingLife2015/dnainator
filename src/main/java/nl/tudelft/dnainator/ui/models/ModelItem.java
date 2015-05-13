package nl.tudelft.dnainator.ui.models;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;

public abstract class ModelItem extends Pane {
	private Graph graph;
	private Group content;
	private List<ModelItem> children;
	private Group childRoot;

	public ModelItem() {
		this(Neo4jSingleton.getInstance().getDatabase());
	}

	public ModelItem(String DB_PATH) {
		this(Neo4jSingleton.getInstance().getDatabase(DB_PATH));
	}

	public ModelItem(Graph graph) {
		this.graph = graph;
		this.children = new ArrayList<>();
		this.content = new Group();
		this.childRoot = new Group();

		getChildren().add(content);
		getChildren().add(childRoot);
	}

	public Group getContent() {
		return content;
	}

	public void setContent(Node node) {
		content.getChildren().clear();
		content.getChildren().add(node);
	}

	public Group getChildRoot() {
		return childRoot;
	}

	public void setChildRoot(Group childroot) {
		this.childRoot = childroot;
	}

	public List<ModelItem> getChildItems() {
		return children;
	}

	public abstract Transform getRootToItem();
	
	public Bounds localToRoot(Bounds b) {
		return getRootToItem().transform(b);
	}

	public void setChildItems(List<ModelItem> childItems) {
		this.children = childItems;
	}

	public void update() {
		update(150);
	}
	
	public void update(double threshold) {
		if (localToRoot(new Rectangle(100, 1).getBoundsInLocal()).getWidth() < threshold) {
			getContent().setVisible(true);
			getChildRoot().getChildren().clear();
		} else {
			getContent().setVisible(false);
			getChildRoot().getChildren().clear(); // FIXME
			getChildRoot().getChildren().addAll(getChildItems());
			for (ModelItem m : children) {
				m.update();
			}
		}
	}
}
