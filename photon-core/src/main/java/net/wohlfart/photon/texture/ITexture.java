package net.wohlfart.photon.texture;

import java.net.URI;

import javax.media.opengl.GL2ES2;


public interface ITexture {

	public final int TEXTURE_RESOLUTION = 10; // pixel per unit, this should depend on the screen resolution

	public static final int[] TEXTURE_SLOTS = new int[] {
		GL2ES2.GL_TEXTURE0,
		GL2ES2.GL_TEXTURE1,
		GL2ES2.GL_TEXTURE2,
		GL2ES2.GL_TEXTURE3,
		GL2ES2.GL_TEXTURE4,
		GL2ES2.GL_TEXTURE5,
		GL2ES2.GL_TEXTURE6,
		GL2ES2.GL_TEXTURE7,
		GL2ES2.GL_TEXTURE8,
		GL2ES2.GL_TEXTURE9,
		GL2ES2.GL_TEXTURE10,
		GL2ES2.GL_TEXTURE11,
		};


    public interface ITextureIdentifier {

        URI getTextureResource();

    }

    int getHandle(GL2ES2 gl);

}
