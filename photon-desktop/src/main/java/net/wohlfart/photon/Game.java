package net.wohlfart.photon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game implements ILifecycleListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(Game.class);

	@Override
	public void init(IGraphicContext gfxContext) {
		LOGGER.info("init called");
	}

	@Override
	public void dispose(IGraphicContext gfxContext) {
		LOGGER.info("dispose called");
	}

	@Override
	public void display(IGraphicContext gfxContext) {
		LOGGER.info("display called");
	}

	@Override
	public void reshape(IGraphicContext gfxContext, int x, int y, int width, int height) {
		LOGGER.info("reshape called");
	}

}
