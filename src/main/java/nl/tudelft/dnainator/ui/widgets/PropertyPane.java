package nl.tudelft.dnainator.ui.widgets;

import java.io.IOException;
import java.util.List;

import nl.tudelft.dnainator.ui.widgets.dialogs.ExceptionDialog;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * A pane displaying several properties of the displayed
 * DNA sequences.
 * <p>
 * The pane is animated with slide-in and slide-out animations. The assumption
 * is made (and enforced) that the pane starts being visible.
 * </p>
 */
public class PropertyPane extends VBox {
	private static final String FXML = "/ui/fxml/propertypane.fxml";
	private static final int DURATION = 250;
	private static final int WIDTH = 300;
	@FXML private Label label;
	@FXML private VBox vbox;
	private Animation show;
	private Animation hide;

	/**
	 * Creates a new property pane.
	 */
	public PropertyPane() {
		loadFXML();

		this.setPrefWidth(WIDTH);
		this.setVisible(true);
		this.setMinWidth(0);
		setupHideAnimation(WIDTH, DURATION);
		setupShowAnimation(WIDTH, DURATION);

		getStyleClass().add("property-pane");
		label.setId("properties-label");
	}

	private void loadFXML() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this); // Necessary for the FXML injection.

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			new ExceptionDialog(null, e, "Can not load the PropertyPane!");
		}
	}

	private void setupHideAnimation(double expandedWidth, int duration) {
		hide = new Transition() {
			{ setCycleDuration(Duration.millis(duration)); }

			@Override
			protected void interpolate(double frac) {
				double curWidth = expandedWidth * (1.0 - frac);
				setPrefWidth(curWidth);
				setTranslateX(expandedWidth - curWidth);
			}
		};
		hide.onFinishedProperty().set(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				setVisible(false);
			}
		});
	}

	private void setupShowAnimation(double expandedWidth, int duration) {
		show = new Transition() {
			{ setCycleDuration(Duration.millis(duration)); }

			@Override
			protected void interpolate(double frac) {
				double curWidth = expandedWidth * frac;
				setPrefWidth(curWidth);
				setTranslateX(expandedWidth - curWidth);
			}
		};
	}

	/**
	 * Toggles the animation, i.e. if the box is currently
	 * shown it will be hidden and vice-versa.
	 */
	public void toggle() {
		if (show.statusProperty().get() == Animation.Status.STOPPED
				&& hide.statusProperty().get() == Animation.Status.STOPPED) {
			if (isVisible()) {
				hide.play();
			} else {
				setVisible(true);
				show.play();
			}
		}
	}

	/**
	 * Updates the displayed information.
	 * @param p The new {@link Propertyable} whose information to display.
	 */
	public void update(Propertyable p) {
		label.setText(p.getType());
		vbox.getChildren().clear();

		updateSources(p);
	}

	private void updateSources(Propertyable p) {
		List<String> list = p.getSources();
		if (list == null) {
			return;
		}

		Label id = new Label("Sources");
		id.getStyleClass().add("property-header");
		vbox.getChildren().add(id);

		for (String s : list) {
			vbox.getChildren().add(new Label(s));
		}
	}
}
