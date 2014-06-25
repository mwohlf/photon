package net.wohlfart.photon.entity.stellar;

import net.wohlfart.photon.ShaderIdent;
import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.resources.TextureIdent;
import net.wohlfart.photon.texture.TextureIdentifier;

public class PlanetClassM extends ProceduralCelestial {


    @Override
    public void setup() {
    	if (seed < 0) {
    		withSeed(2);
    	}
     	if (type == null) {
    		withType(TextureIdent.CONTINENTAL);
    	}
     	if (Float.isNaN(size)) {
     		float min = 2;
     		float max = 10;
     		withSize(random.nextFloat() * (max - min) + min);
    	}

        lod = 6;
        final TextureIdentifier textureId = TextureIdentifier.create(size * 25, type.getId(), seed);
        final IGeometry geometry = new Sphere(getSize(), lod, VertexFormat.VERTEX_P3C0N3T2, StreamFormat.TRIANGLES);
        final RenderCommand renderUnit = new RenderCommand(geometry, textureId, ShaderIdent.VERTEX_LIGHT_SHADER);
        renderCommands.add(renderUnit);
    }
}
