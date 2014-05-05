package net.wohlfart.photon.shader;

import java.util.Arrays;

import javax.vecmath.Matrix4f;

public class Matrix4fValue implements IUniformValue {
    private final Matrix4f matrix;
	private final String name;

    public Matrix4fValue(String name, Matrix4f matrix) {
    	this.name = name;
        this.matrix = matrix;
    }

    @Override
    public void accept(IShaderProgram shader) {
        assert (matrix != null) : "Uniform '" + shader.getUniformLocation(name) + "' is empty";

         float[] modelview = {
        		matrix.m00, matrix.m01, matrix.m02, matrix.m03,
        		matrix.m10, matrix.m11, matrix.m12, matrix.m13,
        		matrix.m20, matrix.m21, matrix.m22, matrix.m23,
        		matrix.m30, matrix.m31, matrix.m32, matrix.m33,
        		};
         shader.getGl().glUniformMatrix4fv(shader.getUniformLocation(name), 1, false, modelview, 0);
    }

    @Override
    public String toString() {
        float[] modelview = {
        		matrix.m00, matrix.m10, matrix.m20, matrix.m30,
        		matrix.m01, matrix.m11, matrix.m21, matrix.m31,
        		matrix.m02, matrix.m12, matrix.m22, matrix.m32,
        		matrix.m03, matrix.m13, matrix.m23, matrix.m33,
        		};
        return Arrays.toString(modelview);
    }

}