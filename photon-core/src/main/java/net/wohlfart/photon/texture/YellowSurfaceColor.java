package net.wohlfart.photon.texture;

import java.awt.Color;

public class YellowSurfaceColor implements ISphereSurfaceColor {

	Color yellow = Color.YELLOW;

	@Override
	public Color getColor(float x, float y, float z, float textureVariant) {
		return yellow;
	}

}
