package nl.tudelft.dnainator.ui.widgets.animations;

import javafx.scene.layout.Pane;

/**
 * The {@link RightSlideAnimation} is a {@link SlidingAnimation} that transitions to the right.
 */
public class RightSlideAnimation extends SlidingAnimation {

	/**
	 * Construct a new {@link RightSlideAnimation}, which will animate a sliding
	 * transition to the right.
	 * @param pane         The {@link Pane} to be animated.
	 * @param size         The size over which the animation will occur.
	 * @param duration     The duration of the animation.
	 * @param pos          The position of the {@link Pane}.
	 */
	public RightSlideAnimation(Pane pane, double size, double duration, Position pos) {
		super(pane, size, duration, pos);
	}
	
	@Override
	public DirectionAnimation opposite() {
		return new LeftSlideAnimation(pane, size, duration, pos);
	}

	@Override
	public double getCurSize(double frac) {
		if (pos == Position.LEFT) {
			newSize = size * frac;
		} else if (pos == Position.RIGHT) {
			newSize = size * (1.0 - frac);
		}
		return newSize;
	}
}
