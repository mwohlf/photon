package net.wohlfart.photon.shader;

import java.awt.Color;

import javax.vecmath.Vector3f;

public class VertexLightValue implements IUniformValue {

	private final String name;
	private final float attenuation;
	private final Vector3f position;
	private final Color color;
	private final Vector3f diffuse;

	public VertexLightValue(
			float attenuation,
			Vector3f position,
			Color color,
			Vector3f diffuse) {
		this.name = ShaderParser.VERTEX_LIGHT;
		this.attenuation = attenuation;
		this.position = position;
		this.color = color;
		this.diffuse = diffuse;
	}

	@Override
	public String getKey() {
		return name;
	}

	@Override
	public void accept(IShaderProgram shader) {
		Integer index = shader.nextLightSlot();
		Integer location;

		location = shader.getUniformLocation(name + "[" + index + "].attentuation");
		if (location != null) {
			shader.getGl().glUniform1f(location, attenuation);
		}

		location = shader.getUniformLocation(name + "[" + index + "].position");
		if (location != null) {
			shader.getGl().glUniform3f(location, position.x, position.y, position.z);
		}

		location = shader.getUniformLocation(name + "[" + index + "].color");
		if (location != null) {
			shader.getGl().glUniform4f(location, color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f);
		}

		location = shader.getUniformLocation(name + "[" + index + "].diffuse");
		if (location != null) {
			shader.getGl().glUniform3f(location, diffuse.x, diffuse.y, diffuse.z);
		}


		// handle.getShader().getGl().glUniformMatrix4fv(handle.getLocation(), 1, false, modelview, 0);
		// TODO
	}

}
