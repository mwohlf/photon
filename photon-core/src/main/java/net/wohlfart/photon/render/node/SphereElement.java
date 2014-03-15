package net.wohlfart.photon.render.node;

import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.shader.ShaderParser;


public class SphereElement extends AbstractRenderElement {

	public static SphereElement createGrid() {
		SphereElement elem = new SphereElement();
		elem.geometry = new Sphere(4, 2);
		return elem;
	}

	public static SphereElement createSolid() {
		SphereElement elem = new SphereElement();
		elem.geometry = new Sphere(4, 6, IGeometry.VertexFormat.VERTEX_P3C0N0T2, IGeometry.StreamFormat.TRIANGLES);
		//elem.shaderId =
		elem.textures.put(ShaderParser.TEXTURE01, TEXTURE_ID1);
		return elem;
	}

	private SphereElement(){

	}

}
