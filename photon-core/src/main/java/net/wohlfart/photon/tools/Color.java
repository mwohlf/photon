package net.wohlfart.photon.tools;


public class Color  {

	public static final Color WHITE = new Color(1f,1f,1f,1f);

	public static final Color YELLOW = new Color(1f,1f,0f,1f);

	public static final Color BLACK = new Color(0f, 0f, 0f, 1f);

	public static final Color BLUE = new Color(0f, 0f, 1f, 1f);

	public static final Color GREEN = new Color(0f, 1f, 0f, 1f);

	public static final Color RED = new Color(1f, 0f, 0f, 1f);


	private final int red;   // [0...255]
	private final int green;
	private final int blue;
	private final int alpha;


	public Color(float red, float green, float blue) {
		this(red, green, blue, 1f);
	}


	public Color(float red, float green, float blue, float alpha) {
		this.red = 0xff & (int)(255f * red);
		this.green = 0xff & (int)(255f * green);
		this.blue = 0xff & (int)(255f * blue);
		this.alpha = 0xff & (int)(255f * alpha);
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	public int getAlpha() {
		return alpha;
	}


	public java.awt.Color getAwtColor() {
		return new java.awt.Color(red,green,blue,alpha);
	}

}
