package net.wohlfart.photon.texture;

import java.awt.Color;

public interface ISphereSurfaceColor {

	String getId();

	Color getColor(float x, float y, float z, float textureVariant);

}
