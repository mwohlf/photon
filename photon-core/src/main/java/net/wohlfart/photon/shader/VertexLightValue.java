package net.wohlfart.photon.shader;

import javax.media.opengl.GL2ES2;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import net.wohlfart.photon.GenericException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertexLightValue implements IUniformValue {
	protected static final Logger LOGGER = LoggerFactory.getLogger(VertexLightValue.class);

	// default light used to fill the light array in the shader
	public final static VertexLightValue VERTEX_LIGHT_NULL = new VertexLightValue(
			"NULL",
			0,                      // no light reduction since we have no light anyways
			new Vector3f(0,0,0),    // position at origin
			new Vector4f(0,0,0,0),  // no directional light
			new Vector3f(0,0,0));   // no diffuse light


	// unique key in the GraphicContext
	private final String key;

	// name of the vertex light array
	private final String name  = ShaderParser.VERTEX_LIGHT;

	// amount of light reduction depending on the distance to the position
	private final float attenuation;

	// light position, origin of the directional light
	private final Vector3f position;

	// light color, alpha defines the glow strength
	private final Vector4f color;

	// non directional light
	private final Vector3f diffuse;

	public VertexLightValue(
			String key,
			float attenuation,
			Vector3f position,
			Vector4f color,
			Vector3f diffuse) {
		this.key = key;
		this.attenuation = attenuation;
		this.position = position;
		this.color = color;
		this.diffuse = diffuse;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void accept(IShaderProgram shader) {
		Integer index = shader.nextLightSlot();
		Integer location = null;
		GL2ES2 gl2 = shader.getGl();

		try { // in debug mode we get an exception here if we provide wrong parameter count...

			location = shader.getUniformLocation(name + "[" + index + "].attenuation");
			if (location != null) {
				LOGGER.debug("setting attenuation: {}", attenuation);
				gl2.glUniform1f(location, attenuation);
			} else {
				LOGGER.debug("attenuation not found");
			}

			location = shader.getUniformLocation(name + "[" + index + "].position");
			if (location != null) {
				LOGGER.debug("setting position: {}", position);
				gl2.glUniform3f(location, position.x, position.y, position.z);
			} else {
				LOGGER.debug("position not found");
			}

			location = shader.getUniformLocation(name + "[" + index + "].color");
			if (location != null) {
				LOGGER.debug("setting color: {}", color);
				gl2.glUniform4f(location, color.x, color.y, color.z, color.w);
			} else {
				LOGGER.debug("color not found");
			}

			location = shader.getUniformLocation(name + "[" + index + "].diffuse");
			if (location != null) {
				LOGGER.debug("setting diffuse: {}", diffuse);
				gl2.glUniform3f(location, diffuse.x, diffuse.y, diffuse.z);
			} else {
				LOGGER.debug("diffuse not found");
			}

		} catch (Exception ex) {
			throw new GenericException("Error setting vertex light value, index: '" + index + "' "
					+ ", location: '" + location + "' ", ex);
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
