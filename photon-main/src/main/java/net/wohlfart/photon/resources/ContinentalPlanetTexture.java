package net.wohlfart.photon.resources;

import net.wohlfart.photon.texture.simplex.AbstractSimplexSphereTexture;
import net.wohlfart.photon.tools.Color;
import net.wohlfart.photon.tools.ColorGradient;

public class ContinentalPlanetTexture extends AbstractSimplexSphereTexture {

	public static final String ID = "CONTINENTAL_PLANET";

	protected ColorGradient gradient = new ColorGradient(
            new Color(  0f/255f,   0f/255f,   0f/255f),
            new Color(  0f/255f,   0f/255f, 100f/255f),
            new Color(  0f/255f,   0f/255f, 255f/255f),
            new Color( 10f/255f,  10f/255f, 255f/255f),
            new Color(180f/255f, 180f/255f, 180f/255f),
            new Color( 10f/255f, 255f/255f,  10f/255f),
            new Color(  0f/255f, 255f/255f,   0f/255f),
            new Color(  0f/255f,  50f/255f,  0f/255f));

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
