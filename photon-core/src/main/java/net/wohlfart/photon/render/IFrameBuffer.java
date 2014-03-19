package net.wohlfart.photon.render;

import javax.media.opengl.GL2;

import net.wohlfart.photon.tools.Dimension;



public interface IFrameBuffer {

	void setup(GL2 gl, Dimension dim);

    int getHandle();

	int getDepthBufferHandle();

	int getTextureHandle();

}
