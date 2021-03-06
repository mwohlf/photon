package net.wohlfart.photon.shader;

import java.net.URI;
import java.util.Collection;

import javax.media.opengl.GL2ES2;

import net.wohlfart.photon.render.IGeometry.VertexFormat;

// TODO: reduce methods
public interface IShaderProgram {

	public interface IShaderProgramIdentifier {

		URI getVertexShaderResource();

		URI getFragmentShaderResource();

	}

	int getId();

	// set the gl context from the current run
	void bind(GL2ES2 gl);

	GL2ES2 getGl();

	void useUniforms(Collection<IUniformValue> uniformValues);

	Integer getUniformLocation(String name);

	void useAttributes(VertexFormat vertexFormat);

	void unbind();

	void dispose();

	void reset();

	int nextTextureSlot();

	int nextLightSlot();



	IShaderProgramIdentifier NULL_SHADER_ID = new IShaderProgramIdentifier() {

		@Override
		public URI getVertexShaderResource() {
			return null;
		}

		@Override
		public URI getFragmentShaderResource() {
			return null;
		}

	};


	public static final IShaderProgram NULL_SHADER = new IShaderProgram() {

		@Override
		public int getId() {
			return -1;
		}

		@Override
		public void bind(GL2ES2 gl) {
			// do nothing
		}

		@Override
		public GL2ES2 getGl() {
			return null;
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
		public void useUniforms(Collection<IUniformValue> uniformValues) {
			// do nothing
		}

		@Override
		public Integer getUniformLocation(String name) {
			return null;
		}

		@Override
		public void useAttributes(VertexFormat vertexFormat) {
			// do nothing
		}

		@Override
		public int nextTextureSlot() {
			return -1;
		}

		@Override
		public int nextLightSlot() {
			return -1;
		}

		@Override
		public void reset() {
			// do nothing
		}

		@Override
		public String toString() {
			return "NULL_SHADER";
		}

	};

}
