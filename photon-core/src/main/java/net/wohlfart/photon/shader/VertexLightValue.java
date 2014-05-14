package net.wohlfart.photon.shader;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertexLightValue implements IUniformValue {
	protected static final Logger LOGGER = LoggerFactory.getLogger(VertexLightValue.class);

	private final String name;
	private final float attenuation;
	private final Vector3f position;
	private final Vector4f color;
	private final Vector3f diffuse;

	public VertexLightValue(
			float attenuation,
			Vector3f position,
			Vector4f color,
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

		location = shader.getUniformLocation(name + "[" + index + "].attenuation");
		if (location != null) {
			LOGGER.debug("setting attenuation: {}", attenuation);
			shader.getGl().glUniform1f(location, attenuation);
		} else {
			LOGGER.debug("attenuation not found");
		}

		location = shader.getUniformLocation(name + "[" + index + "].position");
		if (location != null) {
			LOGGER.debug("setting position: {}", position);
	        System.out.println("lightPos: " + position.x + "," + position.y + "," + position.z);
			shader.getGl().glUniform3f(location, position.x, position.y, position.z);
		} else {
			LOGGER.debug("position not found");
		}

		location = shader.getUniformLocation(name + "[" + index + "].color");
		if (location != null) {
			LOGGER.debug("setting color: {}", color);
			shader.getGl().glUniform4f(location, color.x, color.y, color.z, color.w);
		} else {
			LOGGER.debug("color not found");
		}

		location = shader.getUniformLocation(name + "[" + index + "].diffuse");
		if (location != null) {
			LOGGER.debug("setting diffuse: {}", diffuse);
			shader.getGl().glUniform3f(location, diffuse.x, diffuse.y, diffuse.z);
		} else {
			LOGGER.debug("diffuse not found");
		}


		// handle.getShader().getGl().glUniformMatrix4fv(handle.getLocation(), 1, false, modelview, 0);
		// TODO
	}

	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}

}
