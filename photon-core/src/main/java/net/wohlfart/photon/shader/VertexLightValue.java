package net.wohlfart.photon.shader;

import java.awt.Color;

import javax.vecmath.Vector3f;

public class VertexLightValue implements IUniformValue {

	private final String name;
	private final int count;
	private final float attenuation;
	private final Vector3f position;
	private final Color color;
	private final Vector3f diffuse;

	public VertexLightValue(
			String name,
			int count,
			float attenuation,
			Vector3f position,
			Color color,
			Vector3f diffuse) {
		this.name = name;
		this.count = count;
		this.attenuation = attenuation;
		this.position = position;
		this.color = color;
		this.diffuse = diffuse;
	}

	@Override
	public String getKey() {
		return name + count;
	}

	@Override
	public void accept(IShaderProgram shader) {
        // handle.getShader().getGl().glUniformMatrix4fv(handle.getLocation(), 1, false, modelview, 0);
		// TODO
	}

}
