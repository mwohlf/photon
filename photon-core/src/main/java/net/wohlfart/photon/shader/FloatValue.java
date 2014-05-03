package net.wohlfart.photon.shader;


public class FloatValue implements IUniformValue {
    private final Float value;

    public FloatValue(float value) {
        this.value = value;
    }

	@Override
	public void accept(IUniformHandle handle) {
        assert (value != null) : "Uniform '" + handle.getName() + "' is empty";
        handle.getShader().getGl().glUniform1f(handle.getLocation(), value);
	}

}
