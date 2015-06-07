package nl.tudelft.dnainator.ui.widgets.animations;

import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * The {@link SlidingAnimation} class realises a sliding animation.
 * The animation is a {@link TransitionAnimation} in the sense that it may,
 * slide either in a upward, downward or sideway fashion.
 */
public abstract class SlidingAnimation extends TransitionAnimation {
	
	protected double newSize;
	
	/**
	 * Creates the sliding animation, using the provided parameters for the duration, amount to
	 * slide, and the location of the animation.
	 * @param pane         The {@link Pane} to be animated.
	 * @param size         The size over which the animation will occur.
	 * @param duration     The duration of the animation.
	 * @param pos          The position of the {@link Pane}.
	 */
	public SlidingAnimation(Pane pane, double size, double duration, Position pos) {
		super(pane, size, duration, pos);
	}
	
	@Override
	public void setupAnimation() {
		newSize = size;
		setCycleDuration(Duration.millis(duration));
	}
	
	@Override
	protected void interpolate(double frac) {
		setCurSize(frac);
		setMovement();
	}
	
	/**
	 * Set the newly computed {@link Pane} size.
	 * @param frac a fraction (that goes from 0.0 to 1.0).
	 */
	public abstract void setCurSize(double frac);
	
	/**
	 * Does the sliding animation, through translations and width changing of the {@link Pane}.
	 */
	public void setMovement() {
		if (pos == Position.LEFT) {
			pane.setPrefWidth(newSize);
			pane.setTranslateX(newSize - size);
		} else if (pos == Position.RIGHT) {
			pane.setPrefWidth(newSize);
			pane.setTranslateX(size - newSize);
		} else if (pos == Position.TOP) {
			pane.setPrefHeight(newSize);
			pane.setTranslateY(size - newSize);
		} else if (pos == Position.BOTTOM) {
			pane.setPrefHeight(newSize);
			pane.setTranslateY(newSize - size);
		}
	}
	
	/**
	 * Toggles the animation, i.e. if the box is currently
	 * shown it will be hidden and vice-versa.
	 */
	public void toggle() {
		if (this.getStatus() == Status.STOPPED) {
			pane.setVisible(true);
			this.play();
		}
	}
}
