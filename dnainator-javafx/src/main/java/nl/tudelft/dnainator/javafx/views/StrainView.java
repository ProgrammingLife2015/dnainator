package nl.tudelft.dnainator.javafx.views;

import javafx.geometry.Point2D;
import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.drawables.strains.ClusterDrawable;
import nl.tudelft.dnainator.javafx.drawables.strains.Strain;
import nl.tudelft.dnainator.javafx.widgets.Minimap;
import nl.tudelft.dnainator.javafx.widgets.StrainControl;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * An implementation of {@link AbstractView} for displaying DNA strains.
 */
public class StrainView extends AbstractView {
	private static final int GENE_LENGTH = 5;
	private Graph graph;
	private Strain strain;
	private StrainControl control;

	/**
	 * Creates a new strain view instance.
	 * @param colorServer The {@link ColorServer} to communicate with.
	 * @param graph The Graph that holds the strains.
	 */
	public StrainView(ColorServer colorServer, Graph graph) {
		super();
		this.graph = graph;
		this.strain = new Strain(colorServer, graph);

		setTransforms(strain);
		getChildren().addAll(strain, setupStrainControl(), setupMinimap(strain, graph));
		updateStrain();
	}

	private Minimap setupMinimap(Strain strain, Graph graph) {
		Minimap minimap = new Minimap(strain, graph, this);
		minimap.translateXProperty().bind(translateXProperty());
		minimap.translateYProperty().bind(heightProperty().subtract(minimap.heightProperty()));
		widthProperty().addListener((obj, oldV, newV) -> minimap.setPrefWidth(newV.doubleValue()));
		return minimap;
	}

	private StrainControl setupStrainControl() {
		control = new StrainControl(this);
		control.translateXProperty().bind(widthProperty().subtract(control.widthProperty()));
		return control;
	}
	
	private void updateStrain() {
		strain.update(cameraToWorld(getLayoutBounds()), scale.getMxx());
	}

	@Override
	public void pan(Point2D delta) {
		super.pan(delta);
		updateStrain();
	}

	private void setPan(double x, double y) {
		scale.setTx(0);
		scale.setTy(0);
		translate.setX(x * scale.getMxx());
		translate.setY(y * scale.getMxx());
		updateStrain();
	}

	@Override
	public void zoom(double delta, Point2D center) {
		super.zoom(delta, center);
		updateStrain();
	}

	@Override
	public void resetZoom() {
		super.resetZoom();
		updateStrain();
	}

	@Override
	public void resetTranslate() {
		super.resetTranslate();
		updateStrain();
	}

	/**
	 * Sets the panning to a specific rank in the {@link Strain}.
	 * @param rank The rank to pan to.
	 */
	public void gotoRank(int rank) {
		if (rank >= 0 && rank <= graph.getMaxRank()) {
			setPan(-rank * strain.getRankWidth(), 0);
		}
	}

	/**
	 * Sets the panning to a specific {@link EnrichedSequenceNode} in the {@link Strain}.
	 * This is different from gotoRank in that this method specifically centers the node in the
	 * view.
	 * @param id The ID of the {@link EnrichedSequenceNode} to go to.
	 */
	public void gotoNode(String id) {
		EnrichedSequenceNode node = graph.getNode(id);
		if (node != null) {
			setPan(-node.getRank() * strain.getRankWidth(), 0);
			ClusterDrawable cluster = strain.getClusters().get(id);
			// This has to be done separately to first pan to where the sequence node should be
			// in order for it to get loaded and drawn with a cluster drawable.
			if (cluster != null) {
					setPan(0, cluster.getTranslateY() * strain.getRankWidth());
			}
		}
	}

	/**
	 * @param geneName The name of the gene whose {@link EnrichedSequenceNode}s' IDs to find.
	 * @return The IDs of the {@link EnrichedSequenceNode}s beloning to the given gene, or null if
	 * none are found.
	 */
	public Collection<String> getAnnotatedNodeIDs(String geneName) {
		try {
			Annotation annotation = graph.getAnnotations().getAll().stream()
					.filter(a -> geneName.length() > GENE_LENGTH
							&& a.getGeneName().toLowerCase().contains(geneName.toLowerCase()))
					.findFirst()
					.get();
			return annotation.getAnnotatedNodes();
		} catch (NoSuchElementException nse) {
			return null;
		}
	}

	/**
	 * @return the {@link StrainControl} of the {@link StrainView}.
	 */
	public StrainControl getStrainControl() {
		return control;
	}
}
