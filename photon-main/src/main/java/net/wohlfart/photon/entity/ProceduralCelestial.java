package net.wohlfart.photon.entity;

import java.util.Random;

import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.texture.ISphereSurfaceColor;
import net.wohlfart.photon.texture.TextureIdentifier;
import net.wohlfart.photon.texture.simplex.ContinentalPlanetTexture;

public class ProceduralCelestial extends AbstractCelestial {

    protected ISphereSurfaceColor type;
    protected long seed = -1;
    protected Random random;

    @Override
    public void setup() {
    	if (seed < 0) {
    		withSeed(2);
    	}
     	if (type == null) {
    		withType(new ContinentalPlanetTexture());
    	}
     	if (size < 0) {
     		float min = 2;
     		float max = 10;
     		withSize(random.nextFloat() * (max - min) + min);
    	}

        lod = 6;
        final TextureIdentifier textureId = TextureIdentifier.create(size * 25, type.getId(), seed);
        final IGeometry geometry = new Sphere(getSize(), lod, VertexFormat.VERTEX_P3C0N0T2, StreamFormat.TRIANGLES);
        final RenderCommand renderUnit = new RenderCommand(geometry, textureId);
        renderCommands.add(renderUnit);
    }

    public ProceduralCelestial withType(ISphereSurfaceColor type) {
        this.type = type;
        return this;
    }

    public ProceduralCelestial withSeed(long seed) {
    	this.seed = seed;
    	this.random = new Random(seed);
    	return this;
    }

}
