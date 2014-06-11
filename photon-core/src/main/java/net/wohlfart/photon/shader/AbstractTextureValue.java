package net.wohlfart.photon.shader;

import javax.media.opengl.GL2ES2;

import net.wohlfart.photon.texture.ITexture;

public abstract class AbstractTextureValue implements IUniformValue {

	protected final String key;

    public AbstractTextureValue(String key) {
		this.key = key;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
    public void accept(IShaderProgram shader) {
    	GL2ES2 gl = shader.getGl();
    	int slot = shader.nextTextureSlot();
    	gl.glEnable(GL2ES2.GL_TEXTURE_2D);
    	gl.glActiveTexture(ITexture.TEXTURE_SLOTS[slot]);
    	gl.glBindTexture(GL2ES2.GL_TEXTURE_2D, getTextureHandle(shader));
    	// throws an error with  -Djogl.debug.DebugGL
    	//gl.glUniform1i(getLocation(shader), slot);
    }

    abstract int getTextureHandle(IShaderProgram shader);

    abstract int getLocation(IShaderProgram shader);

}