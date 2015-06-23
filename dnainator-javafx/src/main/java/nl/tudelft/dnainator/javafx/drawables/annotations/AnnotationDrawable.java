package nl.tudelft.dnainator.javafx.drawables.annotations;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.dnainator.annotation.Annotation;
import nl.tudelft.dnainator.javafx.drawables.Drawable;

/**
 * An implementation of {@link Drawable} to display a gene from an {@link Annotation}.
 */
public class AnnotationDrawable extends VBox implements Drawable {

	/**
	 * Instantiates a new {@link AnnotationDrawable}.
	 * @param annotation The {@link Annotation} to display.
	 */
	public AnnotationDrawable(Annotation annotation) {
		draw(annotation);
	}

	private void draw(Annotation annotation) {
		Text name = new Text(annotation.getGeneName());
		Tooltip tooltip = new Tooltip("Coordinates: " + annotation.getStart() + " to "
				+ annotation.getEnd() + "\n" + "Sense: " + annotation.isSense());
		Tooltip.install(name, tooltip);
		getChildren().add(name);
		if (annotation.isMutation()) {
			getStyleClass().add("dr-mutation");
		} else {
			getStyleClass().add("annotation");
		}
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
