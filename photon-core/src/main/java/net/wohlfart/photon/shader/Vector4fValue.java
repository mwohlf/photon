package net.wohlfart.photon.shader;

import javax.media.opengl.GL2ES2;
import javax.vecmath.Vector4f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vector4fValue implements IUniformValue {
	protected static final Logger LOGGER = LoggerFactory.getLogger(Vector4fValue.class);

	protected final String key;
	protected final String name;
    protected final Vector4f vector;

    public Vector4fValue(String key, Vector4f vector) {
    	this.key = key;
    	this.name = key;
        this.vector = vector;
    }

	@Override
	public String getKey() {
		return key;
	}

    @Override
    public void accept(IShaderProgram shader) {
		final GL2ES2 gl2 = shader.getGl();
        final Integer location = shader.getUniformLocation(name);
        if (location == null) {
        	LOGGER.debug("location for {} not found, ignoring", name);
        	return;
        }
        LOGGER.debug("setting vector4f uniform '{}' location is '{}', values are '{}'", name, location, vector);
		gl2.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
    }
}
