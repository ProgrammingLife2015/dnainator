package nl.tudelft.dnainator.ui.widgets.animations;

import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * The {@link SlidingAnimation} class realises a sliding animation.
 * The animation is a {@link DirectionAnimation} in the sense that it may,
 * slide either in a upward, downward or sideway fashion.
 */
public abstract class SlidingAnimation extends DirectionAnimation {
	
	private DirectionAnimation da;
	protected double curSize;
	
	/**
	 * Creates the sliding animation, using the provided parameters for the duration, amount to
	 * slide, and the location of the animation.
	 * @param pane         The {@link Pane} to be animated.
	 * @param size         The size over which the animation will occur.
	 * @param duration     The duration of the animation.
	 */
	public SlidingAnimation(Pane pane, double size, double duration) {
		super(pane, size, duration);
	}
	
	@Override
	public void setupAnimation() {
		da = this;
		curSize = size;
		setCycleDuration(Duration.millis(duration));
	}
	
	/**
	 * Toggles the animation, i.e. if the box is currently
	 * shown it will be hidden and vice-versa.
	 */
	public void toggle() {
		if (this.getStatus() == Status.STOPPED) {
			pane.setVisible(true);
			da = da.opposite();
			da.play();
		}
	}
}
