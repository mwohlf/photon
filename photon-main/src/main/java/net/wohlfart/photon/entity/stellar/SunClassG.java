package net.wohlfart.photon.entity.stellar;

import java.awt.Color;
import java.util.Random;

import net.wohlfart.photon.ShaderIdent;
import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.node.Corona;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.resources.TextureIdent;
import net.wohlfart.photon.texture.ISphereSurfaceColor;
import net.wohlfart.photon.texture.TextureIdentifier;


// see: http://en.wikipedia.org/wiki/Stellar_classification#Modern_interpretation
public class SunClassG extends ProceduralCelestial {

    protected ISphereSurfaceColor type;
    protected long seed = -1;
    protected Random random;

    @Override
    public void setup() {
		withCorona(new Corona().withThinkness(6f).withColor(Color.YELLOW));

    	if (seed < 0) {
    		withSeed(2);
    	}
     	if (type == null) {
    		withType(TextureIdent.YELLOW);
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

    @Override
	public ProceduralCelestial withType(ISphereSurfaceColor type) {
        this.type = type;
        return this;
    }

    @Override
	public ProceduralCelestial withSeed(long seed) {
    	this.seed = seed;
    	this.random = new Random(seed);
    	return this;
    }

}
