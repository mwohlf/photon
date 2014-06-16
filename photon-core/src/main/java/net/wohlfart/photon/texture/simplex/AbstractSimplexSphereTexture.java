package net.wohlfart.photon.texture.simplex;

import net.wohlfart.photon.texture.ISphereSurfaceColor;
import net.wohlfart.photon.tools.SimplexNoise;

public abstract class AbstractSimplexSphereTexture implements ISphereSurfaceColor {

	// default values
    // all lengths in [10^3 km]

    protected float minRadius = 6.371f; // in 10^6 m = 1000 km (earth has 6.371 x10^3 km)
    protected float maxRadius = 100.0f; // sun has 696 x10^3 km

    protected float maxPathRadius = 6000000.0f;
    protected float minPathRadius = 700.0f; // sun-earth is 147098; sun-pluto is 5913520

    protected float minRot = (float) Math.PI * 2f / 60f; // in rad/s, 2pi mean one rotation per second 2pi/3 means one rotation in 3 sec
    protected float maxRot = (float) Math.PI * 2f / 300f;

    protected float maxAxisDeplacement = 0.25f; // this value is randomly added to a normalized up vectors x and y values, earth is around 23.4 degree


    // adding octaves
    protected double createNoise(float x, float y, float z, float v, float persistence, int octaves) {
        double result = 0;
        float max = 0;
        for (int i = 0; i < octaves; i++) {
            final float frequency = (float) Math.pow(2, i);
            final float amplitude = (float) Math.pow(persistence, i);
            result += createNoise(x, y, z, v, amplitude, frequency);
            max += amplitude;
        }
        return result / max;
    }

    // calling the noise
    protected double createNoise(float x, float y, float z, float v, float amplitude, float frequency) {
        // the noise returns [-1 .. +1]
        // double noise = PerlinNoise.noise(x * frequency, y * frequency, z * frequency);
        final double noise = SimplexNoise.noise(x * frequency, y * frequency, z * frequency, v);
        return amplitude * noise;
    }

}
