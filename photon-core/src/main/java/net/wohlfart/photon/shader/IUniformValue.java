package net.wohlfart.photon.shader;


public interface IUniformValue {

    public static final IUniformValue SHADER_UNIFORM_NULL_VALUE = new NullValue();

    void accept(IShaderProgram shader);

    static class NullValue implements IUniformValue {
        @Override
        public void accept(IShaderProgram shader) {
            // do nothing
        }
        @Override
        public String toString() {
        	return "NullValue@" + System.identityHashCode(this);
        }
    }

}