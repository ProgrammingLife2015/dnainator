package nl.tudelft.dnainator.ui.models;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import nl.tudelft.dnainator.graph.impl.Neo4jGraph;

public class ClusterItem extends ModelItem {
	public ClusterItem() {
		super((Neo4jGraph) null);
		
		setContent(new Circle(20, Color.BLUE));
		getChildren().add(getContent());
	}
}
