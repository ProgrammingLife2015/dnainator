package nl.tudelft.dnainator.javafx.drawables.strains;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Group;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;

import java.util.List;

/**
 * This class represents a JavaFX drawable that will display a list of styles as a piechart.
 * Styles can be added and removed, which will trigger redrawing of the chart.
 */
public class Pie extends Group {
	private static final int FULLCIRCLE = 360;
	private ObservableSet<String> styles;
	private Group slices;
	private double radius;

	/**
	 * Construct a new Pie using the specified radius and styles.
	 * @param radius	the radius
	 * @param styles	the list of styles (data)
	 */
	public Pie(double radius, List<String> styles) {
		this.radius = radius;
		this.slices = new Group();
		this.styles = FXCollections.observableSet();
		this.styles.addListener(this::onChange);

		getChildren().addAll(new Circle(radius), slices);
		styles.forEach(e -> this.styles.add(e));
	}

	/**
	 * Return the list of styles.
	 * @return	the styles
	 */
	public ObservableSet<String> getStyles() {
		return styles;
	}

	// The parameter is needed to distinguish what type of listener has to be added,
	// so suppress it for PMD et al.
	@SuppressWarnings("unused")
	private void onChange(SetChangeListener.Change<? extends String> change) {
		slices.getChildren().clear();
		if (styles.isEmpty()) {
			return;
		}

		int start = 0;
		int delta = FULLCIRCLE / styles.size();
		for (String style : styles) {
			Arc arc = new Arc(0, 0, radius, radius, start, delta);
			arc.setType(ArcType.ROUND);
			arc.getStyleClass().add(style);
			slices.getChildren().add(arc);

			start += delta;
		}
	}
}
