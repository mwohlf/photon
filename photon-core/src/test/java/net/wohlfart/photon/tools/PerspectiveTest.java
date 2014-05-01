package net.wohlfart.photon.tools;

import static org.junit.Assert.assertEquals;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;

import org.junit.Test;

public class PerspectiveTest {

	Matrix4f matrix;
	float Pi_eighth = (float) Math.PI/8f;

	@Test
	public void smoke45Test() {
		Perspective perspective = new Perspective();
		perspective.setFarPlane(1000f);
		perspective.setNearPlane(1f);
		perspective.setScreenHeight(200f);
		perspective.setScreenWidth(300f);
		perspective.setFieldOfViewDegree(45f);
		perspective.setScaleFactor(2.414f);
		float aspect = perspective.getAspectRatio();
		float screenScale = perspective.getScreenScale();

		assertEquals(200f/300f, aspect, 0.005f);
		assertEquals(2.414f, screenScale, 0.005f);

		matrix = perspective.getPerspectiveMatrix();
		check(new Vector4f(0.0f, 0.0f, -1f, 1.0f), new Vector4f(
				0.0f * screenScale * aspect,
				0.0f * screenScale * 1,
				0f,
				2.0f
				));
		check(new Vector4f(1.0f, 1.0f, -1f, 1.0f), new Vector4f(
				1f * screenScale * aspect,
				1f * screenScale * 1,
				0f,
				2.0f
				));
	}

	@Test
	public void smoke90Test() {
		Perspective perspective = new Perspective();
		perspective.setFarPlane(1000f);
		perspective.setNearPlane(1f);
		perspective.setScreenHeight(200f);
		perspective.setScreenWidth(300f);
		perspective.setFieldOfViewDegree(90f);
		perspective.setScaleFactor(2.414f);
		float aspect = perspective.getAspectRatio();
		float screenScale = perspective.getScreenScale();

		assertEquals(200f/300f, aspect, 0.005f);

		matrix = perspective.getPerspectiveMatrix();
		check(new Vector4f(0.0f, 0.0f, -1f, 1.0f), new Vector4f(
				0.0f * screenScale * aspect,
				0.0f * screenScale * 1,
				0f,
				2.0f
				));
		check(new Vector4f(1.0f, 1.0f, -1f, 1.0f), new Vector4f(
				1f * screenScale * aspect,
				1f * screenScale * 1,
				0f,
				2.0f
				));
	}


	@Test
	public void smoke50Test() {
		Perspective perspective = new Perspective();
		perspective.setFarPlane(1000f);
		perspective.setNearPlane(1f);
		perspective.setScreenHeight(200f);
		perspective.setScreenWidth(300f);
		perspective.setFieldOfViewDegree(50f);
		perspective.setScaleFactor(2.414f);
		float aspect = perspective.getAspectRatio();
		float screenScale = perspective.getScreenScale();

		assertEquals(200f/300f, aspect, 0.005f);
		assertEquals(2.144f, screenScale, 0.005f);

		matrix = perspective.getPerspectiveMatrix();
		check(new Vector4f(0.0f, 0.0f, -1f, 1.0f), new Vector4f(
				0.0f * screenScale * aspect,
				0.0f * screenScale * 1,
				0f,
				2.0f
				));
		check(new Vector4f(1.0f, 1.0f, -1f, 1.0f), new Vector4f(
				1f * screenScale * aspect,
				1f * screenScale * 1,
				0f,
				2.0f
				));
		check(new Vector4f(-1.0f, 1.0f, -1f, 1.0f), new Vector4f(
				-1f * screenScale * aspect,
				1f * screenScale * 1,
				0f,
				2.0f
				));
		check(new Vector4f(-0.2f, 0.1f, -1f, 1.0f), new Vector4f(
				-0.2f * screenScale * aspect,
				0.1f * screenScale * 1,
				0f,
				2.0f
				));
	}


	private void check(Vector4f in, Vector4f expected) {
		Vector4f actual = new Vector4f();
		matrix.transform(in, actual);
		assertEquals(expected.z, actual.z, 0.005f);
		assertEquals(expected.x, actual.x, 0.005f);
		assertEquals(expected.y, actual.y, 0.005f);
		assertEquals(expected.w, actual.w, 0.005f); // no idea why this is 2
	}
}
