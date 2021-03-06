package net.wohlfart.photon.hud.txt;

import java.net.URI;
import java.net.URISyntaxException;

import net.wohlfart.photon.hud.txt.ICharAtlas.IFontIdentifier;
import net.wohlfart.photon.texture.TextureFactory;

public class FontIdentifier implements IFontIdentifier {
    private final URI fontResource;
    private final float points;

    public static FontIdentifier create(String path, float points) {
        try {
            URI uri = new URI(TextureFactory.FILE_SCHEME + ":///" + path + "?points=" + points);
            return new FontIdentifier(uri, points);
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("can't resolve URL", ex);
        }
    }

    private FontIdentifier(URI fontResource, float points) {
        this.fontResource = fontResource;
        this.points = points;
    }

    @Override
    public URI getFontResource() {
        return fontResource;
    }

    @Override
    public float getPoints() {
        return points;
    }

	@Override
	public String toString() {
		return "FontIdentifier [fontResource=" + fontResource
				+ ", points=" + points + "]";
	}

}
