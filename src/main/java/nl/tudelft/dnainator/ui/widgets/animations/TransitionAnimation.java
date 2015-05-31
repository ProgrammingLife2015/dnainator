package nl.tudelft.dnainator.ui.widgets.animations;

import javafx.animation.Transition;
import javafx.scene.layout.Pane;

/**
 * A {@link CustomAnimation} which animates a transition.
 * It applies the animation onto a {@link Pane}.
 * The animation is carried out in {@link Direction}.
 */
public abstract class TransitionAnimation extends Transition implements CustomAnimation {
	
	/**
	 * An enum that indicates which direction the animation will transition to.
	 */
	public enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	
	protected Pane pane;
	protected int width;
	protected int height;
	protected int duration;
	protected Direction direction;

	/**
	 * Construct a new {@link TransitionAnimation}.
	 * @param pane         The {@link Pane} which is animated.
	 * @param width        The length over which the pane will slide.
	 * @param height       The vertical length over which the pane will slide.
	 * @param duration     The duration of the animations.
	 * @param direction    The {@link Direction} of the animation.
	 */
	public TransitionAnimation(Pane pane, int width, int height, int duration, 
			Direction direction) {
		this.pane = pane;
		this.width = width;
		this.height = height;
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
