package net.wohlfart.photon.entity.stellar;

import java.util.Random;

import net.wohlfart.photon.texture.ISphereSurfaceColor;


// see: http://en.wikipedia.org/wiki/Stellar_classification#Modern_interpretation
public abstract class Sun extends ProceduralCelestial {

    protected ISphereSurfaceColor type;
    protected long seed = -1;
    protected Random random;



    protected ISphereSurfaceColor getType() {
    	return type;
    }

    @Override
	public ProceduralCelestial withType(ISphereSurfaceColor type) {
        this.type = type;
        return this;
    }

    protected long getSeed() {
    	return seed;
    }

    @Override
	public ProceduralCelestial withSeed(long seed) {
    	this.seed = seed;
    	this.random = new Random(seed);
    	return this;
    }

}
