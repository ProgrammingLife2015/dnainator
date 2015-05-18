package nl.tudelft.dnainator.ui.models;

import java.util.List;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.ui.drawables.DrawableNode;

/**
 * The {@link RankItem} class represents the bottom level object in the viewable model.
 * It can hold only content and no children, and is therefore a leaf in the composite pattern.
 */
public class RankItem extends CompositeItem {
	private static final String TYPE = "Rank";

	/**
	 * Construct a new bottom level {@link RankItem} using the default graph.
	 * Every {@link RankItem} needs a reference to its parent.
	 * @param parent	the parent of this {@link RankItem}
	 */
	public RankItem(ModelItem parent) {
		super(parent);

		bindLocalToRoot(parent.localToRootProperty());
	}

	private void load() {
		if (getChildItems().size() == 0) {
			int rank = (int) localToRoot(new Point2D(0, 0)).getX() / RANK_WIDTH;

			List<SequenceNode> nodes = getGraph().getRank(rank);
			for (int i = 0; i < nodes.size(); i++) {
				DrawableNode drawable = new DrawableNode(this, nodes.get(i));
				drawable.setTranslateY(i * RANK_WIDTH); // - nodes.size() * RANK_WIDTH / 2);

				getChildItems().add(drawable);
				getNodes().put(nodes.get(i).getId(), drawable);
			}

			System.out.println("size: " + getNodes().size());
			System.out.println("sequence children " + rank + ": " + getChildItems().size());
		}
	}

	@Override
	public void update(Bounds b) {
		if (!isInViewport(b)) {
			return;
		}

		load();
		update(b, Thresholds.CLUSTER);
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
