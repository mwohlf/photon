package net.wohlfart.photon.node;

import net.wohlfart.photon.geometry.Quad;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.shader.ShaderIdentifier;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.TextureIdentValue;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;

public class QuadElement extends AbstractRenderElement {

	public static final ITextureIdentifier TEXTURE_ID1 = TextureIdentifier.create("gfx/textures/texture.jpg");
	public static final ShaderIdentifier TEXTURE_SHADER_ID = ShaderIdentifier.create("shader/texture.vert", "shader/texture.frag");

	private QuadElement(){}

	public static QuadElement createSolid() {
		QuadElement elem = new QuadElement();
		elem.geometry = new Quad(2f);
		elem.shaderId = TEXTURE_SHADER_ID;
		elem.uniforms.put(ShaderParser.TEXTURE01, new TextureIdentValue(TEXTURE_ID1));
		elem.renderConfig = IRenderConfig.DEFAULT_3D;
		return elem;
	}

}
