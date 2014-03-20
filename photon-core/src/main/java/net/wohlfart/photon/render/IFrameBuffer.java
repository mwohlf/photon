package net.wohlfart.photon.render;

import javax.media.opengl.GL2;

import net.wohlfart.photon.tools.Dimension;



public interface IFrameBuffer {

	void setup(GL2 gl);

    int getHandle();

	int getDepthBufferHandle();

	int getTextureHandle();

	Dimension getDimension();

}
