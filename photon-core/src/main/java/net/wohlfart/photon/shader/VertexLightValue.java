package net.wohlfart.photon.shader;

import java.awt.Color;

import javax.vecmath.Vector3f;

public class VertexLightValue implements IUniformValue {

	private final int count;
	private final float attenuation;
	private final Vector3f position;
	private final Color color;
	private final Vector3f diffuse;

	VertexLightValue(
			int count,
			float attenuation,
			Vector3f position,
			Color color,
			Vector3f diffuse) {
		this.count = count;
		this.attenuation = attenuation;
		this.position = position;
		this.color = color;
		this.diffuse = diffuse;
	}

	@Override
	public void accept(IUniformHandle handle) {
        // handle.getShader().getGl().glUniformMatrix4fv(handle.getLocation(), 1, false, modelview, 0);
		// TODO
	}

}
