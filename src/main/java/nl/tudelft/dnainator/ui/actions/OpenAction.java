package nl.tudelft.dnainator.ui.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import nl.tudelft.dnainator.ui.DNAViewer;
import nl.tudelft.dnainator.ui.Window;
import nl.tudelft.dnainator.util.FileLoader;
import nl.tudelft.dnainator.util.DoubleFileExtensionFilter;

/**
 * A Swing Action to open the "Open File" dialog.
 */
public class OpenAction extends AbstractAction {
	private static final long serialVersionUID = -3074966646187196018L;
	private static final int EXT_LENGTH = 11; // .node.graph
	private static final String EDGE = ".edge.graph";
	private static final String LABEL = "Open...";
	private static final String TOOLTIP = "Open a new graph";
	private static final char ACCELERATOR = 'O';
	private Window parent;
	private String lastDirectory;

	/**
	 * Construct an OpenAction object.
	 * @param parent The parent {@link Window} to map the Open File dialog on.
	 */
	public OpenAction(Window parent) {
		super(LABEL);
		putValue(SHORT_DESCRIPTION, TOOLTIP);
		putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_O));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ACCELERATOR,
		          Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.parent = parent;
		this.lastDirectory = null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser(lastDirectory);
		chooser.setFileFilter(new DoubleFileExtensionFilter("Graphs", "node.graph"));

		if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			File nodeFile = chooser.getSelectedFile();
			File edgeFile = openEdgeFile(nodeFile.getPath());
			lastDirectory = nodeFile.getParent();

			FileLoader loader = new FileLoader(nodeFile, edgeFile);
			loader.execute();

			try {
				DNAViewer v = new DNAViewer(loader.get());
				parent.setViewer(v);
			} catch (InterruptedException | ExecutionException e1) {
				parent.spawnErrorDialog(e1.getMessage(), "Error opening file");
			}
		}
	}

	private File openEdgeFile(String path) {
		return new File(path.substring(0, path.length() - EXT_LENGTH).concat(EDGE));
	}
}
