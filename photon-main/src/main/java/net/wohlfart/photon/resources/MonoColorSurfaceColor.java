package net.wohlfart.photon.resources;

import net.wohlfart.photon.texture.ISphereSurfaceColor;
import net.wohlfart.photon.tools.Color;


public class MonoColorSurfaceColor implements ISphereSurfaceColor {

	private final Color color;
	private final String id;

	// package private, use the enum in TextureIdent
	MonoColorSurfaceColor(String id, Color yellow) {
		this.id = id;
		this.color = yellow;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Color getColor(float x, float y, float z, float textureVariant) {
		return color;
	}

}
