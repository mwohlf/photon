package net.wohlfart.photon.node;

import net.wohlfart.photon.ShaderIdent;
import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.shader.FloatValue;
import net.wohlfart.photon.shader.ShaderParser;

// see: http://www.gamasutra.com/view/feature/131393/a_realtime_procedural_universe_.php?print=1
public class Corona extends AbstractRenderElement {

	private float planetSize = Float.NaN;
	private float thinkness = Float.NaN;

	public Corona() {
		renderConfig = IRenderConfig.BLENDING_ON;
		shaderIdent = ShaderIdent.SIMPLE_ATMOSPHERE_SHADER;
	}

	@Override // lazy create the geometry since we don't know the size in the constructor
	public IGeometry getGeometry() {
		if (geometry == null) {
			// geometry = new Sphere(planetSize + thinkness, 6, VertexFormat.VERTEX_P3C0N3T2, StreamFormat.TRIANGLES);
			geometry = new Sphere(planetSize + thinkness, 6, VertexFormat.VERTEX_P3C0N3T0, StreamFormat.TRIANGLES);

		}
		return geometry;
	}

	public Corona setPlanetSize(float size) {
		planetSize = size;
		setupGeometryAndUniforms();
		return this;
	}

	public Corona withThinkness(float thinkness) {
		this.thinkness = thinkness;
		setupGeometryAndUniforms();
		return this;
	}

	private void setupGeometryAndUniforms() {
		if (!Float.isNaN(planetSize) && !Float.isNaN(thinkness)) {
			geometry = new Sphere(planetSize + thinkness, 6, VertexFormat.VERTEX_P3C0N3T0, StreamFormat.TRIANGLES);
			uniforms.add(new FloatValue(ShaderParser.UNIFORM_ATMOSPHERE_RADIUS, planetSize + thinkness));
			uniforms.add(new FloatValue(ShaderParser.UNIFORM_PLANET_RADIUS, planetSize));
		}

	}

	@Override
	public String toString() {
		return this.getClass().getName() + " [zOrder=" + zOrder
				+ " renderConfig=" + renderConfig
				+ " planetSize=" + planetSize
				+ " thinkness=" + thinkness
				+ "]";
	}

}
