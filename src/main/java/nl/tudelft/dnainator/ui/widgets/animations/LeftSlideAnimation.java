package nl.tudelft.dnainator.ui.widgets.animations;

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
	 */
	public LeftSlideAnimation(Pane pane, double size, double duration) {
		super(pane, size, duration);
	}

	@Override
	protected void interpolate(double frac) {
		curSize = size * frac;
		pane.setPrefWidth(curSize);
		pane.setTranslateX(curSize - size);
		this.setOnFinished(actionEvent -> pane.setVisible(true));
	}
	
	@Override
	public DirectionAnimation opposite() {
		return new RightSlideAnimation(pane, size, duration);
	}
}
