package net.wohlfart.photon.shader;

import javax.vecmath.Vector3f;

public class FragmentLightValue implements IUniformValue {

	private final int count;
	private final Vector3f position;
	private final Vector3f color; // rly?

	FragmentLightValue(int count, Vector3f position, Vector3f color) {
		this.count = count;
		this.position = position;
		this.color = color;
	}

	@Override
	public void accept(IUniformHandle handle) {
		// TODO Auto-generated method stub

	}

}
