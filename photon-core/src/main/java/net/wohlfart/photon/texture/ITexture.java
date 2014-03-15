package net.wohlfart.photon.texture;

import java.net.URI;

import javax.media.opengl.GL2;

public interface ITexture {

	public static final int[] TEXTURE_SLOTS = new int[] {
		GL2.GL_TEXTURE0, GL2.GL_TEXTURE1, GL2.GL_TEXTURE2, GL2.GL_TEXTURE3, GL2.GL_TEXTURE4, GL2.GL_TEXTURE5,
		GL2.GL_TEXTURE6, GL2.GL_TEXTURE7, GL2.GL_TEXTURE8, GL2.GL_TEXTURE9, GL2.GL_TEXTURE10, GL2.GL_TEXTURE11};


    public interface ITextureIdentifier {

        URI getTextureResource();

    }

    int getHandle(GL2 gl);

}
