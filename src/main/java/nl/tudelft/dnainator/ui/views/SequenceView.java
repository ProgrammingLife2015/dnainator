package nl.tudelft.dnainator.ui.views;

import javafx.scene.transform.Scale;
import nl.tudelft.dnainator.ui.models.GraphItem;
import nl.tudelft.dnainator.ui.models.GraphModel;
import nl.tudelft.dnainator.ui.models.ModelItem;

/**
 * The SequenceView views the data from the sequence height, i.e.
 * it displays the sequences of the DNA.
 */
public class SequenceView extends View {
	private Scale scale;
	
	/**
	 * Instantiates a new SequenceView object.
	 * @param model The {@link GraphModel} whose data to display.
	 */
	public SequenceView(GraphModel model) {
		super(model);
		scale = new Scale();

		ModelItem mi = new GraphItem();
		mi.setTranslateX(400);
		mi.setTranslateY(400);
		mi.getTransforms().add(scale);
		
		setOnScroll(ev -> {
			scale.setX(scale.getX() + (scale.getX() * ev.getDeltaY() / 1000));
			scale.setY(scale.getY() + (scale.getX() * ev.getDeltaY() / 1000));
			System.out.println("scale: " + scale);
			System.out.println("view:  " + getLayoutBounds());
			mi.update(getLayoutBounds());
		});
		
		getChildren().add(mi);
		getStyleClass().add("sequence-view");
	}
}
