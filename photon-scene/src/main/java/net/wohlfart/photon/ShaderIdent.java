package net.wohlfart.photon;

import java.net.URI;
import java.net.URISyntaxException;

import net.wohlfart.photon.resources.ResourceTool;
import net.wohlfart.photon.shader.IShaderProgram.IShaderProgramIdentifier;

public enum ShaderIdent implements IShaderProgramIdentifier {
	DEFAULT_SHADER_ID("shader/default.vert", "shader/default.frag"),
	SIMPLE_SHADER_ID("shader/simple/vertex.glsl", "shader/simple/fragment.glsl"),
	SKYBOX_SHADER_ID("shader/skybox.vert", "shader/skybox.frag"),
	TEXTURE_SHADER_ID("shader/texture.vert", "shader/texture.frag"),
	;

	private final URI vertexShaderResource;
	private final URI fragmentShaderResource;

	ShaderIdent(String vertexShaderResource, String fragmentShaderResource) {
		try {
			this.vertexShaderResource = new URI(ResourceTool.FILE_SCHEME + ":///" + vertexShaderResource);
			this.fragmentShaderResource = new URI(ResourceTool.FILE_SCHEME + ":///" + fragmentShaderResource);
		} catch (URISyntaxException ex) {
			throw new IllegalStateException("can't resolve URL", ex);
		}
	}

	@Override
	public URI getVertexShaderResource() {
		return vertexShaderResource;
	}

	@Override
	public URI getFragmentShaderResource() {
		return fragmentShaderResource;
	}

}
