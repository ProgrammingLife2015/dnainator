package nl.tudelft.dnainator;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import nl.tudelft.dnainator.ui.Window;

/**
 * DNAinator's entry point. This class merely sets the UI's look and feel
 * and then creates a window.
 */
public final class DNAinator {

	private DNAinator() {
		super();
	}

	/**
	 * DNAinator entry point.
	 * @param args Command-line arguments, unused for now.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Be thread-safe. */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Window();
			}
		});
	}
}
