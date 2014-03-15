package net.wohlfart.photon.tools;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.jogamp.opengl.math.Quaternion;

public class MathTool {

	public static final float HALF_PI = (float)Math.PI/2f;

	public static final float PI = (float)Math.PI;

	public static final float THREE_HALF_PI = 3f*(float)Math.PI/2f;

	public static final float TWO_PI = 2f*(float)Math.PI;

	public static void mul(Quaternion q, Vector3d vec) {
        double xx, yy, zz;
        // @formatter:off
        xx = q.getW() * q.getW() * vec.x + 2 * q.getY() * q.getW() * vec.z
                - 2 * q.getZ() * q.getW() * vec.y + q.getX() * q.getX() * vec.x
                + 2 * q.getY() * q.getX() * vec.y + 2 * q.getZ() * q.getX() * vec.z
                - q.getZ() * q.getZ() * vec.x - q.getY() * q.getY() * vec.x;

        yy = 2 * q.getX() * q.getY() * vec.x + q.getY() * q.getY() * vec.y
                + 2 * q.getZ() * q.getY() * vec.z + 2 * q.getW() * q.getZ() * vec.x
                - q.getZ() * q.getZ() * vec.y + q.getW() * q.getW() * vec.y
                - 2 * q.getX() * q.getW() * vec.z - q.getX() * q.getX() * vec.y;

        zz = 2 * q.getX() * q.getZ() * vec.x + 2 * q.getY() * q.getZ() * vec.y
                + q.getZ() * q.getZ() * vec.z - 2 * q.getW() * q.getY() * vec.x
                - q.getY() * q.getY() * vec.z + 2 * q.getW() * q.getX() * vec.y
                - q.getX() * q.getX() * vec.z + q.getW() * q.getW() * vec.z;
        // @formatter:on
        vec.x = xx;
        vec.y = yy;
        vec.z = zz;
	}

	public static void add(Vector3f left, Vector3f right, Vector3f result) {
		result.x = left.x + right.x;
		result.y = left.y + right.y;
		result.z = left.z + right.z;
	}

	// FIXME: remove this method
    public static void rotate(Quaternion q, float rad, Vector3f axis) {
    	q.fromAxis(new float[] {axis.x, axis.y, axis.z}, rad);
    	/*
        axis.normalize();
        final double n = Math.sqrt(axis.x * axis.x + axis.y * axis.y + axis.z * axis.z);
        final float sin = (float) (Math.sin(0.5 * rad) / n);
        Quaternion rot = new Quaternion();
        rot.setX(axis.x * sin);
        rot.setY(axis.y * sin);
        rot.setZ(axis.z * sin);
        rot.setW((float) Math.cos(0.5 * rad));

        mul(rot, q, rot);
        rot.normalize();
        q.setW(rot.getW());
        q.setX(rot.getX());
        q.setY(rot.getY());
        q.setZ(rot.getZ());
        */
    }


	public static void convert(Quaternion rot, Matrix4f result) {

		result.set(rot.toMatrix());

		/*
        final float xx = rot.getX() * rot.getX();
        final float xy = rot.getX() * rot.getY();
        final float xz = rot.getX() * rot.getZ();
        final float xw = rot.getX() * rot.getW();

        final float yy = rot.getY() * rot.getY();
        final float yz = rot.getY() * rot.getZ();
        final float yw = rot.getY() * rot.getW();

        final float zz = rot.getZ() * rot.getZ();
        final float zw = rot.getZ() * rot.getW();

        // column-row syntax
        result.m00 = 1 - 2 * (yy + zz);
        result.m10 = 2 * (xy - zw);
        result.m20 = 2 * (xz + yw);
        result.m30 = 0;

        result.m01 = 2 * (xy + zw);
        result.m11 = 1 - 2 * (xx + zz);
        result.m21 = 2 * (yz - xw);
        result.m31 = 0;

        result.m02 = 2 * (xz - yw);
        result.m12 = 2 * (yz + xw);
        result.m22 = 1 - 2 * (xx + yy);
        result.m32 = 0;

        result.m03 = 0;
        result.m13 = 0;
        result.m23 = 0;
        result.m33 = 1;
        */
	}

}
