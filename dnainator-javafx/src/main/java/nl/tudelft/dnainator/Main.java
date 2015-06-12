package nl.tudelft.dnainator;

import javafx.application.Application;
import nl.tudelft.dnainator.javafx.DNAinator;

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
		Application.launch(DNAinator.class, args);
	}
}
