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
	 */
	public RightSlideAnimation(Pane pane, double size, double duration) {
		super(pane, size, duration);
	}

	@Override
	protected void interpolate(double frac) {
		curSize = size * (1.0 - frac);
		pane.setPrefWidth(curSize);
		pane.setTranslateX(curSize - size);
		this.setOnFinished(actionEvent -> pane.setVisible(true));
	}
	
	@Override
	public DirectionAnimation opposite() {
		return new LeftSlideAnimation(pane, size, duration);
	}
}
