package net.wohlfart.photon.shader;

import javax.vecmath.Matrix4f;

public class Model2WorldMatrixValue extends Matrix4fValue {

	public Model2WorldMatrixValue(Matrix4f matrix) {
		super(ShaderParser.UNIFORM_MODEL_2_WORLD_MTX, matrix);
	}

}
