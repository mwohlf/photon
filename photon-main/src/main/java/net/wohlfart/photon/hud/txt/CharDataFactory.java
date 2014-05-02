package net.wohlfart.photon.hud.txt;

import net.wohlfart.photon.resources.ResourceProducer;
import net.wohlfart.photon.texture.ITexture;
import net.wohlfart.photon.texture.ImageTexture;

public class CharDataFactory implements ResourceProducer<ICharData, FontIdentifier> {

    private final CharAtlasFactory charAtlasFactory;

    public CharDataFactory(CharAtlasFactory charAtlasFactory) {
        this.charAtlasFactory = charAtlasFactory;
    }

	@Override
	public Class<ICharData> flavour() {
		return ICharData.class;
	}

    @Override
    public ICharData produce(FontIdentifier key) {
        final ICharAtlas charAtlas = charAtlasFactory.produce(key);
        final ITexture charTexture =  new ImageTexture(charAtlas.getImage());
        return new CharData(charAtlas, charTexture);
    }

}
