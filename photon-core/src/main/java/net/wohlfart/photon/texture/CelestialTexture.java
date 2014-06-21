package net.wohlfart.photon.texture;

import java.awt.Color;
import java.nio.IntBuffer;
import java.util.Random;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.vecmath.Vector3f;

import com.jogamp.common.nio.Buffers;


// see: http://www.java-gaming.org/index.php?topic=25516.0
public class CelestialTexture implements ITexture {

    private final Random random;

    protected int textureId = -1;

    protected final IntBuffer intBuffer;
    protected final int width;
    protected final int height;


    public CelestialTexture(float radius, ISphereSurfaceColor type, long seed /*, int textureUnit*/) {
        this((int) (radius * TEXTURE_RESOLUTION),
             (int) (radius * TEXTURE_RESOLUTION),
             type,
             seed);
    }

    CelestialTexture(int width, int height, ISphereSurfaceColor celestialType, long seed /*, int textureUnit*/) {
        this.width = width;
        this.height = height;
        this.random = new Random(seed);

        intBuffer = Buffers.newDirectIntBuffer(width * height);

        // random for texture variation
        final float textureVariant = random.nextFloat();

        final int[] data = new int[width * height]; // 4 byte
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final Vector3f vector = getNormalVector(x, y);
                Color color = celestialType.getColor(vector.x, vector.y, vector.z, textureVariant);
                setPixel(x, y, color, width, height, data);
            }
        }
        intBuffer.put(data);
        intBuffer.flip();
        intBuffer.rewind();
    }

    @Override
    public int getHandle(GL2ES2 gl) {
        if (textureId == -1) {
            setup(gl);
        }
        return textureId;
    }

    private void setup(GL2ES2 gl) {
        // Create a new texture object in memory and bind it
    	int[] iBuff = new int[1];
        gl.glGenTextures(1, iBuff, 0);
        textureId = iBuff[0];
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textureId);

        // all RGB bytes are aligned to each other and each component is 1 byte
        gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
        // upload the texture data and generate mip maps (for scaling)
        //gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA8, width, height, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_INT_8_8_8_8_REV, intBuffer);
        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA8, width, height, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_INT_8_8_8_8_REV, intBuffer);
        gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);

        //Setup wrap mode for the ST coordinate system
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT); // GL_CLAMP
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT); // GL_CLAMP

        // Setup what to do when the texture has to be scaled
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
    }

    /**
     * this does a 2D to 3D transformation
     * 0/0 is top left, the whole texture is wrapped around the celestial object
     *
     * @return a vector with each element [0..1]
     */
    private final Vector3f getNormalVector(int x, int y) {
        final int yRange = height - 1;
        final int xRange = width - 1;
        return  getNormalVector((float)x/(float)xRange, (float)y/(float)yRange);
    }

    /**
     * convert a 2D texture position into a 3D sphere vector
     *
     * 0/0 is top left, the whole texture is wrapped around a sphere
     *
     * height, width of the texture in pixel x,y the position inside the texture
     */
    private Vector3f getNormalVector(float x, float y) {
        final float latitude = (float) Math.PI * y; // [0 .. PI] (north-south)
        final float longitude = (float) Math.PI * 2 * x; // [0 .. TWO_PI]

        // 0 -> 0; HALF_PI -> 1 ; PI -> 0
        final float xx = (float) Math.sin(longitude) * (float) Math.sin(latitude);
        final float yy = (float) Math.cos(latitude); // 0 -> 1; HALF_PI -> 0 ; PI -> -1
        final float zz = (float) Math.cos(longitude) * (float) Math.sin(latitude); // 0 -> 1;...

        return new Vector3f(xx, yy, zz);
    }


    private void setPixel(int x, int y, Color color, int width, int height, int[] data) {
        y = height - y - 1;
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x > width - 1) {
            x = width - 1;
        }
        if (y > height - 1) {
            y = height - 1;
        }

        final int i = x + y * width;
        int value = 0;
        value = value | 0xff & color.getAlpha();
        value = value << 8;
        value = value | 0xff & color.getBlue();
        value = value << 8;
        value = value | 0xff & color.getGreen();
        value = value << 8;
        value = value | 0xff & color.getRed();
        data[i] = value;
    }

}
