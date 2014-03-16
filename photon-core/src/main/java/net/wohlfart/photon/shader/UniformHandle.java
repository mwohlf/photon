package net.wohlfart.photon.shader;

import java.util.Arrays;

import javax.media.opengl.GL2;
import javax.vecmath.Matrix4f;


// the uniform handle is the name and location of a uniform in a specific shader
public class UniformHandle {

    public static final IUniformValue SHADER_UNIFORM_NULL_VALUE = new NullValue();

    private final int shaderId;

    private final String name;

    private final int location;


    public interface IUniformValue {

        void accept(GL2 gl, UniformHandle handle);
    }


    private static class NullValue implements IUniformValue {
        @Override
        public void accept(GL2 gl, UniformHandle handle) {
            // do nothing
        }
    }

    public static class Matrix4fValue implements IUniformValue {
        private final Matrix4f matrix;

        public Matrix4fValue(Matrix4f matrix) {
            this.matrix = matrix;
        }

        @Override
        public void accept(GL2 gl, UniformHandle handle) {
            assert (matrix != null) : "Uniform '" + handle.name + "' is empty";
            // TODO: figure out if this needs to be transposed
             float[] modelview = {
            		matrix.m00, matrix.m01, matrix.m02, matrix.m03,
            		matrix.m10, matrix.m11, matrix.m12, matrix.m13,
            		matrix.m20, matrix.m21, matrix.m22, matrix.m23,
            		matrix.m30, matrix.m31, matrix.m32, matrix.m33,
            		};
            		/*
            float[] modelview = {
            		matrix.m00, matrix.m10, matrix.m20, matrix.m30,
            		matrix.m01, matrix.m11, matrix.m21, matrix.m31,
            		matrix.m02, matrix.m12, matrix.m22, matrix.m32,
            		matrix.m03, matrix.m13, matrix.m23, matrix.m33,
            		}; */


            gl.glUniformMatrix4fv(handle.location, 1, false, modelview, 0);
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

    public static class TextureIndexValue implements IUniformValue {
        private final int index;

        TextureIndexValue(int index) {
            this.index = index;
        }

        @Override
        public void accept(GL2 gl, UniformHandle handle) {
            gl.glUniform1i(handle.location, index);
        }

    }

    public UniformHandle(int shaderId, String name, int location) {
        if (location < 0) {
            throw new IllegalArgumentException("uniform: '" + name + "' has location '" + location + "'");
        }
        this.shaderId = shaderId;
        this.name = name;
        this.location = location;
    }

    // FIXME: this method is suspicious, remove it!
    public void setTextureIndex(GL2 gl2, int index) {
        gl2.glUniform1i(location, index);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [shaderProgramId=" + shaderId
                + ", name=" + name
                + ", location=" + location + "]";
    }

}
