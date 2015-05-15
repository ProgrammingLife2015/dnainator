package nl.tudelft.dnainator.ui.models;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;
import nl.tudelft.dnainator.ui.drawables.DrawableNode;

/**
 * The {@link RankItem} class represents the bottom level object in the viewable model.
 * It can hold only content and no children, and is therefore a leaf in the composite pattern.
 */
public class RankItem extends ModelItem {
	/**
	 * Construct a new bottom level {@link RankItem} using the default graph.
	 * Since a {@link RankItem} is not at the root of the model,
	 * it should bind its parent for correct positioning.
	 * @param parent	the concatenated transfrom from root to parent
	 */
	public RankItem(ObjectProperty<Transform> parent) {
		super(Neo4jSingleton.getInstance().getDatabase());

		bindLocalToRoot(parent);
	}

	@Override
	public void update(Bounds b) {
		if (!isInViewport(b)) {
			return;
		}

		int rank = (int) localToRoot(new Rectangle().getBoundsInLocal()).getMinX() / RANK_WIDTH;

		Group g = new Group();
		List<SequenceNode> nodes = getGraph().getRank(rank);
		for (int i = 0; i < nodes.size(); i++) {
			SequenceNode node = nodes.get(i);
			DrawableNode drawable = new DrawableNode(node, RANK_SIZE);
			drawable.setTranslateY(i * RANK_WIDTH);
			g.getChildren().add(drawable);
		}
		g.setTranslateY((float) -nodes.size() * RANK_WIDTH / 2);
		System.out.println("sequence children " + rank + ": " + getChildren().size());

		setContent(g);
	}
}
