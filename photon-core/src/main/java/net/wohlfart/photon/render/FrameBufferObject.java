package net.wohlfart.photon.render;

import java.nio.ByteBuffer;

import javax.media.opengl.GL2;

import net.wohlfart.photon.tools.Dimension;



// FIXME: checkout
// http://gamedev.stackexchange.com/questions/19804/how-can-i-downsample-a-texture-using-fbos
// for info about mipmaps

public class FrameBufferObject implements IFrameBuffer {

    private int fboHandle = -1;
    private int depthBufferHandle = -1;
    private int textureHandle = -1;

    private final Dimension dim = new Dimension();


    @Override
	public int getHandle() {
        return fboHandle;
    }

    @Override
    public int getTextureHandle() {
        assert fboHandle != -1;
        assert textureHandle != -1;
        return textureHandle;
    }

    @Override
    public int getDepthBufferHandle() {
        assert fboHandle != -1;
        assert depthBufferHandle != -1;
        return depthBufferHandle;
    }

	@Override
	public Dimension getDimension() {
		return dim;
	}

    @Override
    public void setup(GL2 gl) {
    	// see: https://github.com/demoscenepassivist/SocialCoding/blob/master/code_demos_jogamp/src/framework/base/BaseFrameBufferObjectRendererExecutor.java
    	// see: http://www.mathematik.uni-marburg.de/~thormae/lectures/graphics1/code/JoglFboDepth/JoglFboDepth.java

    	assert(this.dim.getHeight() > 0);
    	assert(this.dim.getWidth() > 0);

        // create and bind a new framebuffer
        int[] arr = new int[1];
        gl.glGenFramebuffers(1, arr, 0);
        fboHandle = arr[0];
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, fboHandle);

        // create and bind and a new texture used as a color buffer
        gl.glGenTextures(1, arr, 0);
        textureHandle = arr[0];
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textureHandle);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
    	ByteBuffer fakeColorBuffer = ByteBuffer.allocateDirect(dim.getWidthi() * dim.getHeighti() * 4);
    	gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, dim.getWidthi(), dim.getHeighti(), 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, fakeColorBuffer);
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);

        // create and bind a new depth buffer
        gl.glGenTextures(1, arr, 0);
        depthBufferHandle = arr[0];
        gl.glBindTexture(GL2.GL_TEXTURE_2D, depthBufferHandle);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
        ByteBuffer fakeDepthBuffer = ByteBuffer.allocateDirect(dim.getWidthi() * dim.getHeighti());
        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_DEPTH_COMPONENT, dim.getWidthi(), dim.getHeighti(), 0, GL2.GL_DEPTH_COMPONENT, GL2.GL_UNSIGNED_BYTE, fakeDepthBuffer);
        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);

        //attach the textures to the framebuffer
        gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0, GL2.GL_TEXTURE_2D, textureHandle, 0);
        gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_DEPTH_ATTACHMENT, GL2.GL_TEXTURE_2D, depthBufferHandle, 0);

        //unbind fbo
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);

        checkFrameBufferObjectCompleteness(gl);
    }

    private void checkFrameBufferObjectCompleteness(GL2 gl) {
        int err = gl.glCheckFramebufferStatus(GL2.GL_FRAMEBUFFER);
        switch(err) {
            case GL2.GL_FRAMEBUFFER_COMPLETE:
            	// everything is fine
            	break;
            case GL2.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
            	throw new RuntimeException("FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT");
            case GL2.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
            	throw new RuntimeException("FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT");
            case GL2.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS:
            	throw new RuntimeException("FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT");
            case GL2.GL_FRAMEBUFFER_INCOMPLETE_FORMATS:
            	throw new RuntimeException("FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT");
            case GL2.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
            	throw new RuntimeException("FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT");
            case GL2.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
            	throw new RuntimeException("FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT");
            case GL2.GL_FRAMEBUFFER_UNSUPPORTED:
            	throw new RuntimeException("FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_UNSUPPORTED_EXT");
            default:
            	throw new RuntimeException("FRAMEBUFFER CHECK RETURNED UNKNOWN RESULT ...");
        }
    }

	public void setDimension(Dimension dimension) {
		dim.set(dimension);
	}

}
