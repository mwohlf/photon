package net.wohlfart.photon.shader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FloatValue implements IUniformValue {
	protected static final Logger LOGGER = LoggerFactory.getLogger(FloatValue.class);

    private final String key;
    private final String name;
    private final float value;

    public FloatValue(String key, float value) {
    	this.key = key;
    	this.name = key;
        this.value = value;
    }

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void accept(IShaderProgram shader) {
        assert (!Float.isNaN(value)) : "Uniform '" + name + "' is empty";
        final Integer location = shader.getUniformLocation(name);
        if (location != null) {
            shader.getGl().glUniform1f(location, value);
        } else {
        	LOGGER.debug("no location found for '{}' value was '{}', shaderId is '{}'", name, value, shader.getId());
        }
	}

}
