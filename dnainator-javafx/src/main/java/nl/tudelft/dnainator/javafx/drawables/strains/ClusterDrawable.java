package nl.tudelft.dnainator.javafx.drawables.strains;

import nl.tudelft.dnainator.core.EnrichedSequenceNode;
import nl.tudelft.dnainator.core.PropertyType;
import nl.tudelft.dnainator.core.SequenceNode;
import nl.tudelft.dnainator.core.impl.Cluster;
import nl.tudelft.dnainator.javafx.ColorServer;
import nl.tudelft.dnainator.javafx.drawables.Drawable;
import nl.tudelft.dnainator.javafx.views.AbstractView;
import nl.tudelft.dnainator.javafx.widgets.Propertyable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.MapChangeListener;
import javafx.scene.Group;
import javafx.scene.shape.Circle;

/**
 * This enum represents all properties a Cluster can have.
 */
enum ClusterPropertyTypes implements PropertyType {
	TITLE("Cluster"),
	ID("NodeID"),
	SEQUENCE("Sequence"),
	STARTREF("Start base on ref. strain"),
	ENDREF("End base on ref. strain"),
	SOURCES("Sources"),
	STARTRANK("Start rank"),
	BASEDIST("Start base"),
	SCORE("Maximum interestingness score");

	private String description;
	private ClusterPropertyTypes(String description) {
		this.description = description;
	}

	@Override
	public String description() {
		return description;
	}
}

/**
 * The {@link ClusterDrawable} class represents the mid level object in the viewable model.
 */
public class ClusterDrawable extends Group implements Drawable, Propertyable {
	protected static final int SINGLE = 1;
	protected static final int SMALL = 3;
	protected static final int MEDIUM = 10;
	protected static final double SINGLE_RADIUS = 2;
	protected static final double SMALL_RADIUS = 4;
	protected static final double MEDIUM_RADIUS = 5;
	protected static final double LARGE_RADIUS = 6;
	protected static final int PIETHRESHOLD = 20;
	private static final int INTERESTINGNESS_THRESHOLD = 600;
	private Cluster cluster;
	private Set<String> sources;
	private Pie pie;
	private Map<PropertyType, String> properties;
	private int interestingness;

	/**
	 * Construct a new mid level {@link ClusterDrawable} using the default graph.
	 * @param colorServer the {@link ColorServer} to bind to.
	 * @param cluster	the {@link Cluster} containing a list of {@link SequenceNode}s.
	 */
	public ClusterDrawable(ColorServer colorServer, Cluster cluster) {
		this.cluster = cluster;
		this.properties = new LinkedHashMap<>();
		this.sources = cluster.getNodes().stream()
				.flatMap(e -> e.getSources().stream())
				.collect(Collectors.toSet());
		this.interestingness = cluster.getNodes().stream()
				.mapToInt(e -> e.getInterestingnessScore())
				.max().getAsInt();
		setOnMouseClicked(e -> AbstractView.setLastClicked(this));
		draw(colorServer);
	}

	/**
	 * Add all properties of a cluster to the property pane.
	 */
	private void initProperties() {
		properties.put(ClusterPropertyTypes.TITLE, cluster.getNodes().size() + " nodes");
		properties.put(ClusterPropertyTypes.ID, cluster.getNodes().stream()
								.map(e -> e.getId())
								.collect(Collectors.toList()).toString());
		properties.put(ClusterPropertyTypes.STARTRANK, Integer.toString(cluster.getStartRank()));
		properties.put(ClusterPropertyTypes.SOURCES, cluster.getNodes().stream()
								.flatMap(e -> e.getSources().stream())
								.collect(Collectors.toList()).toString());
		properties.put(ClusterPropertyTypes.SCORE, Integer.toString(interestingness));
		if (cluster.getNodes().size() == 1) {
			initSingletonProperties();
		}
	}

	/**
	 * Add all properties of a single sequence node to the property pane.
	 */
	private void initSingletonProperties() {
		EnrichedSequenceNode sn = cluster.getNodes().iterator().next();
		String score = properties.remove(ClusterPropertyTypes.SCORE);
		properties.put(ClusterPropertyTypes.BASEDIST, Integer.toString(sn.getBaseDistance()));
		properties.put(ClusterPropertyTypes.STARTREF, Integer.toString(sn.getStartRef()));
		properties.put(ClusterPropertyTypes.ENDREF, Integer.toString(sn.getEndRef()));
		properties.put(ClusterPropertyTypes.SEQUENCE, sn.getSequence());
		properties.put(ClusterPropertyTypes.SCORE, score);

		sn.getScores().forEach((k, v) -> properties.put(k, Integer.toString(v)));
	}

	private void draw(ColorServer colorServer) {
		double radius = getRadius();

		Circle commonNode = new Circle(radius);
		getChildren().add(commonNode);

		if (sources.size() > PIETHRESHOLD) {
			commonNode.getStyleClass().add("common-node");
		} else {
			colorServer.addListener(this::onColorServerChanged);

			List<String> collect = sources.stream()
					.map(e -> colorServer.getColor(e))
					.filter(e -> e != null)
					.collect(Collectors.toList());
			pie = new Pie(radius, collect);
			getChildren().add(pie);
		}
		if (interestingness > INTERESTINGNESS_THRESHOLD) {
			commonNode.getStyleClass().add("interesting-node");
		}
	}

	/**
	 * Return the radius of this cluster drawable.
	 * @return	the radius
	 */
	protected double getRadius() {
		int nChildren = cluster.getNodes().size();

		if (nChildren == SINGLE) {
			return SINGLE_RADIUS;
		} else if (nChildren <= SMALL) {
			return SMALL_RADIUS;
		} else if (nChildren <= MEDIUM) {
			return MEDIUM_RADIUS;
		} else {
			return LARGE_RADIUS;
		}
	}

	private void onColorServerChanged(
			MapChangeListener.Change<? extends String, ? extends String> change) {
		if (!sources.contains(change.getKey())) {
			return;
		} else if (change.wasAdded()) {
			addStyle(change.getValueAdded());
		} else if (change.wasRemoved()) {
			removeStyle(change.getValueRemoved());
		}
	}

	@Override
	public void addStyle(String style) {
		pie.getStyles().add(style);
	}

	@Override
	public void removeStyle(String style) {
		pie.getStyles().remove(style);
	}

	/**
	 * @return the {@link Cluster}s in this drawable.
	 */
	public Cluster getCluster() {
		return cluster;
	}

	@Override
	public Map<PropertyType, String> getPropertyMap() {
		if (properties.size() == 0) {
			initProperties();
		}
		return properties;
	}
}
