package net.wohlfart.photon.entity;

import java.util.Random;

import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.texture.CelestialType;
import net.wohlfart.photon.texture.TextureIdentifier;

public class ProceduralCelestial extends AbstractCelestial {

    protected CelestialType type;
    protected long seed = -1;
    protected Random random;

    @Override
    public void setup() {
    	if (seed < 0) {
    		withSeed(2);
    	}
     	if (type == null) {
    		withType(CelestialType.CONTINENTAL_PLANET);
    	}
     	if (size < 0) {
     		float min = type.minRadius;
     		float max = type.maxRadius;
     		withSize(random.nextFloat() * (max - min) + min);
    	}

        lod = 6;
        final TextureIdentifier textureId = TextureIdentifier.create(size * 25, type, seed);
        final IGeometry geometry = new Sphere(getSize(), lod, VertexFormat.VERTEX_P3C0N0T2, StreamFormat.TRIANGLES);
        final RenderCommand renderUnit = new RenderCommand(geometry, textureId);
        renderCommands.add(renderUnit);
    }

    public ProceduralCelestial withType(CelestialType type) {
        this.type = type;
        return this;
    }

    public ProceduralCelestial withSeed(long seed) {
    	this.seed = seed;
    	this.random = new Random(seed);
    	return this;
    }

}
