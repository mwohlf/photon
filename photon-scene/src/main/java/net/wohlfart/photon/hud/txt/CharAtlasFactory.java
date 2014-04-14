package net.wohlfart.photon.hud.txt;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import net.wohlfart.photon.GenericException;
import net.wohlfart.photon.resources.ResourceProducer;
import net.wohlfart.photon.resources.ResourceTool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CharAtlasFactory implements ResourceProducer<ICharAtlas, FontIdentifier> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CharAtlasFactory.class);

    public static final FontIdentifier DEFAULT_FONT_ID = FontIdentifier.create("fonts/liberation/LiberationMono-Regular.ttf", 20f);

    public static final char NULL_CHAR = '_';
    private static final String CHARS = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789;:,._-#+?!\"()";


    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;
    // switch on for debugging:
    private final boolean borderOn = false;

    public CharAtlasFactory() {
        assert CHARS.contains(String.valueOf(NULL_CHAR)) : "need NULL_CHAR in char sequence";
    }

	@Override
	public Class<ICharAtlas> flavour() {
		return ICharAtlas.class;
	}

    @Override
    public ICharAtlas produce(FontIdentifier identifier) {
        LOGGER.debug("creating CharATlas for identifier '{}'", identifier);
        try (final InputStream inputStream = ResourceTool.openStream(identifier.getFontResource())) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            font = font.deriveFont(identifier.getPoints()); // size
            return createCharacterAtlas(font);
        } catch (Exception ex) {
            throw new GenericException("can't create font from file '" + identifier.getFontResource() + "'", ex);
        }
    }

    private CharAtlas createCharacterAtlas(Font font) {
        final BufferedImage buffImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        final CharAtlas atlas = new CharAtlas(buffImage);
        final Graphics2D g = (Graphics2D) buffImage.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setColor(new Color(0f, 0f, 0f, 0f)); // transparent
        g.fillRect(0, 0, WIDTH, HEIGHT);
        final FontMetrics fontMetrics = g.getFontMetrics();
        final LineMetrics lineMetrics = fontMetrics.getLineMetrics(CHARS, g);

        // for whatever reason some chars cross over to the previous one in some fonts (e.g. j) so we add a
        // gap to make sure each char is alone in its own rectangle
        final float charGap = 0;
        final float height = lineMetrics.getHeight();
        final float ascent = lineMetrics.getAscent();
        float x = 0;
        float y = 0;
        for (final char c : CHARS.toCharArray()) {
            final float width = fontMetrics.charWidth(c) + charGap;
            // final float width = (float) fontMetrics.getStringBounds(String.valueOf(c), g).getWidth();
            if (x + width > WIDTH) { // new line
                x = 0;
                y += height;
                if (y + height > HEIGHT) {
                    throw new IllegalStateException("chars don't fit into the atlas,"
                            + " we are out of space for char '" + c + "'"
                            + " dimensions of the canvas is " + WIDTH + "x" + HEIGHT);
                }
            }
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(c), x + charGap, y + ascent);  // draw the next char more right
            atlas.put(c, x, y, width, height, charGap);                // but still take the whole space (width contains the gap)
            if (borderOn) {
                g.setColor(Color.RED);
                g.drawRect(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
            }
            x += width;
        }
        return atlas;
    }

}
