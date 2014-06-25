package net.wohlfart.photon.entity.stellar;

import java.awt.Color;

import net.wohlfart.photon.ShaderIdent;
import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.node.Corona;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.resources.TextureIdent;
import net.wohlfart.photon.texture.TextureIdentifier;

public class SunClassG extends Sun {

    @Override
    public void setup() {
		withCorona(new Corona().withThinkness(6f).withColor(Color.RED));

    	if (seed < 0) {
    		withSeed(2);
    	}
     	if (type == null) {
    		withType(TextureIdent.SUN_CLASS_G);
    	}
     	if (Float.isNaN(size)) {
     		float min = 2;
     		float max = 10;
     		withSize(random.nextFloat() * (max - min) + min);
    	}

        lod = 6;
        final TextureIdentifier textureId = TextureIdentifier.create(size * 25, type.getId(), seed);
        final IGeometry geometry = new Sphere(getSize(), lod, VertexFormat.VERTEX_P3C0N3T2, StreamFormat.TRIANGLES);
        final RenderCommand renderUnit = new RenderCommand(geometry, textureId, ShaderIdent.TEXTURE_SHADER);
        renderCommands.add(renderUnit);
    }

}
