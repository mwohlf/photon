package net.wohlfart.photon.pov;

import javax.vecmath.Vector3f;

import net.wohlfart.photon.tools.MathTool;
import net.wohlfart.photon.tools.Quaternion;


public class CanRotateImpl extends Quaternion implements CanRotate {
	private static final long serialVersionUID = 1L;

	@Override
	public Quaternion getRotation() {
		return this;
	}

	@Override
	public void setRotation(Quaternion q) {
		setW(q.getW());
		setX(q.getX());
		setY(q.getY());
		setZ(q.getZ());
	}

	@Override
	public void rotate(float deltaAngle, Vector3f axis) {
		MathTool.rotate(this, deltaAngle, axis);
	}

	/** the (1,0,0) vector / X axis */
	@Override
	public Vector3f getRght(final Vector3f result) {
		result.x = 1f - 2f * (y * y + z * z);
		result.y = 2f * (x * y - w * z);
		result.z = 2f * (x * z + w * y);
		result.normalize();
		return result;
	}

	/** the (0,1,0) vector / Y axis */
	@Override
	public Vector3f getUp(final Vector3f result) {
		result.x = 2f * (x * y + w * z);
		result.y = 1f - 2f * (z * z + x * x);
		result.z = 2f * (y * z - w * x);
		result.normalize();
		return result;
	}

	/** the (0,0,1) vector / Z axis */
	@Override
	public Vector3f getForward(final Vector3f result) {
		result.x = 2f * (x * z - w * y);
		result.y = 2f * (y * z + w * x);
		result.z = 1f - 2f * (x * x + y * y);
		result.normalize();
		return result;
	}

	@Override
	public void reset() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 1;
	}

}
