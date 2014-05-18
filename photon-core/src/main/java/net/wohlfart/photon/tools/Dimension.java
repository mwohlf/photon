package net.wohlfart.photon.tools;

public class Dimension {

	float width = 0;
	float height = 0;

	public Dimension() {

	}

	public Dimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void set(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public void set(Dimension dim) {
		this.width = dim.getWidth();
		this.height = dim.getHeight();
	}

	@Override
	public String toString() {
		return "Dimension [width=" + width + ", height=" + height + "]";
	}

}
