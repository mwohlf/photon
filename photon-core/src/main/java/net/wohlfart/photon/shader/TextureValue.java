package net.wohlfart.photon.shader;

import javax.media.opengl.GL2;

import net.wohlfart.photon.texture.ITexture;

public abstract class TextureValue implements IUniformValue {

    @Override
    public void accept(IUniformHandle handle) {
    	GL2 gl = handle.getShader().getGl();
    	int slot = handle.getShader().nextTextureSlot();
    	gl.glEnable(GL2.GL_TEXTURE_2D);
    	gl.glActiveTexture(ITexture.TEXTURE_SLOTS[slot]);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, getTextureHandle(handle));
    	gl.glUniform1i(handle.getLocation(), slot);

		int error = gl.glGetError();
		if (error != GL2.GL_NO_ERROR) {// @formatter:off
			throw new ShaderException("error after accepting texture value"); // @formatter:on
		}
    }

    abstract int getTextureHandle(IUniformHandle handle);

}