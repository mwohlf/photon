package net.wohlfart.photon.shader;

import java.net.URI;
import java.util.Map;

import javax.media.opengl.GL2;

import net.wohlfart.photon.render.IGeometry.VertexFormat;
import net.wohlfart.photon.shader.UniformHandle.UniformValue;
import net.wohlfart.photon.texture.ITexture;

public interface IShaderProgram {


	public interface IShaderProgramIdentifier {

		URI getVertexShaderResource();

		URI getFragmentShaderResource();

	}

	// set the gl context from the current run
	void bind(GL2 gl);

	void useTextures(Map<String, ITexture> textures);

	void useUniforms(Map<String, UniformValue> uniformValues);

	void useAttributes(VertexFormat vertexFormat);

	void unbind();

	void dispose();



	public class NullShader implements IShaderProgram {

		@Override
		public void bind(GL2 gl2) {
			// do nothing
		}

		@Override
		public void unbind() {
			// do nothing
		}

		@Override
		public void dispose() {
			// do nothing
		}

		@Override
		public void useTextures(Map<String, ITexture> textures) {
			// do nothing
		}

		@Override
		public void useUniforms(Map<String, UniformValue> uniformValues) {
			// do nothing
		}

		@Override
		public void useAttributes(VertexFormat vertexFormat) {
			// do nothing
		}

	}

}
