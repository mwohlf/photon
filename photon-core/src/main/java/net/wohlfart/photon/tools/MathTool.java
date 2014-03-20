package net.wohlfart.photon.tools;

import java.util.Random;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.jogamp.opengl.math.Quaternion;

public class MathTool {

    private static final Random RANDOM = new Random();

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
    }


	public static void convert(Quaternion rot, Matrix4f result) {
		result.set(rot.toMatrix());
	}

    public static Matrix4f convert(Vector3f move, Matrix4f mat) {

        // column-row syntax
        mat.m00 = 1;
        mat.m10 = 0;
        mat.m20 = 0;
        mat.m30 = move.x;

        mat.m01 = 0;
        mat.m11 = 1;
        mat.m21 = 0;
        mat.m31 = move.y;

        mat.m02 = 0;
        mat.m12 = 0;
        mat.m22 = 1;
        mat.m32 = move.z;

        mat.m03 = 0;
        mat.m13 = 0;
        mat.m23 = 0;
        mat.m33 = 1;

        return mat;
    }

	public static float random(float min, float max) {
        return RANDOM.nextFloat() * (max - min) + min;
	}

}
