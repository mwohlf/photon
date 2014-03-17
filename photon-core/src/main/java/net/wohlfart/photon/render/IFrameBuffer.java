package net.wohlfart.photon.render;

import javax.media.opengl.GL2;


public interface IFrameBuffer {

    int getHandle();

	public void unbind();

	int getDepthBufferHandle();

	int getTextureHandle();

	void setup(GL2 gl);

}
