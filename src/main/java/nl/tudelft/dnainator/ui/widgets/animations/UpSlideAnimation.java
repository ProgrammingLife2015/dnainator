package nl.tudelft.dnainator.ui.widgets.animations;

import javafx.scene.layout.Pane;

/**
 * The {@link UpSlideAnimation} is a {@link SlidingAnimation} that transitions upwards.
 */
public class UpSlideAnimation extends SlidingAnimation {

	/**
	 * Construct a new {@link UpSlideAnimation}, which will animate a sliding
	 * transition to upwards.
	 * @param pane         The {@link Pane} to be animated.
	 * @param size         The size over which the animation will occur.
	 * @param duration     The duration of the animations.
	 * @param pos          The position of the {@link Pane}.
	 */
	public UpSlideAnimation(Pane pane, double size, double duration, Position pos) {
		super(pane, size, duration, pos);
	}

	@Override
	public void setCurSize(double frac) {
		if (pos == Position.TOP) {
			newSize = size * (1.0 - frac);
		} else if (pos == Position.BOTTOM) {
			newSize = size * frac;
		}
	}
}
