package net.wohlfart.photon.tools;

import javax.vecmath.Matrix4f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// @formatter:off
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

    private final Matrix4f matrix = new Matrix4f();
    private final Dimension dim = new Dimension();

	private float scaleFactor;


	// field of view in y direction in degree
	// the height covers the field of view
    public void setFieldOfViewDegree(float fieldOfViewDegree) {
		this.fieldOfViewDegree = fieldOfViewDegree;
        this.fieldOfViewRad = (float)((Math.PI * 2) / 360f) * (fieldOfViewDegree);
	}

	public void setNearPlane(float nearPlane) {
		assert nearPlane != Float.NaN;
		assert nearPlane > 0;
		this.nearPlane = nearPlane;
	}

	public void setFarPlane(float farPlane) {
		this.farPlane = farPlane;
	}

	public void setScreenWidth(float width) {
		this.width = width;
	}

	public void setScreenHeight(float height) {
		this.height = height;
	}

	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
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
    	dim.set((int)width, (int)height);
    	return dim;
    }

	public float getFieldOfViewPixel() {
		return height;
	}

    public float getScreenScale() {
    	assert fieldOfViewRad != Float.NaN;
    	assert fieldOfViewRad > 0;
    	return (float)(scaleFactor * Math.tan(Math.PI/8f) / Math.tan(fieldOfViewRad/2));
    }

    // http://unspecified.wordpress.com/2012/06/21/calculating-the-gluperspective-matrix-and-other-opengl-matrix-maths/
    // note that this matrix does not depend on the actual size of the screen but just on the aspect ratio
    public Matrix4f getMatrix() {
		assert farPlane != Float.NaN;
		assert nearPlane != Float.NaN;
		assert fieldOfViewDegree != Float.NaN;
		assert fieldOfViewRad != Float.NaN;
		assert width != Float.NaN;
		assert height != Float.NaN;

        if (fieldOfViewDegree > FIELD_OF_VIEW_LIMIT) {
            LOGGER.warn("field of view must be <= {} found: '{}', resetting to {}", FIELD_OF_VIEW_LIMIT, fieldOfViewDegree, FIELD_OF_VIEW_LIMIT);
        }
        fieldOfViewDegree = Math.min(fieldOfViewDegree, FIELD_OF_VIEW_LIMIT);
        fieldOfViewRad = (float) ((2f * Math.PI) / 360f) * fieldOfViewDegree;

        final float frustumLength = nearPlane - farPlane;
        final float aspectRatio = width / height;
        final float yScale = 1f / (float) Math.tan(fieldOfViewRad / 2f);
        final float xScale = yScale / aspectRatio;

        matrix.m00 = xScale;
        matrix.m01 = 0;
        matrix.m02 = 0;
        matrix.m03 = 0;

        matrix.m10 = 0;
        matrix.m11 = yScale;
        matrix.m12 = 0;
        matrix.m13 = 0;

        matrix.m20 = 0;
        matrix.m21 = 0;
        matrix.m22 = (farPlane+nearPlane)/frustumLength;
        matrix.m23 = -1;

        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 2f*farPlane*nearPlane/frustumLength;
        matrix.m33 = 0;

        return matrix;
    }

}
