package nl.tudelft.dnainator.javafx.drawables.annotations;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.shape.Line;
import nl.tudelft.dnainator.javafx.drawables.strains.ClusterDrawable;

/**
 * The {@link Connection} is a {@link Line} that connects a {@link Gene} to a
 * {@link ClusterDrawable}.
 */
public class Connection extends Line {

	/**
	 * Instantiates a new {@link Connection}.
	 * @param srcX The {@link DoubleBinding} to bind the start x-coordinate to.
	 * @param srcY The {@link DoubleBinding} to bind the start y-coordinate to.
	 * @param dstX The {@link DoubleBinding} to bind the end x-coordinate to.
	 * @param dstY The {@link DoubleBinding} to bind the end y-coordinate to.
	 */
	public Connection(DoubleBinding srcX, DoubleBinding srcY,
	                  DoubleBinding dstX, DoubleBinding dstY) {
		startXProperty().bind(srcX);
		startYProperty().bind(srcY);
		endXProperty().bind(dstX);
		endYProperty().bind(dstY);
	}
}
