package net.wohlfart.photon.node;

import net.wohlfart.photon.geometry.Cube;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;

public class CubeElement extends AbstractRenderElement {

	public static final ITextureIdentifier TEXTURE_ID1 = TextureIdentifier.create("gfx/textures/texture.jpg");
	public static final ShaderIdentifier TEXTURE_SHADER_ID = ShaderIdentifier.create("shader/texture.vert", "shader/texture.frag");

	private CubeElement(){}

	public static CubeElement createGrid(float side) {
		CubeElement elem = new CubeElement();
		elem.geometry = new Cube(side);
		return elem;
	}

}
