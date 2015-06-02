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
	 */
	public UpSlideAnimation(Pane pane, double size, double duration) {
		super(pane, size, duration);
	}

	@Override
	protected void interpolate(double frac) {
		curSize = size * frac;
		pane.setPrefHeight(curSize);
		pane.setTranslateY(curSize - size);
		this.setOnFinished(actionEvent -> pane.setVisible(true));
	}

	@Override
	public DirectionAnimation opposite() {
		return new DownSlideAnimation(pane, size, duration);
	}
}
