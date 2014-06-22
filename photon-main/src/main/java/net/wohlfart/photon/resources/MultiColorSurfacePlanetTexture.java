package net.wohlfart.photon.resources;

import java.awt.Color;

import net.wohlfart.photon.texture.simplex.AbstractSimplexSphereTexture;
import net.wohlfart.photon.tools.ColorGradient;

public class MultiColorSurfacePlanetTexture extends AbstractSimplexSphereTexture {
	private static final float DEFAULT_PERSISTENCE = 0.5f;
	private static final int DEFAULT_OCTAVES = 8;

	protected final ColorGradient gradient;
	protected final String id;
	protected float persistence;
	protected int octaves;

	// package private, use the enum in TextureIdent
	MultiColorSurfacePlanetTexture(String id, Color... colors) {
		this(id, DEFAULT_PERSISTENCE, DEFAULT_OCTAVES, colors);
	}

	MultiColorSurfacePlanetTexture(String id, float persistence, int octaves, Color... colors) {
		this.persistence = persistence;
		this.octaves = octaves;
		this.id = id;
		this.gradient = new ColorGradient(colors);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Color getColor(float x, float y, float z, float textureVariant) {
        final double noise = createNoise(x, y, z, textureVariant, persistence, octaves);
        return gradient.getColor(noise);
	}

}
