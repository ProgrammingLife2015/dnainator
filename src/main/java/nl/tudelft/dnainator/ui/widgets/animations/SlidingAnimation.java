package nl.tudelft.dnainator.ui.widgets.animations;

import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * This class realises a sliding animation.
 * It may slide up, down left or right.
 */
public class SlidingAnimation extends TransitionAnimation {
	
	private double curWidth;
	private double curHeight;
	
	/**
	 * Creates the sliding animation, using the provided parameters for the duration, amount to
	 * slide, and the location of the animation.
	 *
	 * @param pane         The {@link Pane} to be animated.
	 * @param width        The horizontal length over which the pane will slide.
	 * @param height       The vertical length over which the pane will slide.
	 * @param duration     The duration of the animations.
	 * @param direction    The {@link TransitionAnimation.Direction} of the sliding animation.
	 */
	public SlidingAnimation(Pane pane, int width, int height, int duration, Direction direction) {
		super(pane, width, height, duration, direction);
	}
	
	/**
	 * Creates a sliding animation with default {@link Direction#LEFT}.
	 * @param pane         The {@link Pane} to be animated.
	 * @param width        The length over which the pane will slide.
	 * @param height       The vertical length over which the pane will slide.
	 * @param duration     The duration of the animations.
	 */
	public SlidingAnimation(Pane pane, int width, int height, int duration) {
		this(pane, width, height, duration, Direction.LEFT);
	}
	
	
	@Override
	public void setupAnimation() {
		curWidth = width;
		curHeight = height;
		setCycleDuration(Duration.millis(duration));
	}

	@Override
	protected void interpolate(double frac) {
		switch (direction) {
			case UP : 
				curHeight = height * frac;
				pane.setPrefHeight(curHeight);
				pane.setTranslateY(curHeight - height);
				this.setOnFinished(actionEvent -> pane.setVisible(true));
				return;
			case DOWN :
				curHeight = height * (1.0 - frac);
				pane.setPrefHeight(curHeight);
				pane.setTranslateY(curHeight - height);
				this.setOnFinished(actionEvent -> pane.setVisible(false));
				return;
			case LEFT : 
				curWidth = width * frac;
				pane.setPrefWidth(curWidth);
				pane.setTranslateX(curWidth - width);
				this.setOnFinished(actionEvent -> pane.setVisible(true));
				return;
			case RIGHT :
				curWidth = width * (1.0 - frac);
				pane.setPrefWidth(curWidth);
				pane.setTranslateX(curWidth - width);
				this.setOnFinished(actionEvent -> pane.setVisible(false));
				return;
			default :
				return;
		}
	}
	
	/**
	 * Toggles the animation, i.e. if the box is currently
	 * shown it will be hidden and vice-versa.
	 */
	public void toggle() {
		if (this.getStatus() == Status.STOPPED) {
			pane.setVisible(true);
			switch (direction) {
				case UP : 
					direction = Direction.DOWN;
					this.play();
					return;
				case DOWN :
					direction = Direction.UP;
					this.play();
					return;
				case LEFT : 
					direction = Direction.RIGHT;
					this.play();
					return;
				case RIGHT :
					direction = Direction.LEFT;
					this.play();
					return;
				default :
					return;
			}
		}
	}
}
