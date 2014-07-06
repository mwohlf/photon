package net.wohlfart.photon.texture;

import net.wohlfart.photon.tools.Color;


public interface ISphereSurfaceColor {

	String getId();

	Color getColor(float x, float y, float z, float textureVariant);

}
