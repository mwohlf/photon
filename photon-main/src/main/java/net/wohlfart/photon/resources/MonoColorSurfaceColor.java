package net.wohlfart.photon.resources;

import java.awt.Color;

import net.wohlfart.photon.texture.ISphereSurfaceColor;

public class MonoColorSurfaceColor implements ISphereSurfaceColor {

	private final Color color;
	private final String id;

	// package private, use the enum in TextureIdent
	MonoColorSurfaceColor(String id, Color color) {
		this.id = id;
		this.color = color;
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
