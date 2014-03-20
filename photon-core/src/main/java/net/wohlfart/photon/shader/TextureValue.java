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
    }

    abstract int getTextureHandle(IUniformHandle handle);

}