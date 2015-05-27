package nl.tudelft.dnainator.ui.widgets.animations;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * This class realises a sliding animation, to show and hide a pane and its contents.
 * TODO: extend for multiple directions.
 */
public class SlidingAnimation {
//	/**
//	 * An enum that specifies the location of the {@link Pane} to be animated. Using this
//	 * location, the translations can be set correctly.
//	 * TODO: add more directions.
//	 */
//	public enum Location {
//		TOP_LEFT
//	}

	private Pane pane;
//	private Location location;
	private Animation show;
	private Animation hide;
	private int width;
	private int duration;

	/**
	 * Creates the sliding animation, using the provided parameters for the duration, amount to
	 * slide, and the location of the animation.
	 *
	 * @param pane         The {@link Pane} to be animated.
	 * @param width        The length over which the pane will slide.
	 * @param duration     The duration of the animations.
//	  param paneLocation The {@link SlidingAnimation.Location} of the {@link Pane}.
	 */
	public SlidingAnimation(Pane pane, int width, int duration/*, Location paneLocation*/) {
		this.pane = pane;
		this.width = width;
		this.duration = duration;
//		this.location = paneLocation;

		setupShowAnimation();
		setupHideAnimation();
	}

	/**
	 * Hides the pane in a sliding fashion.
	 */
	private void setupHideAnimation() {
		hide = new Transition() {
			{
				setCycleDuration(Duration.millis(duration));
			}

			@Override
			protected void interpolate(double frac) {
				double curWidth = width * (1.0 - frac);
				pane.setPrefWidth(curWidth);
				pane.setTranslateX(curWidth - width);
			}
		};
		hide.onFinishedProperty().set(actionEvent -> pane.setVisible(false));
	}

	/**
	 * Shows the pane in a sliding fashion.
	 */
	private void setupShowAnimation() {
		show = new Transition() {
			{
				setCycleDuration(Duration.millis(duration));
			}

			@Override
			protected void interpolate(double frac) {
				double curWidth = width * frac;
				pane.setPrefWidth(curWidth);
				pane.setTranslateX(curWidth - width);
			}
		};
	}

	/**
	 * Toggles the animation, i.e. if the box is currently
	 * shown it will be hidden and vice-versa.
	 */
	public void toggle() {
		if (show.statusProperty().get() == Animation.Status.STOPPED
				&& hide.statusProperty().get() == Animation.Status.STOPPED) {
			if (pane.isVisible()) {
				hide.play();
			} else {
				pane.setVisible(true);
				show.play();
			}
		}
	}
}
