package net.wohlfart.photon.render;

import javax.media.opengl.GL2ES2;

import net.wohlfart.photon.tools.Dimension;



public interface IFrameBuffer {

	void setup(GL2ES2 gl);

    int getHandle();

	int getDepthBufferHandle();

	int getTextureHandle();

	Dimension getDimension();

}
