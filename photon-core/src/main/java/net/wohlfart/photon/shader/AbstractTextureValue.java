package net.wohlfart.photon.shader;

import javax.media.opengl.GL2ES2;

import net.wohlfart.photon.texture.ITexture;

public abstract class AbstractTextureValue implements IUniformValue {

    @Override
    public void accept(IShaderProgram shader) {
    	GL2ES2 gl = shader.getGl();
    	int slot = shader.nextTextureSlot();
    	gl.glEnable(GL2ES2.GL_TEXTURE_2D);
    	gl.glActiveTexture(ITexture.TEXTURE_SLOTS[slot]);
    	gl.glBindTexture(GL2ES2.GL_TEXTURE_2D, getTextureHandle(shader));
    	gl.glUniform1i(getLocation(shader), slot);
    }

    abstract int getTextureHandle(IShaderProgram shader);

    abstract int getLocation(IShaderProgram shader);

}