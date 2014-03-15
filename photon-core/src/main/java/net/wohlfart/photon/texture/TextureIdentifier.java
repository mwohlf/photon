package net.wohlfart.photon.texture;

import java.net.URI;
import java.net.URISyntaxException;

import net.wohlfart.photon.resources.ResourceTool;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;

public class TextureIdentifier implements ITextureIdentifier {    
    private final URI textureResource;

    public static TextureIdentifier create(float radius, CelestialType type, long seed) {
        try {
            URI uri = new URI(ResourceTool.PROC_SCHEME + ":///celestial?"
                    + "radius=" + radius + "&"
                    + "type=" + type.toString() + "&"
                    + "seed=" + seed);
            return new TextureIdentifier(uri);
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("invalid uri", ex);
        }
    }

    public static TextureIdentifier create(String path, float size) {
        try {
            URI uri = new URI(ResourceTool.FILE_SCHEME + ":///" + path + "?size=" + size);
            return new TextureIdentifier(uri);
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("invalid uri", ex);
        }
    }

    public static TextureIdentifier create(String path) {
        try {
            URI uri = new URI(ResourceTool.FILE_SCHEME + ":///" + path);
            return new TextureIdentifier(uri);
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("invalid uri", ex);
        }    
    }
  
    private TextureIdentifier(URI textureResource) {
        this.textureResource = textureResource;
    }

    @Override
    public URI getTextureResource() {
        return textureResource;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + textureResource.hashCode();
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
        TextureIdentifier other = (TextureIdentifier) obj;
        if (!textureResource.equals(other.textureResource)) {
            return false;
        }
        return true;
    }

}
