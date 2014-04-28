package net.wohlfart.photon.node;

import net.wohlfart.photon.ShaderIdent;
import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.TextureIdentValue;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;
import net.wohlfart.photon.texture.TextureIdentifier;


public class SphereElement extends AbstractRenderElement {

	public static final ITextureIdentifier TEXTURE_ID1 = TextureIdentifier.create("gfx/textures/texture.jpg");

	private SphereElement(){}

	public static SphereElement createGrid() {
		SphereElement elem = new SphereElement();
		elem.geometry = new Sphere(4, 2);
		elem.renderConfig = IRenderConfig.DEFAULT_3D;
		elem.shaderId = ShaderIdent.DEFAULT_SHADER_ID;
		return elem;
	}

	public static SphereElement createSolid() {
		SphereElement elem = new SphereElement();
		elem.geometry = new Sphere(4, 6, IGeometry.VertexFormat.VERTEX_P3C0N0T2, IGeometry.StreamFormat.TRIANGLES);
		elem.shaderId = ShaderIdent.TEXTURE_SHADER_ID;
		elem.uniforms.put(ShaderParser.TEXTURE01, new TextureIdentValue(TEXTURE_ID1));
		elem.renderConfig = IRenderConfig.DEFAULT_3D;
		return elem;
	}

}
