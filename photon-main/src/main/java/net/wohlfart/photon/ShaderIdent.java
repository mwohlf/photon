package net.wohlfart.photon;

import java.net.URI;
import java.net.URISyntaxException;

import net.wohlfart.photon.shader.IShaderProgram.IShaderProgramIdentifier;
import net.wohlfart.photon.texture.TextureFactory;

public enum ShaderIdent implements IShaderProgramIdentifier {
	DEFAULT_SHADER("shader/default.vert", "shader/default.frag"),
	SKYBOX_SHADER("shader/skybox.vert", "shader/skybox.frag"),
	TEXTURE_SHADER("shader/texture.vert", "shader/texture.frag"),
	VERTEX_LIGHT_SHADER("shader/vertexLight.vert", "shader/vertexLight.frag"),
	POINT_SPRITE_SHADER("shader/pointSprite.vert", "shader/pointSprite.frag"),
	ATMOSPHERE_SHADER("shader/atmosphere.vert", "shader/atmosphere.frag"),
	SEMI_TRANSPARENT_SHADER("shader/semiTransparent.vert", "shader/semiTransparent.frag"),
	; // TODO: add null shader

	private final URI vertexShaderResource;
	private final URI fragmentShaderResource;

	ShaderIdent(String vertexShaderResource, String fragmentShaderResource) {
		try {
			this.vertexShaderResource = new URI(TextureFactory.FILE_SCHEME + ":///" + vertexShaderResource);
			this.fragmentShaderResource = new URI(TextureFactory.FILE_SCHEME + ":///" + fragmentShaderResource);
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

	@Override
	public String toString() {
		return "ShaderIdent [vertexShaderResource=" + vertexShaderResource
				+ ", fragmentShaderResource=" + fragmentShaderResource + "]";
	}

}
