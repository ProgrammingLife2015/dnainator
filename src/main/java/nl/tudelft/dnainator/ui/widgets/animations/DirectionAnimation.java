package nl.tudelft.dnainator.ui.widgets.animations;

import javafx.scene.layout.Pane;

/**
 * This abstract class represents any transisting animation with a direction.
 */
public abstract class DirectionAnimation extends TransitionAnimation {

	/**
	 * Constructs a new animation with direction.
	 * @param pane         The {@link Pane} to be animated.
	 * @param size         The size over which the animation will occur.
	 * @param duration     The duration of the animations.
	 */
	public DirectionAnimation(Pane pane, double size, double duration) {
		super(pane, size, duration);
	}
	
	/**
	 * @return the animation in the opposite direction.
	 */
	public abstract DirectionAnimation opposite();
}