package nl.tudelft.dnainator.ui.models;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.transform.Transform;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;
import nl.tudelft.dnainator.ui.drawables.DrawableNode;

/**
 * The {@link RankItem} class represents the bottom level object in the viewable model.
 * It can hold only content and no children, and is therefore a leaf in the composite pattern.
 */
public class RankItem extends ModelItem {
	private static final String TYPE = "Rank";

	/**
	 * Construct a new bottom level {@link RankItem} using the default graph.
	 * Since a {@link RankItem} is not at the root of the model,
	 * it should bind its parent for correct positioning.
	 * @param parent	the concatenated transfrom from root to parent
	 */
	public RankItem(ObjectProperty<Transform> parent) {
		this(Neo4jSingleton.getInstance().getDatabase(), parent);
	}

	/**
	 * Construct a new bottom level {@link RankItem} using the default graph.
	 * Since a {@link RankItem} is not at the root of the model,
	 * it should bind its parent for correct positioning.
	 * @param graph		the specified graph
	 * @param parent	the concatenated transfrom from root to parent
	 */
	public RankItem(Graph graph, ObjectProperty<Transform> parent) {
		super(graph);

		bindLocalToRoot(parent);
	}

	private void load() {
		if (getContent().getChildren().size() == 0) {
			int rank = (int) localToRoot(new Point2D(0, 0)).getX() / RANK_WIDTH;

			List<SequenceNode> nodes = getGraph().getRank(rank);
			for (int i = 0; i < nodes.size(); i++) {
				DrawableNode drawable = new DrawableNode(nodes.get(i), RANK_SIZE);
				drawable.setTranslateY(i * RANK_WIDTH);
				getContent().getChildren().add(drawable);
			}
			getContent().setTranslateY((float) -nodes.size() * RANK_WIDTH / 2);

			System.out.println("sequence children " + rank + ": "
					+ getContent().getChildren().size());
		}
	}

	@Override
	public void update(Bounds b) {
		if (!isInViewport(b)) {
			return;
		}

		load();
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public List<String> getSources() {
		return null;
	}
}
