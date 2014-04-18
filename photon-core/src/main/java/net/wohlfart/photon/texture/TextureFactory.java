package net.wohlfart.photon.texture;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import net.wohlfart.photon.resources.ResourceProducer;
import net.wohlfart.photon.resources.ResourceTool;
import net.wohlfart.photon.resources.ResourceUriParser;

public class TextureFactory implements ResourceProducer<ITexture, TextureIdentifier> {

	@Override
	public Class<ITexture> flavour() {
		return ITexture.class;
	}

    @Override
    public ITexture produce(TextureIdentifier ident) {
        final URI uri = ident.getTextureResource();
        try {
            if (ResourceTool.FILE_SCHEME.equals(uri.getScheme())) {
            	return createFilebasedTexture(uri);
            } else if (ResourceTool.PROC_SCHEME.equals(uri.getScheme())) {
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
        final CelestialType type = CelestialType.valueOf(parser.getString("type"));
        final long seed = parser.getLong("seed");
        return new CelestialTexture(radius, type, seed);
    }

}
