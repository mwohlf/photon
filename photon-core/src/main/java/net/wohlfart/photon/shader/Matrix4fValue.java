package net.wohlfart.photon.shader;

import java.util.Arrays;

import javax.vecmath.Matrix4f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// a simple matrix value, note the key is the name,
// setting a second uniform value with the same key will override the first one
public class Matrix4fValue implements IUniformValue {
	protected static final Logger LOGGER = LoggerFactory.getLogger(Matrix4fValue.class);

	protected final String key;
	protected final String name;
    protected final Matrix4f matrix;

    public Matrix4fValue(String key, Matrix4f matrix) {
    	this.key = key;
    	this.name = key;
        this.matrix = matrix;
    }

	@Override
	public String getKey() {
		return key;
	}

    @Override
    public void accept(IShaderProgram shader) {
        assert (matrix != null) : "Uniform '" + shader.getUniformLocation(name) + "' is empty";

         float[] floatArray = {
        		matrix.m00, matrix.m01, matrix.m02, matrix.m03,
        		matrix.m10, matrix.m11, matrix.m12, matrix.m13,
        		matrix.m20, matrix.m21, matrix.m22, matrix.m23,
        		matrix.m30, matrix.m31, matrix.m32, matrix.m33,
        		};
         Integer location = shader.getUniformLocation(name);
         LOGGER.debug("setting matrix uniform '{}' location is '{}', values are '{}'", name, location, Arrays.toString(floatArray));
         shader.getGl().glUniformMatrix4fv(location, 1, false, floatArray, 0);
    }

    @Override
    public String toString() {
        float[] floatArray = {
        		matrix.m00, matrix.m10, matrix.m20, matrix.m30,
        		matrix.m01, matrix.m11, matrix.m21, matrix.m31,
        		matrix.m02, matrix.m12, matrix.m22, matrix.m32,
        		matrix.m03, matrix.m13, matrix.m23, matrix.m33,
        		};
        return Arrays.toString(floatArray);
    }

}