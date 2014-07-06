package net.wohlfart.photon.node;


import javax.vecmath.Vector4f;

import net.wohlfart.photon.ShaderIdent;
import net.wohlfart.photon.entity.stellar.AbstractCelestial;
import net.wohlfart.photon.geometry.Sphere;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.IGeometry;
import net.wohlfart.photon.render.IGeometry.StreamFormat;
import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.render.IRenderConfig;
import net.wohlfart.photon.shader.FloatValue;
import net.wohlfart.photon.shader.ShaderParser;
import net.wohlfart.photon.shader.Vector4fValue;
import net.wohlfart.photon.tools.Color;

// see: http://www.gamasutra.com/view/feature/131393/a_realtime_procedural_universe_.php?print=1
public class Corona extends AbstractRenderElement {

	private AbstractCelestial celestial;
	private float thinkness = Float.NaN;
	private Color color = Color.WHITE;

	public Corona() {
		renderConfig = IRenderConfig.ATMOSPHERE_FRONT;
		shaderIdent = ShaderIdent.SIMPLE_CORONA_SHADER;
	}

	@Override // lazy create the geometry since we don't know the size in the constructor
	public IGeometry getGeometry() {
		setupGeometryAndUniforms();
		return geometry;
	}

	public Corona withCelestial(AbstractCelestial celestial) {
		this.celestial = celestial;
		setupGeometryAndUniforms();
		return this;
	}

	public Corona withThinkness(float thinkness) {
		this.thinkness = thinkness;
		setupGeometryAndUniforms();
		return this;
	}

	public Corona withColor(Color color) {
		this.color = color;
		setupGeometryAndUniforms();
		return this;
	}

	private void setupGeometryAndUniforms() {
		if (celestial != null
				&& color != null
				&& !Float.isNaN(thinkness)) {
			float planetSize = celestial.getSize();
			geometry = new Sphere(planetSize + thinkness, 6, VertexFormat.VERTEX_P3C0N3T0, StreamFormat.TRIANGLES);
			uniforms.add(new FloatValue(ShaderParser.UNIFORM_CORONA_RADIUS, planetSize + thinkness));
			uniforms.add(new FloatValue(ShaderParser.UNIFORM_PLANET_RADIUS, planetSize));
			uniforms.add(new Vector4fValue(ShaderParser.UNIFORM_CORONA_COLOR,
					new Vector4f(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, color.getAlpha()/255f)));
		}
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " [zOrder=" + zOrder
				+ " renderConfig=" + renderConfig
				+ " celestial=" + celestial
				+ " thinkness=" + thinkness
				+ "]";
	}

	public Corona withColor() {
		// TODO Auto-generated method stub
		return null;
	}

}
