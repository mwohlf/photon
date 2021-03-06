package net.wohlfart.photon.shader;

import javax.vecmath.Vector3f;

public class FragmentLightValue implements IUniformValue {

	private final String name;
	private final int count;
	private final Vector3f position;
	private final Vector3f color; // rly?

	public FragmentLightValue(String name, int count, Vector3f position, Vector3f color) {
		this.name = name;
		this.count = count;
		this.position = position;
		this.color = color;
	}

	@Override
	public String getKey() {
		return name + count;
	}

	@Override
	public void accept(IShaderProgram shader) {
		// TODO Auto-generated method stub

	}

}
