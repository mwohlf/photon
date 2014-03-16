package net.wohlfart.photon.tools;

import java.io.Serializable;

// to hide the underlying platform from clients
public class Quaternion extends com.jogamp.opengl.math.Quaternion implements Serializable {
	private static final long serialVersionUID = 1L;

	public Quaternion() {
		super();
	}

	public Quaternion(Quaternion rot) {
		super(rot);
	}

	public void set(float x, float y, float z, float w) {
		setX(x);
		setY(y);
		setZ(z);
		setW(w);
	}

}
