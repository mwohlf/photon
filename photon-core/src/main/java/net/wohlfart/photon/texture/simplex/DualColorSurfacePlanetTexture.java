package net.wohlfart.photon.texture.simplex;

import java.awt.Color;

import net.wohlfart.photon.tools.ColorGradient;

public class DualColorSurfacePlanetTexture extends AbstractSimplexSphereTexture {

	protected final ColorGradient gradient;
	protected final String id;

	public DualColorSurfacePlanetTexture(String id, Color colorA, Color colorB) {
		this.id = id;
		gradient = new ColorGradient(colorA, colorB);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Color getColor(float x, float y, float z, float textureVariant) {
        final double noise = createNoise(x, y, z, textureVariant, 0.5f, 5);
        return gradient.getColor(noise);
	}

}
