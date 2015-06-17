package nl.tudelft.dnainator.javafx.widgets.animations;

import javafx.scene.layout.Pane;

/**
 * The {@link LeftSlideAnimation} is a {@link SlidingAnimation} that transitions to the left.
 */
public class LeftSlideAnimation extends SlidingAnimation {

	/**
	 * Construct a new {@link LeftSlideAnimation}, which will animate a sliding
	 * transition to the left.
	 * @param pane         The {@link Pane} to be animated.
	 * @param size         The size over which the animation will occur.
	 * @param duration     The duration of the animations.
	 * @param pos          The position of the {@link Pane}.
	 */
	public LeftSlideAnimation(Pane pane, double size, double duration, Position pos) {
		super(pane, size, duration, pos);
	}

	@Override
	public void setCurSize(double frac) {
		if (pos == Position.LEFT) {
			newSize = size * (1.0 - frac);
		} else if (pos == Position.RIGHT) {
			newSize = size * frac;
		}
	}
	
	@Override
	public void setVisibility(Pane pane, Position pos) {
		if (pos == Position.LEFT && getRate() > 0) {
			pane.setVisible(false);
		} else if (pos == Position.RIGHT && getRate() < 0) {
			pane.setVisible(false);
		}
	}
}
