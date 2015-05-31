package nl.tudelft.dnainator.ui.widgets.animations;

import nl.tudelft.dnainator.ui.widgets.animations.Direction;
import javafx.animation.Transition;
import javafx.scene.layout.Pane;

/**
 * A {@link CustomAnimation} which is toggleable.
 * It applies the animation onto a {@link Pane}.
 */
public abstract class TransitionAnimation extends Transition implements CustomAnimation {
	
	protected Pane pane;
	protected int width;
	protected int duration;
	protected Direction direction;

	/**
	 * Construct a new {@link TransitionAnimation}.
	 * @param pane         The {@link Pane} which is animated.
	 * @param width        The length over which the pane will slide.
	 * @param duration     The duration of the animations.
	 * @param direction    The {@link SlidingAnimation.Location} of the {@link Pane}.
	 */
	public TransitionAnimation(Pane pane, int width, int duration, Direction direction) {
		this.pane = pane;
		this.width = width;
		this.duration = duration;
		this.direction = direction;
		
		setupAnimation();
	}
	
	/**
	 * Plays the {@link TransitionAnimation}.
	 */
	@Override
	public void playAnimation() {
		this.play();
	}
	
	/**
	 * Stops the {@link TransitionAnimation}.
	 */
	@Override
	public void stopAnimation() {
		this.stop();
	}
}
