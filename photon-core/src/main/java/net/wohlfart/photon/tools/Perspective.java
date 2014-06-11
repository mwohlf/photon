package net.wohlfart.photon.tools;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * - the projection matrix defines the lens of the camera it translates the
 * world space into 2D screen space, it is independent of the actual screen
 * resolution or pixel, just the aspect ratio matters, the screen resolution is
 * used when setting the viewport e.g. glViewport(0, 0, textureWidth,
 * textureHeight);
 *
 * - the view matrix defines the position and the direction of the camera it is
 * set once per rendering pass and defined in which direction the cam is looking
 *
 * - the model matrix defines the position and direction of each 3D model it is
 * used to move and rotate a model in the world space around each model can set
 * its individual matrix before rendering so it is set for each model object
 *
 * see: http://www.lwjgl.org/wiki/index.php?title=The_Quad_with_Projection,
 * _View_and_Model_matrices see:
 * http://db-in.com/blog/2011/04/cameras-on-opengl-es-2-x/
 * http://www.songho.ca/opengl/gl_projectionmatrix.html
 * http://unspecified.wordpress.com/2012/06/21/calculating-the-gluperspective-matrix-and-other-opengl-matrix-maths/
 *
 * @return our projection matrix
 */
public class Perspective {
	private static final Logger LOGGER = LoggerFactory.getLogger(Perspective.class);

	private static final float FIELD_OF_VIEW_LIMIT = 100; // << 180

	boolean isDirty = true;

	// field of view in y direction
	// in dregee [0...360] not rad this is the whole range
	private float fieldOfViewDegree = Float.NaN;
	private float fieldOfViewRad = Float.NaN;

	// the neares visible zCoord
	private float nearPlane = Float.NaN;

	// the fares visible zCoord
	private float farPlane = Float.NaN;

	// the screen width in pixel
	private float width = Float.NaN;

	// the screen height in pixel
	private float height = Float.NaN;

	private final Matrix4f perspectiveMatrix = new Matrix4f();
	private final Matrix3f normalMatrix = new Matrix3f();

	private final Dimension dim = new Dimension();

	private float scaleFactor;
	private float screenScale;

	// field of view in y direction in degree
	// the height covers the field of view
	public void setFieldOfViewDegree(float fieldOfViewDegree) {
		if (fieldOfViewDegree > FIELD_OF_VIEW_LIMIT) {
			LOGGER.warn("field of view must be <= {} found: '{}', resetting to {}", FIELD_OF_VIEW_LIMIT, fieldOfViewDegree, FIELD_OF_VIEW_LIMIT);
		}
		this.fieldOfViewDegree = Math.min(fieldOfViewDegree, FIELD_OF_VIEW_LIMIT);
		this.fieldOfViewRad = (float) ((2f * Math.PI) / 360f) * this.fieldOfViewDegree;
		isDirty = true;
	}

	public void setNearPlane(float nearPlane) {
		this.nearPlane = nearPlane;
		isDirty = true;
	}

	public void setFarPlane(float farPlane) {
		this.farPlane = farPlane;
		isDirty = true;
	}

	public void setScreenWidth(float width) {
		this.width = width;
		isDirty = true;
	}

	public void setScreenHeight(float height) {
		this.height = height;
		isDirty = true;
	}

	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
		isDirty = true;
	}


	public float getFieldOfViewRad() {
		return fieldOfViewRad;
	}

	// return a z value that is transformed into -1 after all the matrices are applied
	public float getZValue() {
		return -nearPlane;
	}

	public float getAspectRatio() {
		return height/width;
	}

	public float getNearPlane() {
		return nearPlane;
	}

	public Dimension getScreenDimension() {
		lazyRecalculate();
		return dim;
	}

	public float getFieldOfViewPixel() {
		return height;
	}

	public float getScreenScale() {
		lazyRecalculate();
		return screenScale;
	}

	// http://unspecified.wordpress.com/2012/06/21/calculating-the-gluperspective-matrix-and-other-opengl-matrix-maths/
	// note that this matrix does not depend on the actual size of the screen but just on the aspect ratio
	public Matrix4f getPerspectiveMatrix() {
		lazyRecalculate();
		return perspectiveMatrix;
	}

	public Matrix3f getNormalMatrix() {
		lazyRecalculate();
		return normalMatrix;
	}


	private void lazyRecalculate() {
		assert !Float.isNaN(farPlane) : "no farPlane configured";
		assert !Float.isNaN(nearPlane) : "no nearPlane configured";
		assert !Float.isNaN(fieldOfViewDegree) : "no fieldOfViewDegree configured";
		assert !Float.isNaN(fieldOfViewRad) : "no fieldOfViewRad configured";
		assert !Float.isNaN(width) : "no width configured";
		assert !Float.isNaN(height) : "no height configured";
		assert !Float.isNaN(scaleFactor) : "no scaleFactor configured";

		if (!isDirty) {
			return;
		}
    	dim.set((int)width, (int)height);
    	screenScale = (float)(scaleFactor * Math.tan(Math.PI/8f) / Math.tan(fieldOfViewRad/2f));

		final float frustumLength = nearPlane - farPlane;
		final float aspectRatio = width / height;
		final float yScale = 1f / (float) Math.tan(fieldOfViewRad / 2f);
		final float xScale = yScale / aspectRatio;

		perspectiveMatrix.m00 = xScale;
		perspectiveMatrix.m01 = 0;
		perspectiveMatrix.m02 = 0;
		perspectiveMatrix.m03 = 0;

		perspectiveMatrix.m10 = 0;
		perspectiveMatrix.m11 = yScale;
		perspectiveMatrix.m12 = 0;
		perspectiveMatrix.m13 = 0;

		perspectiveMatrix.m20 = 0;
		perspectiveMatrix.m21 = 0;
		perspectiveMatrix.m22 = (farPlane+nearPlane)/frustumLength;
		perspectiveMatrix.m23 = -1;

		perspectiveMatrix.m30 = 0;
		perspectiveMatrix.m31 = 0;
		perspectiveMatrix.m32 = 2f*farPlane*nearPlane/frustumLength;
		perspectiveMatrix.m33 = 0;


        normalMatrix.m00 = perspectiveMatrix.m00;
        normalMatrix.m01 = perspectiveMatrix.m01;
        normalMatrix.m02 = perspectiveMatrix.m02;

        normalMatrix.m10 = perspectiveMatrix.m10;
        normalMatrix.m11 = perspectiveMatrix.m11;
        normalMatrix.m12 = perspectiveMatrix.m12;

        normalMatrix.m20 = perspectiveMatrix.m20;
        normalMatrix.m21 = perspectiveMatrix.m21;
        normalMatrix.m22 = perspectiveMatrix.m22;

        normalMatrix.invert(normalMatrix);
        normalMatrix.transpose(normalMatrix);

        isDirty = false;
	}

}
