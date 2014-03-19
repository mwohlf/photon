package net.wohlfart.photon.shader;

import java.util.Arrays;

import javax.media.opengl.GL2;
import javax.vecmath.Matrix4f;

import net.wohlfart.photon.resources.ResourceManager;
import net.wohlfart.photon.texture.ITexture;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;


// the uniform handle is the name and location of a uniform in a specific shader
// uniforms also include the textures
public class UniformHandle {

    public static final IUniformValue SHADER_UNIFORM_NULL_VALUE = new NullValue();

    private final IShaderProgram shader;

    private final String name;

    private final int location;


    public UniformHandle(IShaderProgram shader, String name, int location) {
        if (location < 0) {
            throw new IllegalArgumentException("uniform: '" + name + "' has location '" + location + "'");
        }
        this.shader = shader;
        this.name = name;
        this.location = location;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [shaderProgramId=" + shader.getId()
                + ", name=" + name
                + ", location=" + location + "]";
    }


    // uniform values are values that can be assigned to uniform handlers
    // they are independent from a specific shader calling accept for a specific hanle and shader
    // assigns them

    public interface IUniformValue {

        void accept(UniformHandle handle);

    }


    private static class NullValue implements IUniformValue {
        @Override
        public void accept(UniformHandle handle) {
            // do nothing
        }
    }

    public static class Matrix4fValue implements IUniformValue {
        private final Matrix4f matrix;

        public Matrix4fValue(Matrix4f matrix) {
            this.matrix = matrix;
        }

        @Override
        public void accept(UniformHandle handle) {
            assert (matrix != null) : "Uniform '" + handle.name + "' is empty";
            // TODO: figure out if this needs to be transposed
             float[] modelview = {
            		matrix.m00, matrix.m01, matrix.m02, matrix.m03,
            		matrix.m10, matrix.m11, matrix.m12, matrix.m13,
            		matrix.m20, matrix.m21, matrix.m22, matrix.m23,
            		matrix.m30, matrix.m31, matrix.m32, matrix.m33,
            		};
             handle.shader.getGl().glUniformMatrix4fv(handle.location, 1, false, modelview, 0);
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

    public static abstract class TextureValue implements IUniformValue {

        @Override
        public void accept(UniformHandle handle) {
        	GL2 gl = handle.shader.getGl();
        	int slot = handle.shader.nextTextureSlot();
        	gl.glEnable(GL2.GL_TEXTURE_2D);
        	gl.glActiveTexture(ITexture.TEXTURE_SLOTS[slot]);
        	gl.glBindTexture(GL2.GL_TEXTURE_2D, getTextureHandle(handle));
        	gl.glUniform1i(handle.location, slot);

    		int error = gl.glGetError();
    		if (error != GL2.GL_NO_ERROR) {// @formatter:off
    			throw new ShaderException("error after accepting texture value"); // @formatter:on
    		}
        }

        abstract int getTextureHandle(UniformHandle handle);

    }

    public static class TextureIdentValue extends TextureValue {

    	private final ITextureIdentifier textureIdentifier;

		public TextureIdentValue(ITextureIdentifier textureIdentifier) {
    		this.textureIdentifier = textureIdentifier;
    	}

    	@Override
		public int getTextureHandle(UniformHandle handle) {
    		ITexture texture = ResourceManager.loadResource(ITexture.class, textureIdentifier);
    		return texture.getHandle(handle.shader.getGl());
    	}

    }

    public static class TextureHandleValue extends TextureValue {

    	private final int textureHandle;

		public TextureHandleValue(int textureHandle) {
			assert textureHandle > -1;
    		this.textureHandle = textureHandle;
    	}

    	@Override
		public int getTextureHandle(UniformHandle handle) {
    		return textureHandle;
    	}

    }

}
