package net.wohlfart.photon.resources;

import java.awt.Color;

import net.wohlfart.photon.texture.simplex.AbstractSimplexSphereTexture;
import net.wohlfart.photon.tools.ColorGradient;

public class MultiColorSurfacePlanetTexture extends AbstractSimplexSphereTexture {
	private static final float PERSISTENCE = 0.5f;
	private static final int OCTAVES = 8;

	protected final ColorGradient gradient;
	protected final String id;

	// package private, use the enum in TextureIdent
	MultiColorSurfacePlanetTexture(String id, Color... colors) {
		this.id = id;
		gradient = new ColorGradient(colors);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Color getColor(float x, float y, float z, float textureVariant) {
        final double noise = createNoise(x, y, z, textureVariant, PERSISTENCE, OCTAVES);
        return gradient.getColor(noise);
	}

}
