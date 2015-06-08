package nl.tudelft.dnainator;

import com.sun.javafx.application.LauncherImpl;
import nl.tudelft.dnainator.javafx.DNAinator;
import nl.tudelft.dnainator.javafx.splashscreen.SplashScreen;

/**
 * DNAinator's main entry point.
 */
public final class Main {
	// Keep Checkstyle satisfied.
	private Main() {
	}

	/**
	 * DNAinator's entry point. Fires up a new instance
	 * of DNAinator with its own window.
	 * @param args Command-line arguments, unused for now.
	 */
	public static void main(String[] args) {
		LauncherImpl.launchApplication(DNAinator.class, SplashScreen.class, args);
	}
}
