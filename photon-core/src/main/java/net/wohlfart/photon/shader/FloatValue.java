package net.wohlfart.photon.shader;


public class FloatValue implements IUniformValue {
    private final Float value;
    private final String name;

    public FloatValue(String name, float value) {
    	this.name = name;
        this.value = value;
    }

	@Override
	public void accept(IShaderProgram shader) {
        assert (value != null) : "Uniform '" + name + "' is empty";
        shader.getGl().glUniform1f(shader.getUniformLocation(name), value);
	}

}
