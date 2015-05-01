package nl.tudelft.dnainator.ui;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import org.graphstream.ui.swingViewer.ViewPanel;

import nl.tudelft.dnainator.ui.actions.FilterAction;
import nl.tudelft.dnainator.ui.actions.OpenAction;

/**
 * DNAinator's main window.
 */
public class Window extends JFrame {
	private static final long serialVersionUID = 6099336881439141487L;
	private static final String DNAINATOR = "DNAinator";
	private static final String ICON = "/ui/icons/dnainator.png";
	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	private static final String[] FILTERS = { "Foo", "Bar", "Baz" };

	/**
	 * Creates a new main window.
	 */
	public Window() {
		super(DNAINATOR);

		/* Appearance. */
		try {
			setIconImage(ImageIO.read(new File(getClass().getResource(ICON).toURI())));
		} catch (Exception e) {
			e.printStackTrace();
		}

		/* Size & placement. */
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setBounds(getScreenWidth() / 2 - WIDTH / 2,
				getScreenHeight() / 2 - HEIGHT / 2, WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* Populate the window. */
		setJMenuBar(createMenuBar());
		add(new DNAViewer().addDefaultView(false));

		/* Finally: show the window. */
		setVisible(true);
	}

	/**
	 * @returns A populated menu bar.
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menubar = new JMenuBar();

		JMenu file = new JMenu("File");
		file.setMnemonic('F');
		file.add(new OpenAction(this));
		menubar.add(file);

		JMenu filter = new JMenu("Filter");
		filter.setMnemonic('i');
		ButtonGroup group = new ButtonGroup();
		for (int i = 0; i < FILTERS.length; i++) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(new FilterAction(FILTERS[i]));
			group.add(filter.add(item));
		}
		menubar.add(filter);

		return menubar;
	}

	/**
	 * @return The screen width.
	 */
	private int getScreenWidth() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();

		return (int) gd.getDefaultConfiguration().getBounds().getWidth();
	}

	/**
	 * @return The screen height.
	 */
	private int getScreenHeight() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();

		return (int) gd.getDefaultConfiguration().getBounds().getHeight();
	}
}
