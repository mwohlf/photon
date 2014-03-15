package net.wohlfart.photon.tools;

import java.awt.Color;

import javax.vecmath.Vector3f;

/**
 * tool class to create VAOs and VBOs
 *
 * see: http://www.lwjgl.org/wiki/index.php?title=The_Quad_interleaved
 */
public class VertexData { 

    private float[] xyzw = new float[] { 0f, 0f, 0f, 1f };
    private float[] rgba = new float[] { 1f, 1f, 1f, 1f };
    private float[] normal = new float[] { 1f, 0f, 0f };
    private float[] st = new float[] { 0f, 0f };


    public VertexData withXYZ(Vector3f vec) {
        this.withXYZW(vec.x, vec.y, vec.z, 1f);
        return this;
    }

    public VertexData withXYZ(float x, float y, float z) {
        this.withXYZW(x, y, z, 1f);
        return this;
    }

    public VertexData withNormal(Vector3f vec) {
        this.withNormal(vec.x, vec.y, vec.z);
        return this;
    }

    public VertexData withNormal(float x, float y, float z) {
        this.normal = new float[] { x, y, z };
        return this;
    }

    public VertexData withRGB(float r, float g, float b) {
        this.withRGBA(r, g, b, 1f);
        return this;
    }

    public VertexData withXYZW(float x, float y, float z, float w) {
        this.xyzw = new float[] { x, y, z, w };
        return this;
    }

    public VertexData withST(float s, float t) {
        this.st = new float[] { s, t };
        return this;
    }

    public VertexData withRGBA(float r, float g, float b, float a) {
        this.rgba = new float[] { r, g, b, 1f };
        return this;
    }


    public VertexData withColor(Color color) {
        this.rgba = new float[] {
                color.getRed()/255f,
                color.getGreen()/255f,
                color.getBlue()/255f,
                color.getAlpha()/255f };
        return this;
    }

    public float[] getXYZ() {
        return new float[] { this.xyzw[0], this.xyzw[1], this.xyzw[2] };
    }

    public float[] getXYZW() {
        return new float[] { this.xyzw[0], this.xyzw[1], this.xyzw[2], this.xyzw[3] };
    }

    public float[] getRGBA() {
        return new float[] { this.rgba[0], this.rgba[1], this.rgba[2], this.rgba[3] };
    }

    public float[] getST() {
        return new float[] { this.st[0], this.st[1] };
    }

    public float[] getNormal() {
        return new float[] { this.normal[0], this.normal[1], this.normal[2] };
    }

}
