package nl.tudelft.dnainator.ui;

import java.awt.event.MouseEvent;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;
import java.awt.event.MouseListener;

import org.graphstream.ui.view.Camera;

public class DNAMouseListener implements MouseListener {
	private Camera cam;

	public DNAMouseListener(Camera c) {
		this.cam = c;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (isLeftMouseButton(e)) {
			cam.setViewPercent(Math.abs(cam.getViewPercent() - .05));
		} else if (isRightMouseButton(e)) {
			cam.setViewPercent(cam.getViewPercent() + .05);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
