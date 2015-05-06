package nl.tudelft.dnainator.ui.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
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

		setModel(new GraphModel());
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
		System.out.println(getModel().getNodes().get(0));
	}
}
