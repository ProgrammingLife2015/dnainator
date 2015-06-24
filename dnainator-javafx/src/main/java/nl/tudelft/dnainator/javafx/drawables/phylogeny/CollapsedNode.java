package nl.tudelft.dnainator.javafx.drawables.phylogeny;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Represents an {@link InternalNode} that has been collapsed.
 */
public class CollapsedNode extends AbstractNode {
	private InternalNode wrappedNode;

	/**
	 * Create a new collapsed node.
	 * @param wrapped the node that has been collapsed.
	 */
	public CollapsedNode(InternalNode wrapped) {
		wrappedNode = wrapped;
		wrappedNode.getChildren().forEach(child -> child.setVisible(false));
		wrappedNode.getChildren().add(this);
		// Bind the margin of the wrapped node to this margin, so that the layout adapts.
		wrappedNode.marginProperty().bind(this.marginProperty());

		Text label = new Text(LeafNode.LABEL_X_OFFSET, LeafNode.LABEL_Y_OFFSET, "");
		label.textProperty().bind(wrapped.leafCountProperty().asString().concat(" strains"));
		label.onMouseClickedProperty().bind(shape.onMouseClickedProperty());
		label.setTextAlignment(TextAlignment.CENTER);
		getChildren().add(label);

		// Make it the size of a leaf.
		this.marginProperty().set(LeafNode.LEAFHEIGHT);
	}

	@Override
	public Shape getShape() {
		return new Circle(0, 0, DIM / 2);
	}

	@Override
	public void onMouseClicked() {
		wrappedNode.getChildren().remove(this);
		wrappedNode.getChildren().forEach(child -> child.setVisible(true));
		wrappedNode.marginProperty().unbind();
		// Rebind the margins, so that the layout adapts.
		wrappedNode.bindMargins();
	}
}
