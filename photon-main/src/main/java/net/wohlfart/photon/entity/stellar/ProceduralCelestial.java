package net.wohlfart.photon.entity.stellar;

import java.util.Random;

import net.wohlfart.photon.texture.ISphereSurfaceColor;

public abstract class ProceduralCelestial extends AbstractCelestial {

    protected ISphereSurfaceColor type;
    protected long seed = -1;
    protected Random random;


    public ProceduralCelestial withType(ISphereSurfaceColor type) {
        this.type = type;
        return this;
    }

    public ProceduralCelestial withSeed(long seed) {
    	this.seed = seed;
    	this.random = new Random(seed);
    	return this;
    }

}
