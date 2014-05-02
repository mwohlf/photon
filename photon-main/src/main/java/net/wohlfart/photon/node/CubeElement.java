package net.wohlfart.photon.node;

import net.wohlfart.photon.ShaderIdent;
import net.wohlfart.photon.geometry.Cube;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;

public class CubeElement extends AbstractRenderElement {

	public static final ITextureIdentifier TEXTURE_ID1 = TextureIdentifier.create("gfx/textures/texture.jpg");

	private CubeElement(){
	}

	public static CubeElement createGrid(float side) {
		CubeElement elem = new CubeElement();
		elem.geometry = new Cube(side);
		elem.renderConfig = IRenderConfig.DEFAULT_3D;
		elem.shaderId = ShaderIdent.DEFAULT_SHADER_ID;
		return elem;
	}

}
