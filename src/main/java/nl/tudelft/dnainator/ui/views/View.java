package nl.tudelft.dnainator.ui.views;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import nl.tudelft.dnainator.graph.impl.Neo4jSingleton;
import nl.tudelft.dnainator.ui.models.GraphItem;
import nl.tudelft.dnainator.ui.models.ModelItem;
import nl.tudelft.dnainator.ui.widgets.ExceptionDialog;
import nl.tudelft.dnainator.ui.widgets.ViewContext;

import org.neo4j.io.fs.FileUtils;

/**
 * This class is the View part of the MVC pattern.
 */
public class View extends Pane {
	private Scale scale;
	private Translate toCenter;
	private Translate translate;
	private Transform worldToCamera;

	private ModelItem mi;

	/**
	 * Creates a new view instance.
	 */
	public View() {
		try {
			FileUtils.deleteRecursively(new File(Neo4jSingleton.DB_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}

		loadFXML();
		getStyleClass().add("view");
		setOnContextMenuRequested(e -> {
			ViewContext.getInstance().show(View.this, e.getScreenX(), e.getScreenY());
			e.consume();
		});

		toCenter = new Translate();
		widthProperty().addListener((o, v1, v2) -> toCenter.setX(v2.intValue() / 2));
		heightProperty().addListener((o, v1, v2) -> toCenter.setY(v2.intValue() / 2));

		translate = new Translate();
		translate.setOnTransformChanged(e -> worldToCamera = worldToCamera());

		scale = new Scale();
		scale.setOnTransformChanged(e -> worldToCamera = worldToCamera());

		mi = new GraphItem();
		mi.getTransforms().add(toCenter);
		mi.getTransforms().add(scale);
		mi.getTransforms().add(translate);
		getChildren().add(mi);
	}

	private void loadFXML() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/fxml/view.fxml"));
		fxmlLoader.setRoot(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			new ExceptionDialog(e, "Can not load the View!");
		}
	}

	/**
	 * Returns the concatenated transform from world coordinates to camera coordinates.
	 * @return	the concatenated transform
	 */
	private Transform worldToCamera() {
		return toCenter.createConcatenation(scale).createConcatenation(translate);
	}

	/**
	 * Transforms a given bounding box from camera coordinates to world coordinates.
	 * @param b	the given bounding box
	 * @return	the transformed bounding box
	 */
	public Bounds cameraToWorld(Bounds b) {
		Bounds world = null;
		try {
			world = worldToCamera.inverseTransform(b);
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}
		return world;
	}

	/**
	 * Pan the camera by the amount given by the delta vector.
	 * FIXME: sensitivity depends on zoom level.
	 * @param delta	the delta vector
	 */
	public void pan(Point2D delta) {
		try {
			translate.setX(translate.getX() + scale.inverseTransform(delta).getX());
			translate.setY(translate.getY() + scale.inverseTransform(delta).getY());
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}
		mi.update(cameraToWorld(getLayoutBounds()));
	}

	/**
	 * Zoom the camera by the amount given by zoom.
	 * @param zoom	the amount to zoom in
	 */
	public void zoom(Double zoom) {
		scale.setX(scale.getX() + (scale.getX() * zoom));
		scale.setY(scale.getY() + (scale.getY() * zoom));
		mi.update(cameraToWorld(getLayoutBounds()));
	}
}
