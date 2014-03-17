package net.wohlfart.photon.render;

import javax.media.opengl.GL2;



// FIXME: checkout
// http://gamedev.stackexchange.com/questions/19804/how-can-i-downsample-a-texture-using-fbos
// for info about mipmaps

public class FrameBufferObject implements IFrameBuffer {

    private final boolean isInitialized = false;

    private int fboHandle = -1;
    private int depthBufferHandle = -1;
    private int textureHandle = -1;

    private int width;
    private int height;

    public boolean isInitialzed() {
        return isInitialized;
    }

    @Override
	public int getHandle() {
        return fboHandle;
    }

    @Override
    public int getTextureHandle() {
        assert fboHandle != -1;
        return textureHandle;
    }

    @Override
    public int getDepthBufferHandle() {
        assert fboHandle != -1;
        return depthBufferHandle;
    }

    protected int getWidth() {
        return width;
    }

    protected int getHeight() {
        return height;
    }

    @Override
    public void setup(GL2 gl) {
    	// see: https://github.com/demoscenepassivist/SocialCoding/blob/master/code_demos_jogamp/src/framework/base/BaseFrameBufferObjectRendererExecutor.java

        // create and bind a new framebuffer
        int[] result = new int[1];
        gl.glGenFramebuffers(1, result, 0);
        fboHandle = result[0];
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, fboHandle);

        // create and bind and a new texture used as a color buffer
        gl.glGenTextures(1, result, 0);
        textureHandle = result[0];
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textureHandle);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA8, width, height, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, null);

        // create and bind a new depth buffer
        gl.glGenTextures(1, result, 0);
        depthBufferHandle = result[0];
        gl.glBindTexture(GL2.GL_TEXTURE_2D, depthBufferHandle);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_DEPTH_COMPONENT32, width, height, 0, GL2.GL_DEPTH_COMPONENT, GL2.GL_UNSIGNED_INT, null);
        //attach the textures to the framebuffer
        gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0, GL2.GL_TEXTURE_2D, textureHandle, 0);
        gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_DEPTH_ATTACHMENT, GL2.GL_TEXTURE_2D, depthBufferHandle, 0);
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);

        checkFrameBufferObjectCompleteness(gl);
    }

    public void bind() {
    	/*
        GL11.glViewport (0, 0, width, height);
        // unlink textures because if we dont it all is gonna fail
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        // switch to rendering on our FBO, this is the render target now
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboHandle);

        GL11.glClearColor (0f, 0f, 0f, 0f);
        GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		*/


        /*
        // clear screen and depth buffer on the FBO
        //GL11.glClearColor (0f, 0f, 0f, 0f);
        GL11.glClearColor (0f, 0f, 0f, 0f);
        GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        // normal rendering to the FBO

        GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA); // pre-multiplied alpha

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        */
    }

	@Override
	public void unbind() {
        //EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
        //Dimension dim = visitor.getScreenDimension();
        //GL11.glViewport(0, 0, dim.getWidth(), dim.getHeight());
	}


    private void checkFrameBufferObjectCompleteness(GL2 gl) {
        int err = gl.glCheckFramebufferStatus(GL2.GL_FRAMEBUFFER);
        switch(err) {
            case GL2.GL_FRAMEBUFFER_COMPLETE:
            	// everything is fine
            	break;
            	//throw new RuntimeException("FRAMEBUFFEROBJECT CHECK RESULT=GL_FRAMEBUFFER_COMPLETE_EXT");
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

    void destroy(GL2 gl) {
    	/*
    	gl.glDeleteFramebuffers(1, Buffers.newDirectIntBuffer(mFrameBufferObjectID));
    	gl.glDeleteTextures(1, Buffers.newDirectIntBuffer(mColorTextureID));
    	gl.glDeleteTextures(1, Buffers.newDirectIntBuffer(mDepthTextureID));
        if (mBaseFrameBufferObjectRendererInterface!=null) {
            mBaseFrameBufferObjectRendererInterface.cleanup_FBORenderer(inGL,inGLU,inGLUT);
        } else {
            BaseLogging.getInstance().warning("BaseFrameBufferObjectRendererInterface FOR THIS EXECUTOR IS NULL! cleanup() SKIPPED!");
        }
        */
    }

}
