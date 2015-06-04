package nl.tudelft.dnainator.javafx.controllers;

import java.util.List;
import nl.tudelft.dnainator.javafx.widgets.Propertyable;
import nl.tudelft.dnainator.javafx.widgets.animations.LeftSlideAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.SlidingAnimation;
import nl.tudelft.dnainator.javafx.widgets.animations.TransitionAnimation.Position;
import nl.tudelft.dnainator.javafx.widgets.ClusterProperties;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
	
	@SuppressWarnings("unused") @FXML private VBox propertyPane;
	@SuppressWarnings("unused") @FXML private VBox vbox;
	@SuppressWarnings("unused") @FXML private Label propertyTitle;
	
	/*
	 * Sets up the services, filechooser and treeproperty.
	 */
	@SuppressWarnings("unused") @FXML
	private void initialize() {
		propertyPane.getStyleClass().add("property-pane");
		propertyTitle.setId("properties-label");
		animation = new LeftSlideAnimation(propertyPane, WIDTH, ANIM_DURATION, Position.RIGHT);
	}
	
	/**
	 * Updates the displayed information.
	 * @param p The new {@link Propertyable} whose information to display.
	 */
	public void update(Propertyable p) {
		propertyTitle.setText(p.getType());
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

	private void updateSources(Propertyable p) {
		List<String> sources = p.getSources();
		if (sources == null) {
			return;
		}

		Label id = new Label("Sources");
		id.getStyleClass().add("property-header");
		vbox.getChildren().add(id);
		sources.forEach(source -> {
			Label sourceLabel = new Label(source);
			
			Tooltip t = new Tooltip(source);
			Tooltip.install(sourceLabel, t);
			vbox.getChildren().add(sourceLabel);
		});

	}
	
	private void updateIds(ClusterProperties cp) {
		List<String> ids = cp.getIds();
		if (ids == null) {
			return;
		}
		
		Label idLabel = new Label("Id(s)");
		idLabel.getStyleClass().add("property-header");
		vbox.getChildren().add(idLabel);
		StringBuilder sb = new StringBuilder();
		ids.forEach(id -> sb.append(id + ", "));
		vbox.getChildren().add(new Label(sb.toString().substring(0, sb.toString().length() - 2)));
	}
	
	private void updateStartRefs(ClusterProperties cp) {
		List<Integer> sRefs = cp.getStartRefs();
		if (sRefs == null) {
			return;
		}
		
		Label srLabel = new Label("Start Reference(s)");
		srLabel.getStyleClass().add("property-header");
		vbox.getChildren().add(srLabel);
		StringBuilder sb = new StringBuilder();
		sRefs.forEach(sRef -> sb.append(sRef + ", "));
		vbox.getChildren().add(new Label(sb.toString().substring(0, sb.toString().length() - 2)));
	}
	
	private void updateEndRefs(ClusterProperties cp) {
		List<Integer> eRefs = cp.getEndRefs();
		if (eRefs == null) {
			return;
		}
		
		Label erLabel = new Label("End Reference(s)");
		erLabel.getStyleClass().add("property-header");
		vbox.getChildren().add(erLabel);
		StringBuilder sb = new StringBuilder();
		eRefs.forEach(eRef -> sb.append(eRef + ", "));
		vbox.getChildren().add(new Label(sb.toString().substring(0, sb.toString().length() - 2)));
	}
	
	private void updateSequences(ClusterProperties cp) {
		List<String> seqs = cp.getSequences();
		if (seqs == null) {
			return;
		}
		
		Label seqLabel = new Label("Sequence(s)");
		seqLabel.getStyleClass().add("property-header");
		vbox.getChildren().add(seqLabel);
		StringBuilder sb = new StringBuilder();
		seqs.forEach(seq -> sb.append(seq + ", "));
		String res = sb.toString().substring(0, sb.toString().length() - 2);
		Label seqLabels = new Label(res);
		Tooltip t = new Tooltip(res);
		Tooltip.install(seqLabels, t);
		vbox.getChildren().add(seqLabels);
	}
	
	/**
	 * Toggle the sliding animation on the {@link PropertyPane}.
	 */
	public void toggle() {
		animation.toggle();
	}
}
