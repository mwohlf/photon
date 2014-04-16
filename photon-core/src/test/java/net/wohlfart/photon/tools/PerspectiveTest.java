package net.wohlfart.photon.tools;

import static org.junit.Assert.assertEquals;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;

import org.junit.Test;

public class PerspectiveTest {

	@Test
	public void smokeTest() {
		Perspective perspective = new Perspective();
		perspective.setFarPlane(1000);
		perspective.setNearPlane(1);
		perspective.setScreenHeight(200);
		perspective.setScreenWidth(300);

		Matrix4f matrix = perspective.getMatrix();
		Vector4f vect = new Vector4f(0.0f, 0.0f, -1.21f, 1.0f);
		matrix.transform(vect);
		assertEquals(200f, vect.z, 0.001f);
		assertEquals(200f, vect.x, 0.001f);
		assertEquals(200f, vect.y, 0.001f);
		assertEquals(200f, vect.w, 0.001f);
	}
}
