package net.wohlfart.photon.render.entity;

import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.texture.CelestialType;
import net.wohlfart.photon.texture.TextureIdentifier;

public class ProceduralCelestial extends AbstractCelestial {

    protected CelestialType type = CelestialType.CONTINENTAL_PLANET;

    @Override
    public void setup() {
        lod = 6;
        final TextureIdentifier textureId = TextureIdentifier.create(40, type, 2);
        final IGeometry geometry = new Sphere(getSize(), lod, VertexFormat.VERTEX_P3C0N0T2, StreamFormat.TRIANGLES);
        final RenderCommand renderUnit = new RenderCommand(geometry, textureId);
        renderCommands.add(renderUnit);
    }

    public ProceduralCelestial withType(CelestialType type) {
        this.type = type;
        return this;
    }

}
