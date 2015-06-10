package nl.tudelft.dnainator.javafx.drawables.annotations;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.javafx.drawables.Drawable;

/**
 * An implementation of {@link Drawable} to display a gene from an {@link Annotation}.
 */
public class Gene extends VBox implements Drawable {

	/**
	 * Instantiates a new {@link Gene}.
	 * @param annotation The {@link Annotation} to display.
	 */
	public Gene(Annotation annotation) {
		draw(annotation);
	}

	private void draw(Annotation annotation) {
		Text name = new Text(annotation.getGeneName());
		Text coordinates = new Text("Coordinates: " + annotation.getRange());
		Text sense = new Text("Sense: " + annotation.isSense());
		getChildren().addAll(name, coordinates, sense);
	}

	@Override
	public void addStyle(String style) {
		for (Node child : getChildren()) {
			child.getStyleClass().add(style);
		}
	}

	@Override
	public void removeStyle(String style) {
		for (Node child : getChildren()) {
			child.getStyleClass().remove(style);
		}
	}
}
