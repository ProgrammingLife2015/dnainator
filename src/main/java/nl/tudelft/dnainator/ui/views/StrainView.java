package nl.tudelft.dnainator.ui.views;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import nl.tudelft.dnainator.ui.models.GraphItem;
import nl.tudelft.dnainator.ui.models.ModelItem;
import nl.tudelft.dnainator.ui.widgets.contexts.ViewContext;
import nl.tudelft.dnainator.ui.widgets.dialogs.ExceptionDialog;

/**
 * This class is the View part of the MVC pattern.
 */
public class StrainView extends Pane {
	private static final double SCALE = .1;
	private static final double ZOOM_FACTOR = 1e-3;
	private static final int DEFAULT_ZOOM_IN = 55;
	private static final int DEFAULT_ZOOM_OUT = -150;
	private Affine scale;
	private Translate toCenter;
	private Translate translate;

	private ModelItem mi;

	/**
	 * Creates a new view instance.
	 */
	public StrainView() {
		loadFXML();
		getStyleClass().add("view");
		setOnContextMenuRequested(e -> {
			ViewContext.getInstance().show(StrainView.this, e.getScreenX(), e.getScreenY());
			e.consume();
		});

		toCenter = new Translate();
		widthProperty().addListener((o, v1, v2) -> toCenter.setX(v2.intValue() / 2));
		heightProperty().addListener((o, v1, v2) -> toCenter.setY(v2.intValue() / 2));

		translate = new Translate();
		scale = new Affine(new Scale(SCALE, SCALE));

		mi = new GraphItem();
		mi.getTransforms().add(toCenter);
		mi.getTransforms().add(translate);
		mi.getTransforms().add(scale);
		getChildren().add(mi);
	}

	private void loadFXML() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/fxml/view.fxml"));
		fxmlLoader.setRoot(this);

		try {
			fxmlLoader.load();
		} catch (IOException e) {
			new ExceptionDialog(null, e, "Can not load the strain view!");
		}
	}

	/**
	 * Returns the concatenated transform from world coordinates to camera coordinates.
	 * @return	the concatenated transform
	 */
	private Transform worldToCamera() {
		return toCenter.createConcatenation(translate).createConcatenation(scale);
	}

	/**
	 * Transforms a given bounding box from camera coordinates to world coordinates.
	 * @param b	the given bounding box
	 * @return	the transformed bounding box
	 */
	public Bounds cameraToWorld(Bounds b) {
		Bounds world = null;
		try {
			world = worldToCamera().inverseTransform(b);
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}
		return world;
	}

	/**
	 * Pan the camera by the amount given by the delta vector.
	 * @param delta	the delta vector
	 */
	public void pan(Point2D delta) {
		translate.setX(translate.getX() + delta.getX());
		translate.setY(translate.getY() + delta.getY());
		mi.update(cameraToWorld(getLayoutBounds()));
	}

	/**
	 * Zoom the camera by the amount given by zoom.
	 * @param delta		the amount to zoom in
	 * @param center	the center of the zoom, in camera space
	 */
	public void zoom(double delta, Point2D center) {
		try {
			center = scale.inverseTransform(center.getX() - toCenter.getX() - translate.getX(),
							center.getY() - toCenter.getY() - translate.getY());
			double zoom = 1 + delta * ZOOM_FACTOR;
			scale.append(new Scale(zoom, zoom, center.getX(), center.getY()));
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}
		mi.update(cameraToWorld(getLayoutBounds()));
	}

	/**
	 * Default zoom method. Zooms in the camera by a default amount and to the center of the view.
	 */
	public void zoomIn() {
		zoom(DEFAULT_ZOOM_IN, getCenter());
	}

	/**
	 * Default zoom method. Zooms out the camera by a default amount and to the center of the view.
	 */
	public void zoomOut() {
		zoom(DEFAULT_ZOOM_OUT, getCenter());
	}

	/**
	 * @return The center {@link Point2D} of the view.
	 */
	public final Point2D getCenter() {
		return new Point2D(getWidth() / 2, getHeight() / 2);
	}
}
