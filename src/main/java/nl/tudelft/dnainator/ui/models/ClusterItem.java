package nl.tudelft.dnainator.ui.models;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import nl.tudelft.dnainator.graph.impl.Neo4jGraph;

public class ClusterItem extends ModelItem {
	private ObjectProperty<Transform> rootToItem;

	public ClusterItem(ObjectProperty<Transform> parent) {
		super((Neo4jGraph) null);

		rootToItem = new SimpleObjectProperty<>();
		ObjectBinding<Transform> transform = new ObjectBinding<Transform>() {
			{
				super.bind(parent);
			}
			@Override
			protected Transform computeValue() {
				return parent.get().createConcatenation(getLocalToParentTransform());
			}
		};
		rootToItem.bind(transform);

		setContent(new Circle(20, Color.BLUE));
		
		ModelItem mi;
		mi = new SequenceItem(rootToItem);
		mi.setTranslateX(0);
		getChildItems().add(mi);
		mi = new SequenceItem(rootToItem);
		mi.setTranslateX(100);
		getChildItems().add(mi);
		mi = new SequenceItem(rootToItem);
		mi.setTranslateX(200);
		getChildItems().add(mi);
	}

	@Override
	public Transform getRootToItem() {
		return rootToItem.get();
	}
	
	@Override
	public void update() {
		update(300);
	}
}
