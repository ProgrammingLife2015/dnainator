package nl.tudelft.dnainator.ui.widgets.animations;

import javafx.scene.layout.Pane;

/**
 * This abstract class represents any transisting animation with a direction.
 */
public abstract class DirectionAnimation extends TransitionAnimation {
	
	protected double newSize;
	
	/**
	 * Constructs a new animation with direction.
	 * @param pane         The {@link Pane} to be animated.
	 * @param size         The size over which the animation will occur.
	 * @param duration     The duration of the animations.
	 * @param pos          The position of the {@link Pane}.
	 */
	public DirectionAnimation(Pane pane, double size, double duration, Position pos) {
		super(pane, size, duration, pos);
	}
	
	/**
	 * @return the animation in the opposite direction.
	 */
	public abstract DirectionAnimation opposite();
	
	/**
	 * @param frac a fraction (that goes from 0.0 to 1.0).
	 * @return the newly computed pane size for the transition.
	 */
	public abstract double getCurSize(double frac);
	
	/**
	 * Does the sliding animation, through translations and width changing of the {@link Pane}.
	 * @param move the size of the movement.
	 */
	public void setMovement(double move) {
		if (pos == Position.LEFT) {
			pane.setPrefWidth(newSize);
			pane.setTranslateX(newSize - size);
		} else if (pos == Position.RIGHT) {
			pane.setPrefWidth(newSize);
			pane.setTranslateX(size - newSize);
		}
	}
}