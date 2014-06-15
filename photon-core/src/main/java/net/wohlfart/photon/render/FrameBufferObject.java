package net.wohlfart.photon.render;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;

import net.wohlfart.photon.tools.Dimension;


// FIXME: resize doesn't work here yet
//
// FIXME: checkout
// http://gamedev.stackexchange.com/questions/19804/how-can-i-downsample-a-texture-using-fbos
// for info about mipmaps

public class FrameBufferObject implements IFrameBuffer {

    private int fboHandle = -1;
    private int depthBufferHandle = -1;
    private int textureHandle = -1;

    private final float scaleing = 1f/20f;
    private final Dimension bufferDimension = new Dimension();


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
	public Dimension getBufferDimension() {
		return bufferDimension;
	}

    @Override
    public void setup(GL2ES2 gl) {
    	// see: https://github.com/demoscenepassivist/SocialCoding/blob/master/code_demos_jogamp/src/framework/base/BaseFrameBufferObjectRendererExecutor.java
    	// see: http://www.mathematik.uni-marburg.de/~thormae/lectures/graphics1/code/JoglFboDepth/JoglFboDepth.java

    	assert(this.bufferDimension.getHeight() > 0);
    	assert(this.bufferDimension.getWidth() > 0);

    	int width = (int) bufferDimension.getWidth();
    	int height = (int) bufferDimension.getHeight();

        // create and bind a new framebuffer
        int[] arr = new int[1];
        gl.glGenFramebuffers(1, arr, 0);
        fboHandle = arr[0];
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, fboHandle);


        // create and bind and a new texture used as a color buffer
        gl.glGenTextures(1, arr, 0);
        textureHandle = arr[0];
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textureHandle);

        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

        // ByteBuffer fakeColorBuffer = ByteBuffer.allocateDirect(width * height * 4);
    	gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, width, height, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, null); //fakeColorBuffer);
    	//gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
        //attach the texture to the framebuffer
        gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0, GL2.GL_TEXTURE_2D, textureHandle, 0);


        // create and bind a new depth buffer
        gl.glGenTextures(1, arr, 0);
        depthBufferHandle = arr[0];
        gl.glBindTexture(GL2.GL_TEXTURE_2D, depthBufferHandle);

        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR); // NEAREST);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR); // NEAREST);

        // ByteBuffer fakeDepthBuffer = ByteBuffer.allocateDirect(width * height);
        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_DEPTH_COMPONENT, width, height, 0, GL2.GL_DEPTH_COMPONENT, GL2.GL_UNSIGNED_BYTE, null); //fakeDepthBuffer);
        //gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
        //attach the texture to the framebuffer
        gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_DEPTH_ATTACHMENT, GL2.GL_TEXTURE_2D, depthBufferHandle, 0);

        checkFrameBufferObjectCompleteness(gl);
        //unbind fbo
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);

    }

    private void checkFrameBufferObjectCompleteness(GL2ES2 gl) {
        int err = gl.glCheckFramebufferStatus(GL2ES2.GL_FRAMEBUFFER);
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

    // todo: adjust when resizing the screen
	public void setScreenDimension(Dimension dimension) {
		bufferDimension.set((int) (dimension.getWidth() * scaleing), (int) (dimension.getHeight() * scaleing));
	}

}
