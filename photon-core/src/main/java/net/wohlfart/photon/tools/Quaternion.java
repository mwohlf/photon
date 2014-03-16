package net.wohlfart.photon.tools;

import java.io.Serializable;

import javax.vecmath.Vector3f;

// to hide the underlying platform from clients
public class Quaternion extends com.jogamp.opengl.math.Quaternion implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final float EPSILON = 0.0000001f;
	private static final Vector3f X_AXIS = new Vector3f(1, 0, 0);
	private static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);


	public Quaternion() {
		super();
	}

	public Quaternion(Quaternion rot) {
		super(rot);
	}

	public Quaternion(Vector3f start, Vector3f end) {
		final Vector3f startNorm = new Vector3f(start);
		startNorm.normalize();
		final Vector3f endNorm = new Vector3f(end);
		endNorm.normalize();

		final float dot = startNorm.dot(endNorm);

		if (dot >= +(1f - EPSILON)) {
			// vectors are equals
			this.setIdentity();
		}
		else if (dot <= -(1f - EPSILON)) {
			// opposite direction, create a random axis
			Vector3f axis = new Vector3f();
			axis.cross(startNorm, X_AXIS);
			if (axis.length() <= EPSILON) {// pick another if co-linear
				axis.cross(startNorm, Y_AXIS);
			}
			axis.normalize();
			fromAxis(new float[] {axis.x, axis.y, axis.z}, (float)Math.PI);
		}
		else {

			final float s = (float)Math.sqrt((1f + dot) * 2f);
			final float invs = 1 / s;
			final Vector3f c = new Vector3f();
			c.cross(startNorm, endNorm);

			setX(c.x * invs);
			setY(c.y * invs);
			setZ(c.z * invs);
			setW(s * 0.5f);

			normalize();
		}

	}

	public void set(float x, float y, float z, float w) {
		setX(x);
		setY(y);
		setZ(z);
		setW(w);
	}

	public void mult(Vector3f vec) {
		float[] result = super.mult(new float[] {vec.x, vec.y, vec.z});
		vec.x = result[0];
		vec.y = result[1];
		vec.z = result[2];
	}

}
