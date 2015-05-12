package nl.tudelft.dnainator.ui.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import nl.tudelft.dnainator.ui.models.GraphModel;
import nl.tudelft.dnainator.ui.views.SequenceView;
import nl.tudelft.dnainator.ui.views.View;

/**
 * Controller class for all graph interaction.
 */
public class GraphController {

	/**
	 * Defines the different types of {@link View}s.
	 */
	public enum ViewType { SEQUENCE, LAST }

	@FXML private ScrollPane scrollPane;
	private ObjectProperty<GraphModel> graphModel;
	// FIXME: Perhaps this should also be a property for the WindowController to bind to.
	private View[] views;

	@FXML
	private void initialize() {
		this.graphModel = new SimpleObjectProperty<>(this, "graphModel");

		GraphModel model = new GraphModel();
		setModel(model);

		/*
		 * Here the relevant listeners should be bound to their related properties
		 * in the GraphModel. For now, there are only two properties: one with all
		 * the nodes in the graph and one with all the edges in the graph. These are
		 * just here as an example and as something to work against. Eventually, we
		 * probably only want to contain those nodes that are visible + some cache
		 * of off-screen nodes (on both sides). Other properties will probably also be
		 * needed. FIXME: We should look into lazy evaluation or perhaps even lazy redraw.
		 */
//		model.nodesProperty().addListener((observable, oldValue, newValue) ->
//				getActiveView().redraw());
//		model.edgesProperty().addListener((observable, oldValue, newValue) ->
//				getActiveView().redraw());

		// Create and store all the views.
		this.views = new View[ViewType.LAST.ordinal()];
		this.views[0] = new SequenceView(model);

		// Set the first view as default.
		scrollPane.setContent(this.views[0]);
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

	/**
	 * Sets the currently active {@link View}.
	 * @param v The {@link ViewType} corresponding to the required {@link View}.
	 */
	public void setView(ViewType v) {
		scrollPane.setContent(this.views[v.ordinal()]);
	}

	/**
	 * @return The currently active {@link View}.
	 */
	public View getActiveView() {
		return (View) scrollPane.getContent();
	}
}
