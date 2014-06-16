package net.wohlfart.photon.resources;

import java.awt.Color;

import net.wohlfart.photon.texture.simplex.AbstractSimplexSphereTexture;
import net.wohlfart.photon.tools.ColorGradient;

public class ContinentalPlanetTexture extends AbstractSimplexSphereTexture {

	public static final String ID = "CONTINENTAL_PLANET";

	protected ColorGradient gradient = new ColorGradient(
            new Color(0, 0, 0),
            new Color(0, 0, 100),
            new Color(0, 0, 255),
            new Color(10, 10, 255),
            new Color(180, 180, 180),
            new Color(10, 255, 10),
            new Color(0, 255, 0),
            new Color(0, 50, 0));

	// package private, use the enum in TextureIdent
	ContinentalPlanetTexture() {
		maxRadius = 6f;
        minRadius = 3f;
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public Color getColor(float x, float y, float z, float textureVariant) {
        final double groundNoise = createNoise(x, (float) Math.asin(y), z, textureVariant, 0.5f, 4);
        final Color ground = gradient.getColor(groundNoise);
        final double skyNoise = createNoise(x * 2, (float) Math.asin(y) * 4, z * 2, textureVariant, 0.2f, 3);
        return ColorGradient.linearGradient(ground, Color.WHITE, skyNoise);
	}

}
