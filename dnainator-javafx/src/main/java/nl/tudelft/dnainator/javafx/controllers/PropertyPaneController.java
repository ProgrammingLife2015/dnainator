package nl.tudelft.dnainator.javafx.controllers;

import java.util.List;
import nl.tudelft.dnainator.javafx.widgets.Propertyable;
import nl.tudelft.dnainator.javafx.widgets.animations.LeftSlideAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.SlidingAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.TransitionAnimation.Position;
import nl.tudelft.dnainator.javafx.widgets.ClusterProperties;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

/**
 * Controls the property pane on the right side of the application. 
 * It provides information about selected strain data.
 */
public class PropertyPaneController {
	private SlidingAnimation animation;
	private static final int WIDTH = 300;
	private static final int ANIM_DURATION = 250;
	private static final int TOOLTIP_WIDTH = 450;
	
	@SuppressWarnings("unused") @FXML private VBox propertyPane;
	@SuppressWarnings("unused") @FXML private VBox vbox;
	@SuppressWarnings("unused") @FXML private Label propertyType;
	@SuppressWarnings("unused") @FXML private Label id;
	@SuppressWarnings("unused") @FXML private Label idInfo;
	@SuppressWarnings("unused") @FXML private Separator idSeparator;
	@SuppressWarnings("unused") @FXML private Label sRef;
	@SuppressWarnings("unused") @FXML private Label sRefInfo;
	@SuppressWarnings("unused") @FXML private Separator sRefSeparator;
	@SuppressWarnings("unused") @FXML private Label eRef;
	@SuppressWarnings("unused") @FXML private Label eRefInfo;
	@SuppressWarnings("unused") @FXML private Separator eRefSeparator;
	@SuppressWarnings("unused") @FXML private Label seq;
	@SuppressWarnings("unused") @FXML private Label seqInfo;
	@SuppressWarnings("unused") @FXML private Separator seqSeparator;
	@SuppressWarnings("unused") @FXML private Label source;
	@SuppressWarnings("unused") @FXML private Label sourceInfo;

	/*
	 * Sets up the animation for the property pane.
	 */
	@SuppressWarnings("unused") @FXML
	private void initialize() {
		animation = new LeftSlideAnimation(propertyPane, WIDTH, ANIM_DURATION, Position.RIGHT);
	}
	
	/**
	 * Updates the displayed information.
	 * @param p The new {@link Propertyable} whose information to display.
	 */
	public void update(Propertyable p) {
		propertyType.setText(p.getType());
		propertyType.setVisible(true);
		vbox.getChildren().clear();

		if (p instanceof ClusterProperties) {
			ClusterProperties cs = (ClusterProperties) p;
			updateIds(cs);
			updateStartRefs(cs);
			updateEndRefs(cs);
			updateSequences(cs);
		}	
		updateSources(p);
	}
	
	private void updateIds(ClusterProperties cp) {
		List<String> ids = cp.getIds();
		if (ids == null) {
			return;
		}

		setLabelGroupVisible(id, idInfo, idSeparator);
		String res = setupInfoText(ids);
		idInfo.setText(res);
		attachTooltip(idInfo, res);
		
		vbox.getChildren().addAll(id, idInfo, idSeparator);
	}
	
	private void updateStartRefs(ClusterProperties cp) {
		List<Integer> sRefs = cp.getStartRefs();
		if (sRefs == null) {
			return;
		}
		
		setLabelGroupVisible(sRef, sRefInfo, sRefSeparator);
		String res = setupInfoText(sRefs);
		sRefInfo.setText(res);
		attachTooltip(sRefInfo, res);
		
		vbox.getChildren().addAll(sRef, sRefInfo, sRefSeparator);
	}
	
	private void updateEndRefs(ClusterProperties cp) {
		List<Integer> eRefs = cp.getEndRefs();
		if (eRefs == null) {
			return;
		}
		
		setLabelGroupVisible(eRef, eRefInfo, eRefSeparator);
		String res = setupInfoText(eRefs);
		eRefInfo.setText(res);
		attachTooltip(eRefInfo, res);
		
		vbox.getChildren().addAll(eRef, eRefInfo, eRefSeparator);
	}
	
	private void updateSequences(ClusterProperties cp) {
		List<String> seqs = cp.getSequences();
		if (seqs == null) {
			return;
		}

		setLabelGroupVisible(seq, seqInfo, seqSeparator);
		String res = setupInfoText(seqs);
		seqInfo.setText(res);
		attachTooltip(seqInfo, res);
		
		vbox.getChildren().addAll(seq, seqInfo, seqSeparator);
	}
	
	private void updateSources(Propertyable p) {
		List<String> sources = p.getSources();
		if (sources == null) {
			return;
		}
		
		setLabelGroupVisible(source, sourceInfo, null);
		
		String res = setupInfoText(sources);
		sourceInfo.setText(res);
		
		attachTooltip(sourceInfo, res);
		vbox.getChildren().addAll(source, sourceInfo);
	}
	
	private void setLabelGroupVisible(Label title, Label info, Separator separator) {
		title.setVisible(true);
		info.setVisible(true);
		
		if (separator != null) {
			separator.setVisible(true);
		}
	}
	
	private String setupInfoText(List<?> infoText) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < infoText.size(); i++) {
			sb.append(infoText.get(i));
			if (i < infoText.size() - 1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	
	/**
	 * Attach a {@link Tooltip} to a label.
	 * @param attachee     The {@link Label} the {@link Tooltip} will be attached to.
	 * @param msg          The {@link Tooltip}'s message.
	 */
	private void attachTooltip(Label attachee, String msg) {
		Tooltip t = new Tooltip(msg);
		t.setWrapText(true);
		t.setAutoFix(true);
		t.setMaxWidth(TOOLTIP_WIDTH);
		Tooltip.install(attachee, t);
	}
	
	/**
	 * Toggle the sliding animation on the {@link PropertyPane}.
	 */
	public void toggle() {
		animation.toggle();
	}
}
