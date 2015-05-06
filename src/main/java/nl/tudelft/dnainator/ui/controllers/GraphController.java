package nl.tudelft.dnainator.ui.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import nl.tudelft.dnainator.core.DefaultSequenceNode;
import nl.tudelft.dnainator.ui.models.GraphModel;

/**
 * Controller class for all graph interaction.
 */
public class GraphController {
	@FXML private Group graphRoot;
	private ObjectProperty<GraphModel> graphModel;

	@FXML
	private void initialize() {
		this.graphModel = new SimpleObjectProperty<GraphModel>(this, "graphModel");

		GraphModel model = new GraphModel();
		setModel(model);
		// FIXME: have to read into javafx listeners concerning lazy evaluation.
		model.nodesProperty().addListener((observable, oldValue, newValue) -> redraw());
		model.edgesProperty().addListener((observable, oldValue, newValue) -> redraw());
	}

	/**
	 * @return The current {@link GraphModel}.
	 */
	public final GraphModel getModel() {
		return graphModel.get();
	}

	/**
	 * @param graphModel The new {@link GraphModel}.
	 */
	public final void setModel(GraphModel graphModel) {
		this.graphModel.set(graphModel);
	}

	/**
	 * @return The graph model property.
	 */
	public ObjectProperty<GraphModel> modelProperty() {
		return graphModel;
	}

	@FXML
	private void onMouseClick(MouseEvent e) {
		//System.out.println(getModel().getNodes().get(0));
		// CHECKSTYLE.OFF: MagicNumber
		getModel().getNodes().add(new DefaultSequenceNode("5", "Hooman", 42, 43, "ACTG"));
		// CHECKSTYLE.ON: MagicNumber
	}

	private void redraw() {
		System.out.println("Redrawing...");
	}
}
