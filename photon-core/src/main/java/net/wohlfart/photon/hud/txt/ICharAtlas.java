package net.wohlfart.photon.hud.txt;

import java.awt.image.BufferedImage;
import java.net.URI;

public interface ICharAtlas {
    
    public interface IFontIdentifier {
        
        URI getFontResource();

        float getPoints();
                
    }

    BufferedImage getImage();

    CharInfo getCharInfo(char c);

}
