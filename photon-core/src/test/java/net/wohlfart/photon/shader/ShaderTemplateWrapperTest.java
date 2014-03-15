package net.wohlfart.photon.shader;

import static org.junit.Assert.*;

import org.junit.Test;

public class ShaderTemplateWrapperTest {

	@Test
	public void smokeTest() {	
		String rendered = new ShaderParser("shader template").render();
		assertEquals(rendered, "shader template");
	}

	@Test
	public void test() {	
		String rendered = new ShaderParser("shader template $position$ test").render();
		assertEquals(rendered, "shader template in_Position test");
	}

}
