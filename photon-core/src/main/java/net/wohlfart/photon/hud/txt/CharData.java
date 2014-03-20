package net.wohlfart.photon.hud.txt;

import net.wohlfart.photon.texture.ITexture;

public class CharData implements ICharData {

    private final ICharAtlas charAtlas;
    private final ITexture charTexture;

    public CharData(ICharAtlas charAtlas, ITexture charTexture) {
        this.charAtlas = charAtlas;
        this.charTexture = charTexture;
    }

    @Override
    public ICharAtlas getCharAtlas() {
        return charAtlas;
    }

    @Override
    public ITexture getCharTexture() {
        return charTexture;
    }

}
