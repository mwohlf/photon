package net.wohlfart.photon.hud.txt;

import gnu.trove.map.TCharObjectMap;
import gnu.trove.map.hash.TCharObjectHashMap;

import java.awt.image.BufferedImage;

import javax.annotation.Nullable;

class CharAtlas implements ICharAtlas {

    private final TCharObjectMap<CharInfo> map = new TCharObjectHashMap<>();
    private final BufferedImage buffImage;

    // package private, use the builder
    CharAtlas(BufferedImage buffImage) {
        this.buffImage = buffImage;
    }
       
    /**
     *  store a single character with its coordinates inside the texture
     *  parameters are in pixel space
     * @param charGap 
     */
    void put(char c, float x, float y, float w, float h, float g) {
        map.put(c, new CharInfo(x, y, w, h, g));
    }

    @Override
    public BufferedImage getImage() {
        return buffImage;
    }

    @Override @Nullable
    public CharInfo getCharInfo(char c) {
        return map.get(c);
    }

}
