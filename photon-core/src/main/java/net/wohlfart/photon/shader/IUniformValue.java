package net.wohlfart.photon.shader;

public interface IUniformValue {

	public interface IUniformHandle {

		int getLocation(); // the uniform location inside the shader

		String getName();

		IShaderProgram getShader();

	}


    public static final IUniformValue SHADER_UNIFORM_NULL_VALUE = new NullValue();

    void accept(IUniformValue.IUniformHandle handle);

    static class NullValue implements IUniformValue {
        @Override
        public void accept(IUniformHandle handle) {
            // do nothing
        }
        @Override
        public String toString() {
        	return "NullValue@" + System.identityHashCode(this);
        }
    }

}