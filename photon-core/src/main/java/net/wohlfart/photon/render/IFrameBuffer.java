package net.wohlfart.photon.render;

import javax.media.nativewindow.util.Dimension;
import javax.media.opengl.GL2;


public interface IFrameBuffer {

	void setup(GL2 gl, Dimension dim);

    int getHandle();

	int getDepthBufferHandle();

	int getTextureHandle();

}
