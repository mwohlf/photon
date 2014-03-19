package net.wohlfart.photon.shader;



// the uniform handle is the name and location of a uniform in a specific shader
// uniforms also include the textures
public class UniformHandle implements IUniformValue.IUniformHandle {


    final IShaderProgram shader;

    final String name;

    final int location;


    public UniformHandle(IShaderProgram shader, String name, int location) {
        if (location < 0) {
            throw new IllegalArgumentException("uniform: '" + name + "' has location '" + location + "'");
        }
        this.shader = shader;
        this.name = name;
        this.location = location;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [shaderProgramId=" + shader.getId()
                + ", name=" + name
                + ", location=" + location + "]";
    }


    // uniform values are values that can be assigned to uniform handlers
    // they are independent from a specific shader calling accept for a specific hanle and shader
    // assigns them

    @Override
	public int getLocation() {
		return location;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IShaderProgram getShader() {
		return shader;
	}

}
