package nl.tudelft.dnainator.ui.widgets.animations;

import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * This class realises a sliding animation.
 * It may slide up, down left or right.
 */
public class SlidingAnimation extends TransitionAnimation {

	private double curWidth;
	
	/**
	 * Creates the sliding animation, using the provided parameters for the duration, amount to
	 * slide, and the location of the animation.
	 *
	 * @param pane         The {@link Pane} to be animated.
	 * @param width        The length over which the pane will slide.
	 * @param duration     The duration of the animations.
	 * @param direction    The {@link SlidingAnimation.Location} of the {@link Pane}.
	 */
	public SlidingAnimation(Pane pane, int width, int duration, Direction direction) {
		super(pane, width, duration, direction);
	}
	
	/**
	 * Creates a sliding animation with default {@link Direction#RIGHT}.
	 * @param pane         The {@link Pane} to be animated.
	 * @param width        The length over which the pane will slide.
	 * @param duration     The duration of the animations.
	 */
	public SlidingAnimation(Pane pane, int width, int duration) {
		this(pane, width, duration, Direction.RIGHT);
	}
	
	
	@Override
	public void setupAnimation() {
		curWidth = width;
		setCycleDuration(Duration.millis(duration));
	}

	@Override
	protected void interpolate(double frac) {
		switch (direction) {
			case UP : 
				//TODO up animation.
				return;
			case DOWN :
				//TODO down animation.
				return;
			case LEFT : 
				curWidth = width * (1.0 - frac);
				pane.setPrefWidth(curWidth);
				pane.setTranslateX(curWidth - width);
				this.setOnFinished(actionEvent -> pane.setVisible(false));
				return;
			case RIGHT :
				curWidth = width * frac;
				pane.setPrefWidth(curWidth);
				pane.setTranslateX(curWidth - width);
				this.setOnFinished(actionEvent -> pane.setVisible(true));
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
			if (pane.isVisible()) {
				direction = Direction.LEFT;
			} else {
				pane.setVisible(true);
				direction = Direction.RIGHT;
			}
			this.play();
		}
	}
}
