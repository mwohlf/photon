package net.wohlfart.photon.resources;

import net.wohlfart.photon.texture.ISphereSurfaceColor;
import net.wohlfart.photon.tools.Color;


public enum TextureIdent implements ISphereSurfaceColor {
	GAS_PLANET(new GasPlanetTexture("GAS1", Color.BLUE, Color.RED)),
	CONTINENTAL(new ContinentalPlanetTexture()),
	SUN_CLASS_G(new MultiColorSurfacePlanetTexture("SUN_CLASS_G",
			Color.WHITE, Color.YELLOW, Color.RED, Color.BLACK)),
	SUN_CLASS_M(new MultiColorSurfacePlanetTexture("SUN_CLASS_M",
			Color.WHITE, Color.YELLOW)),
	YELLOW(new MonoColorSurfaceColor("YELLOW", Color.YELLOW)),
	BLUE(new MonoColorSurfaceColor("BLUE", Color.BLUE)),
	RED(new MonoColorSurfaceColor("RED", Color.RED)),
	GREEN(new MonoColorSurfaceColor("GREEN", Color.GREEN));

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
