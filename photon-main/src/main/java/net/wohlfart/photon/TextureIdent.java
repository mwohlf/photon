package net.wohlfart.photon;

import java.awt.Color;

import net.wohlfart.photon.resources.ResourceManager;
import net.wohlfart.photon.texture.ISphereSurfaceColor;
import net.wohlfart.photon.texture.simplex.ContinentalPlanetTexture;
import net.wohlfart.photon.texture.simplex.GasPlanetTexture;
import net.wohlfart.photon.texture.simplex.MonoColorSurfaceColor;

public enum TextureIdent implements ISphereSurfaceColor {
	GAS_PLANET(new GasPlanetTexture("GAS1", Color.BLUE, Color.RED)),
	CONTINENTAL(new ContinentalPlanetTexture()),
	YELLOW(new MonoColorSurfaceColor("YELLOW", Color.YELLOW));

	final ISphereSurfaceColor delegate;

	TextureIdent(ISphereSurfaceColor delegate) {
		this.delegate = delegate;
		ResourceManager.INSTANCE.registerSimplexTexture(delegate);
	}

	@Override
	public String getId() {
		return delegate.getId();
	}

	@Override
	public Color getColor(float x, float y, float z, float textureVariant) {
		return delegate.getColor(x, y, z, textureVariant);
	}

}
