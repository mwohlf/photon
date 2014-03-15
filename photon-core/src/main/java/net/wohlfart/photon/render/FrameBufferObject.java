package net.wohlfart.photon.render;

import javax.media.nativewindow.util.Dimension;



// FIXME: checkout
// http://gamedev.stackexchange.com/questions/19804/how-can-i-downsample-a-texture-using-fbos
// for info about mipmaps

public class FrameBufferObject {

    private final boolean isInitialized = false;

    private final int fboHandle = -1;
    private final int depthBufferHandle = -1;
    private final int textureHandle = -1;

    private int width;
    private int height;

    public boolean isInitialzed() {
        return isInitialized;
    }


    public int getFboHandle() {
        assert fboHandle != -1;
        return fboHandle;
    }


    public int getTextureHandle() {
        return textureHandle;
    }


    public void setup(Dimension dim) {
    	/*
        this.width = dim.getWidth();
        this.height = dim.getHeight();

        // create a new framebuffer
        fboHandle = EXTFramebufferObject.glGenFramebuffersEXT();
        // and a new texture used as a color buffer
        textureHandle = GL11.glGenTextures();
        // and a new depthbuffer
        depthBufferHandle = EXTFramebufferObject.glGenRenderbuffersEXT();

        // switch to the new FBO target can be:  GL_FRAMEBUFFER​, GL_READ_FRAMEBUFFER​, or GL_DRAW_FRAMEBUFFER​
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboHandle);

        // initialize texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);               // make it linear filterd
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
                EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, textureHandle, 0); // attach it to the framebuffer

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        */

        /*

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 20);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);

        // we want GL11.GL_NEAREST so we don't mix in the background color of the first texture/fbo at the edges
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR); // GL_NEAREST, GL_LINEAR without mipmaps
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
                EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, textureHandle, 0); // attach it to the framebuffer
        */

        /*
        // initialize depth renderbuffer
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthBufferHandle);                // bind the depth renderbuffer
        EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, width, height); // get the data space for it
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
                EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthBufferHandle); // bind it to the renderbuffer

        checkFBO(fboHandle);

        // switch back to normal framebuffer
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboHandle);

        isInitialized = true;
        */
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


    private void checkFBO(int fboHandle) {
    	/*
        int framebuffer = EXTFramebufferObject.glCheckFramebufferStatusEXT( EXTFramebufferObject.GL_FRAMEBUFFER_EXT );
        switch ( framebuffer ) {
        case EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT:
            break;
        case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
            throw new RuntimeException( "FrameBuffer: " + fboHandle
                    + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception" );
        case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
            throw new RuntimeException( "FrameBuffer: " + fboHandle
                    + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception" );
        case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
            throw new RuntimeException( "FrameBuffer: " + fboHandle
                    + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception" );
        case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
            throw new RuntimeException( "FrameBuffer: " + fboHandle
                    + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception" );
        case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
            throw new RuntimeException( "FrameBuffer: " + fboHandle
                    + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception" );
        case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
            throw new RuntimeException( "FrameBuffer: " + fboHandle
                    + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception" );
        default:
            throw new RuntimeException( "Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer );
        }
        */
    }

}
