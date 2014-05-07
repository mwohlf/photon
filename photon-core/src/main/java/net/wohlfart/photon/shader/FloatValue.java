package net.wohlfart.photon.shader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FloatValue implements IUniformValue {
	protected static final Logger LOGGER = LoggerFactory.getLogger(FloatValue.class);

    private final Float value;
    private final String name;

    public FloatValue(String name, float value) {
    	this.name = name;
        this.value = value;
    }

	@Override
	public String getKey() {
		return name;
	}

	@Override
	public void accept(IShaderProgram shader) {
        assert (value != null) : "Uniform '" + name + "' is empty";
        Integer location = shader.getUniformLocation(name);
        if (location != null) {
            shader.getGl().glUniform1f(location, value);
        } else {
        	LOGGER.info("no location found for '{}' value was '{}'", name, value);
        }
	}

}
