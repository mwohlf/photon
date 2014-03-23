package net.wohlfart.photon.node;

import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.shader.ShaderIdentifier;

public class Corona extends AbstractRenderElement {

	public static final ShaderIdentifier TEXTURE_SHADER_ID = ShaderIdentifier.create("shader/texture.vert", "shader/texture.frag");

    private float planetSize;
    private float thinkness = 1;


    public Corona() {
        renderConfig = IRenderConfig.DEFAULT_3D;
        shaderId = TEXTURE_SHADER_ID;
    }

    @Override // lazy create the geometry since we don't know the size in the constructor
    public IGeometry getGeometry() {
        if (geometry == null) {
            // geometry = new Sphere(planetSize + thinkness, 6, VertexFormat.VERTEX_P3C0N3T2, StreamFormat.TRIANGLES);
            geometry = new Sphere(planetSize + thinkness, 6, VertexFormat.VERTEX_P3C0N0T0, StreamFormat.LINES);
        }
        return geometry;
    }

    public void setPlanetSize(float size) {
        planetSize = size;
        geometry = null;
    }

    public Corona withThinkness(float thinkness) {
        this.thinkness = thinkness;
        geometry = null;
        return this;
    }


    @Override
    public String toString() {
        return this.getClass().getName() + " [zOrder=" + zOrder
                + " renderConfig=" + renderConfig
                + " planetSize=" + planetSize
                + " thinkness=" + thinkness
                + "]";
    }

}
