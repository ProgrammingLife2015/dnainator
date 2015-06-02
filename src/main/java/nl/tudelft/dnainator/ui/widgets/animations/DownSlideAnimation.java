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
	 */
	public DownSlideAnimation(Pane pane, double size, double duration) {
		super(pane, size, duration);
	}

	@Override
	protected void interpolate(double frac) {
		curSize = size * (1.0 - frac);
		pane.setPrefHeight(curSize);
		pane.setTranslateY(curSize - size);
		this.setOnFinished(actionEvent -> pane.setVisible(false));
	}

	@Override
	public DirectionAnimation opposite() {
		return new UpSlideAnimation(pane, size, duration);
	}
}
