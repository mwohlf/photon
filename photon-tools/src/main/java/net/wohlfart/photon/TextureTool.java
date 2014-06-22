package net.wohlfart.photon;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;

import net.wohlfart.photon.resources.TextureIdent;
import net.wohlfart.photon.texture.CelestialTexture;
import net.wohlfart.photon.texture.ISphereSurfaceColor;



public class TextureTool {

	protected ReadableCelestialTexture texture;
	protected BufferedImage image;

	protected final JFrame frame;

	protected final JSlider octaveSlider = new JSlider();
	protected final JSlider persistenceSlider = new JSlider();
	protected final JSlider wSlider = new JSlider();


	public TextureTool() {
		assert EventQueue.isDispatchThread() : "running in wrong thread";
		this.frame = new JFrame();
		this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	public TextureTool initialize() {
		assert EventQueue.isDispatchThread() : "running in wrong thread";
		this.texture = new ReadableCelestialTexture(3 * 10, TextureIdent.CONTINENTAL, 1);
		this.texture = new ReadableCelestialTexture(3 * 10, TextureIdent.GAS_PLANET, 1);
		this.texture = new ReadableCelestialTexture(3 * 10, TextureIdent.GREEN, 1);
		this.texture = new ReadableCelestialTexture(3 * 10, TextureIdent.RED, 1);
		this.texture = new ReadableCelestialTexture(3 * 10, TextureIdent.GREEN, 1);
		this.texture = new ReadableCelestialTexture(3 * 10, TextureIdent.SUN_CLASS_G, 1);
        this.image = new BufferedImage(texture.getWidth(), texture.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
		return this;
	}

	public void start() {
		assert EventQueue.isDispatchThread() : "running in wrong thread";
		addContent();
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

    private void addContent() {
        final Container content = frame.getContentPane();
        content.add(new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
            public Dimension getPreferredSize() {
                return new Dimension(texture.getWidth(), texture.getHeight());
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                texture.updateImage(image);
                g.drawImage(image, 0, 0, null);
            }
        }, BorderLayout.CENTER);
    }

    protected final class ReadableCelestialTexture extends CelestialTexture {

		public ReadableCelestialTexture(float radius, ISphereSurfaceColor type, long seed) {
			super(radius, type, seed);
		}

		public IntBuffer getIntBuffer() {
			return intBuffer;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

	    private void updateImage(BufferedImage image) {
	        final IntBuffer buffer = getIntBuffer();
	        for (int x = 0; x < image.getWidth(); x++) {
	            for (int y = 0; y < image.getHeight(); y++) {
	            	int pixel = buffer.get(x + y * image.getWidth());
	                int value = 0;
	                value = value | (pixel >> 24) & 0xFF;     // alpha component for RGBA
	                value = value << 8;
	                value = value | (pixel >> 0) & 0xFF;     // red component
	                value = value << 8;
	                value = value | (pixel >> 8) & 0xFF;      // green component
	                value = value << 8;
	                value = value | (pixel >> 16) & 0xFF;    // blue component
	                image.setRGB(x, y, value);
	            }
	        }
	    }

    }

}
