package net.wohlfart.photon.shader;

import java.util.Arrays;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Model2WorldMatrixValue extends Matrix4fValue {
	protected static final Logger LOGGER = LoggerFactory.getLogger(Model2WorldMatrixValue.class);


	public class NormalMatrix implements IUniformValue {

		protected final String key;
		protected final Matrix3f m3 = new Matrix3f();


		public NormalMatrix() {
			key = ShaderParser.UNIFORM_NORMAL_MTX;
		}

		@Override
		public String getKey() {
			return key;
		}

		@Override
		public void accept(IShaderProgram shader) {

			Integer location = shader.getUniformLocation(key);
			if (location == null) {
				LOGGER.debug("no normal position found, skipping uniform value");
				return;
			}

			calculateNormalMatrix(matrix, m3);

			float[] normalArray = {
					m3.m00, m3.m01, m3.m02,
					m3.m10, m3.m11, m3.m12,
					m3.m20, m3.m21, m3.m22,
			};
			LOGGER.debug("setting matrix uniform '{}' location is '{}', values are '{}'",
					ShaderParser.UNIFORM_NORMAL_MTX, location, Arrays.toString(normalArray));
			shader.getGl().glUniformMatrix3fv(location, 1, false, normalArray, 0);
		}


		public Matrix3f calculateNormalMatrix(Matrix4f m4, Matrix3f m3) {
			m3.m00 = m4.m00;
			m3.m01 = m4.m01;
			m3.m02 = m4.m02;
			m3.m10 = m4.m10;
			m3.m11 = m4.m11;
			m3.m12 = m4.m12;
			m3.m20 = m4.m20;
			m3.m21 = m4.m21;
			m3.m22 = m4.m22;

			m3.invert();
			m3.transpose();
			return m3;
		}


	}

	public Model2WorldMatrixValue(Matrix4f matrix) {
		super(ShaderParser.UNIFORM_MODEL_2_WORLD_MTX, matrix); // unique name/key for any shader
	}

	@Override
	public void accept(IShaderProgram shader) {
		assert (matrix != null) : "Uniform '" + shader.getUniformLocation(name) + "' is empty";

		float[] floatArray = {
				matrix.m00, matrix.m01, matrix.m02, matrix.m03,
				matrix.m10, matrix.m11, matrix.m12, matrix.m13,
				matrix.m20, matrix.m21, matrix.m22, matrix.m23,
				matrix.m30, matrix.m31, matrix.m32, matrix.m33,
		};
		Integer location = shader.getUniformLocation(name);
		LOGGER.debug("setting matrix uniform '{}' location is '{}', values are '{}'", name, location, Arrays.toString(floatArray));
		shader.getGl().glUniformMatrix4fv(location, 1, false, floatArray, 0);
	}

}
