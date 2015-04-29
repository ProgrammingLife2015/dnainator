package nl.tudelft.dnainator.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * A Swing Action to filter what is being displayed in the graph.
 */
public class FilterAction extends AbstractAction {
	private static final long serialVersionUID = -5547537630330063687L;
	private static final String TOOLTIP = "Filter displayed information to only ";
	private String label;

	/**
	 * Construct a FilterAction object.
	 * @param label This filter's label and selection. The first character
	 * of this String is used as the mnemonic
	 */
	public FilterAction(String label) {
		super(label);
		putValue(SHORT_DESCRIPTION, TOOLTIP.concat(label));
		putValue(MNEMONIC_KEY, Integer.valueOf(label.charAt(0)));
		this.label = label;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(label);
	}
}
