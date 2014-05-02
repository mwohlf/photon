package net.wohlfart.photon.entity;

import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;

@SuppressWarnings("unused")
public class Earth extends AbstractCelestial {

    private static final TextureIdentifier EARTH_LOWRES =  TextureIdentifier.create("gfx/textures/earth-512.jpg");

    private static final ITextureIdentifier EARTH_HIRES =  TextureIdentifier.create("gfx/textures/earth-1024.jpg");

    @Override
    public void setup() {
        lod = 6;
        renderCommands.add(new RenderCommand( new Sphere(5, lod, VertexFormat.VERTEX_P3C0N0T2, StreamFormat.TRIANGLES), EARTH_HIRES));
    }

}
