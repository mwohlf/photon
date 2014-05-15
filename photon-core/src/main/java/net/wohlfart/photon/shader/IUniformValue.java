package net.wohlfart.photon.shader;


public interface IUniformValue {

    public static final IUniformValue UNIFORM_NULL_VALUE = new IUniformValue() {
		@Override
		public String getKey() {
			return null;
		}
		@Override
        public void accept(IShaderProgram shader) {
            // do nothing
        }
        @Override
        public String toString() {
        	return "NullValue@" + System.identityHashCode(this);
        }
    };

    // a unique key identifying the uniform value, used to store the value in
    // a Hashmap which is part of the graphic context
    String getKey();

    // setting one or more values in the shader
    // activating texture slots, enabling lights, ...
    void accept(IShaderProgram shader);

}