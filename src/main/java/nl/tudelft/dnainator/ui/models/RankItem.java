package nl.tudelft.dnainator.ui.models;

import java.util.List;

import javafx.geometry.Bounds;
import nl.tudelft.dnainator.core.impl.Cluster;

/**
 * The {@link RankItem} class represents the bottom level object in the viewable model.
 * It can hold only content and no children, and is therefore a leaf in the composite pattern.
 */
public class RankItem extends CompositeItem {
	private List<Cluster> clusters;

	/**
	 * Construct a new bottom level {@link RankItem} using the default graph.
	 * Every {@link RankItem} needs a reference to its parent.
	 * @param parent	the parent of this {@link RankItem}
	 * @param rank		the rank of this {@link RankItem}
	 * @param clusters	the clusters this rankitem contains.
	 */
	public RankItem(ModelItem parent, int rank, List<Cluster> clusters) {
		super(parent, rank);
		
		this.clusters = clusters;
		getContent().setTranslateX(rank * RANK_WIDTH);
	}

	private void load() {
		System.out.println("loading: " + getRank());
		if (getChildItems().size() == 0) {
			for (int i = 0; i < clusters.size(); i++) {
				ClusterItem c = new ClusterItem(this, getRank(), clusters.get(i).getNodes());
				c.setTranslateX(getRank() * RANK_WIDTH);
				c.setTranslateY(i * RANK_WIDTH - clusters.size() * RANK_WIDTH / 2);
				getChildItems().add(c);
			}
		}
	}

	@Override
	public void update(Bounds b) {
		if (!isInViewport(b)) {
			return;
		}

		load();
		update(b, Thresholds.GRAPH);
	}
}
