package nl.tudelft.dnainator.ui.actions;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A Swing Action to open the "Open File" dialog.
 */
public class OpenAction extends AbstractAction {
	private static final long serialVersionUID = -3074966646187196018L;
	private static final String LABEL = "Open...";
	private static final String TOOLTIP = "Open a new graph";
	private static final char ACCELERATOR = 'O';
	private Component parent;

	/**
	 * Construct an OpenAction object.
	 * @param parent The parent Component to map the Open File dialog on.
	 */
	public OpenAction(Component parent) {
		super(LABEL);
		putValue(SHORT_DESCRIPTION, TOOLTIP);
		putValue(MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_O));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(ACCELERATOR,
		          Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Graphs", "node.graph", "edge.graph"));

		if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			System.out.println(file.getPath());
		}
	}
}
