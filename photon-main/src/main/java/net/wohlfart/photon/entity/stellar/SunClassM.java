package net.wohlfart.photon.entity.stellar;


import net.wohlfart.photon.ShaderIdent;
import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.node.Corona;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.resources.TextureIdent;
import net.wohlfart.photon.texture.TextureIdentifier;
import net.wohlfart.photon.tools.Color;

public class SunClassM extends Sun {
	public static final float MIN_SIZE = 2;
	public static final float MAX_SIZE = 10;
	public static final float CORONA_ASPECT = 2f;

    @Override
    public void setup() {
    	if (seed < 0) {
    		withSeed(2);
    	}
     	if (type == null) {
    		withType(TextureIdent.SUN_CLASS_M);
    	}
     	if (Float.isNaN(size)) {
     		assert random != null: "need to set random";
     		withSize(random.nextFloat() * (MAX_SIZE - MIN_SIZE) + MIN_SIZE);
    	}
     	if (corona == null) {
     		withCorona(new Corona().withThinkness(size * CORONA_ASPECT).withColor(Color.YELLOW));
     	}

        lod = 6;
        final TextureIdentifier textureId = TextureIdentifier.create(size * 25, type.getId(), seed);
        final IGeometry geometry = new Sphere(getSize(), lod, VertexFormat.VERTEX_P3C0N3T2, StreamFormat.TRIANGLES);
        final RenderCommand renderUnit = new RenderCommand(geometry, textureId, ShaderIdent.TEXTURE_SHADER);
        renderCommands.add(renderUnit);
    }

}
