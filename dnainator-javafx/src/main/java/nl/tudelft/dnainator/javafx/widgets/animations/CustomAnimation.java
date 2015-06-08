package nl.tudelft.dnainator.javafx.widgets.animations;

/**
 * An interface for custom animations.
 */
public interface CustomAnimation {
	
	/**
	 * Setup the animation.
	 */
	void setupAnimation();
	
	/**
	 * Play the animation.
	 */
	void playAnimation();
	
	/**
	 * Stop the animation.
	 */
	void stopAnimation();
}
