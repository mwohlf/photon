package net.wohlfart.photon.shader;

import java.net.URI;
import java.net.URISyntaxException;

import net.wohlfart.photon.resources.ResourceTool;
import net.wohlfart.photon.shader.IShaderProgram.IShaderProgramIdentifier;

// immutable
public class ShaderIdentifier implements IShaderProgramIdentifier {
    private final URI vertexShaderResource;
    private final URI fragmentShaderResource;

    public static final ShaderIdentifier create(String vertexShaderResource, String fragmentShaderResource) {
        try {
            return new ShaderIdentifier(
                    new URI(ResourceTool.FILE_SCHEME + ":///" + vertexShaderResource),
                    new URI(ResourceTool.FILE_SCHEME + ":///" + fragmentShaderResource));
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("can't resolve URL", ex);
        }
    }

    protected ShaderIdentifier(URI vertexShaderResource, URI frgamentShaderResource) {
        assert vertexShaderResource != null;
        assert frgamentShaderResource != null;
        this.vertexShaderResource = vertexShaderResource;
        this.fragmentShaderResource = frgamentShaderResource;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + fragmentShaderResource.hashCode();
        result = prime * result + vertexShaderResource.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        ShaderIdentifier other = (ShaderIdentifier) obj;
        if (!fragmentShaderResource.equals(other.fragmentShaderResource)) {
            return false;
        }
        if (!vertexShaderResource.equals(other.vertexShaderResource)) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "ShaderIdentifier [vertexShaderResource=" + vertexShaderResource
				+ ", fragmentShaderResource=" + fragmentShaderResource + "]";
	}

}
