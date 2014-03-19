package net.wohlfart.photon.shader;

import java.util.Arrays;

import javax.vecmath.Matrix4f;

public class Matrix4fValue implements IUniformValue {
    private final Matrix4f matrix;

    public Matrix4fValue(Matrix4f matrix) {
        this.matrix = matrix;
    }

    @Override
    public void accept(IUniformHandle handle) {
        assert (matrix != null) : "Uniform '" + handle.getName() + "' is empty";
        // TODO: figure out if this needs to be transposed
         float[] modelview = {
        		matrix.m00, matrix.m01, matrix.m02, matrix.m03,
        		matrix.m10, matrix.m11, matrix.m12, matrix.m13,
        		matrix.m20, matrix.m21, matrix.m22, matrix.m23,
        		matrix.m30, matrix.m31, matrix.m32, matrix.m33,
        		};
         handle.getShader().getGl().glUniformMatrix4fv(handle.getLocation(), 1, false, modelview, 0);
    }

    @Override
    public String toString() {
        float[] modelview = {
        		matrix.m00, matrix.m01, matrix.m02, matrix.m03,
        		matrix.m10, matrix.m11, matrix.m12, matrix.m13,
        		matrix.m20, matrix.m21, matrix.m22, matrix.m23,
        		matrix.m30, matrix.m31, matrix.m32, matrix.m33,
        		};
        return Arrays.toString(modelview);
    }

}