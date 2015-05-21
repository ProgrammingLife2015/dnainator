package nl.tudelft.dnainator.ui.models;

import java.util.List;

import javafx.geometry.Bounds;
import nl.tudelft.dnainator.core.SequenceNode;

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
	 * @param rank		the rank of this {@link RankItem}
	 */
	public RankItem(ModelItem parent, int rank) {
		super(parent, rank);
		
		getContent().setTranslateX(rank * RANK_WIDTH);
	}

	private void load() {
		if (getChildItems().size() == 0) {
			List<SequenceNode> nodes = getGraph().getRank(getRank());
			for (int i = 0; i < nodes.size(); i++) {
				NodeItem drawable = new NodeItem(this, nodes.get(i));
				drawable.setTranslateX(getRank() * RANK_WIDTH);
				drawable.setTranslateY(i * RANK_WIDTH - nodes.size() * RANK_WIDTH / 2);

				getChildItems().add(drawable);
				getNodes().put(nodes.get(i).getId(), drawable);
			}
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
