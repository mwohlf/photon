package net.wohlfart.photon.resources;

import java.awt.Color;

import net.wohlfart.photon.texture.simplex.AbstractSimplexSphereTexture;
import net.wohlfart.photon.tools.ColorGradient;

public class GasPlanetTexture extends AbstractSimplexSphereTexture {

	protected final String id;
	protected final ColorGradient gradient;

	// package private, use the enum in TextureIdent
	GasPlanetTexture(String id, Color colorA, Color colorB) {
		this.id = id;
		this.gradient = new ColorGradient(colorA, colorB);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Color getColor(float x, float y, float z, float textureVariant) {
        final double noise = createNoise(x / 1.5f, y * 10, z / 1.5f, textureVariant, 0.5f, 5);
        return gradient.getColor(noise);
	}

}
