package net.wohlfart.photon.texture;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;

import net.wohlfart.photon.resources.ResourceProducer;
import net.wohlfart.photon.resources.ResourceTool;
import net.wohlfart.photon.resources.ResourceUriParser;
import net.wohlfart.photon.texture.ITexture.ITextureIdentifier;

public class TextureFactory implements ResourceProducer<ITexture, ITextureIdentifier> {

    public static final String FILE_SCHEME = "file";
    public static final String SIMPLEX_SCHEME = "smplx";

	private static final HashMap<String, ISphereSurfaceColor> SPHERE_SURFACE_MAP = new HashMap<String, ISphereSurfaceColor>();

	@Override
	public Class<ITexture> flavour() {
		return ITexture.class;
	}

    @Override
    public ITexture produce(ITextureIdentifier ident) {
        final URI uri = ident.getTextureResource();
        try {
            if (FILE_SCHEME.equals(uri.getScheme())) {
            	return createFilebasedTexture(uri);
            } else if (SIMPLEX_SCHEME.equals(uri.getScheme())) {
                return createProcedualTexture(uri);
            } else {
                throw new IllegalStateException("unknown scheme: '" + uri.getScheme()
                        + "' identifier is: '" + uri + "'");
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Can't load texture from '" + uri + "'", ex);
        }
    }

    private ITexture createFilebasedTexture(URI uri) throws IOException {
        return new ImageTexture(ResourceTool.readImage(uri));
    }

    private ITexture createProcedualTexture(URI uri) throws UnsupportedEncodingException {
        final ResourceUriParser parser = new ResourceUriParser(uri);
        final float radius = parser.getFloat("radius");
        final ISphereSurfaceColor type = getSphereSurfaceColor(parser.getString("type"));
        final long seed = parser.getLong("seed");
        return new CelestialTexture(radius, type, seed);
    }

    private ISphereSurfaceColor getSphereSurfaceColor(String key) {
    	ISphereSurfaceColor result = SPHERE_SURFACE_MAP.get(key);
    	if (result == null) {
    		throw new IllegalArgumentException("key not found in SurfaceMap: '" + key + "'");
    	}
    	return result;
    }

    public ISphereSurfaceColor registerSphereSurfaceColor(ISphereSurfaceColor element) {
    	return SPHERE_SURFACE_MAP.put(element.getId(), element);
    }

}
