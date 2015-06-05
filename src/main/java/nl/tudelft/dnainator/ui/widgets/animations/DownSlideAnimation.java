package nl.tudelft.dnainator.ui.widgets.animations;

import javafx.scene.layout.Pane;

/**
 * The {@link DownSlideAnimation} is a {@link SlidingAnimation} that transitions downwards.
 */
public class DownSlideAnimation extends SlidingAnimation {
	
	/**
	 * Construct a new {@link DownSlideAnimation}, which will animate a sliding
	 * transition to downwards.
	 * @param pane         The {@link Pane} to be animated.
	 * @param size         The size over which the animation will occur.
	 * @param duration     The duration of the animations.
	 * @param pos          The position of the {@link Pane}.
	 */
	public DownSlideAnimation(Pane pane, double size, double duration, Position pos) {
		super(pane, size, duration, pos);
	}

	@Override
	public void setCurSize(double frac) {
		if (pos == Position.TOP) {
			newSize = size * frac;
		} else if (pos == Position.BOTTOM) {
			newSize = size * (1.0 - frac);
		}
	}
}
