package net.wohlfart.photon.entity.stellar;

import java.awt.Color;

import net.wohlfart.photon.entity.AbstractCelestial;
import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.resources.ResourceManager;
import net.wohlfart.photon.texture.ISphereSurfaceColor;
import net.wohlfart.photon.texture.TextureIdentifier;
import net.wohlfart.photon.texture.simplex.MonoColorSurfaceColor;

// see: http://en.wikipedia.org/wiki/Stellar_classification#Modern_interpretation
public class SunClassG extends AbstractCelestial {

	long randomSeed = 1;
	ISphereSurfaceColor color = new MonoColorSurfaceColor("YELLOW", Color.YELLOW);

	@Override
	public void setup() { // TODO: maybe call setup with the renderCommands?
		assert size > 0 : "size not set";
        lod = 6;
        ResourceManager.INSTANCE.registerSimplexTexture(color);
        final TextureIdentifier textureId = TextureIdentifier.create(size * 25, color.getId(), randomSeed);
        renderCommands.add(new RenderCommand( new Sphere(5, lod, VertexFormat.VERTEX_P3C0N0T2, StreamFormat.TRIANGLES), textureId));
	}

}
