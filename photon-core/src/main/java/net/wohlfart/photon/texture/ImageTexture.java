package net.wohlfart.photon.texture;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;

import com.jogamp.common.nio.Buffers;


// see: http://www.java-gaming.org/index.php?topic=25516.0
public class ImageTexture implements ITexture {
    private static int BYTES_PER_PIXEL = 4;

    protected int textureId = -1;

    protected final ByteBuffer buffer;
    protected final int width;
    protected final int height;


    public ImageTexture(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        buffer = Buffers.newDirectByteBuffer(width * height * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int pixel = pixels[y * width + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // green component
                buffer.put((byte) (pixel & 0xFF));             // blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));     // alpha component for RGBA
            }
        }
        buffer.flip();
    }

    @Override
    public int getHandle(GL2ES2 gl) {
        if (textureId == -1) {
            setup(gl);
        }
        return textureId;
    }

    protected int getWidth() {
        return width;
    }

    protected int getHeight() {
        return height;
    }

    private void setup(GL2ES2 gl) {
        // see: http://lwjgl.org/wiki/index.php?title=The_Quad_textured
    	int[] iBuff = new int[1];
        gl.glGenTextures(1, iBuff, 0);
        textureId = iBuff[0];
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textureId);

        // setup the ST coordinate system
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        // setup what to do when the texture has to be scaled
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);

        gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);

        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA8, width, height, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buffer);
        gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
    }

}
