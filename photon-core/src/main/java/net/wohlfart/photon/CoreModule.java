package net.wohlfart.photon;


import javax.inject.Singleton;

import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.resources.ResourceManager;
import dagger.Module;
import dagger.Provides;


@Module(library=true)
public class CoreModule {


	@Provides @Singleton
	PoolEventBus providePoolEventBus() {
		return new PoolEventBus();
	}

	/*
	@Provides @Singleton
	IGraphicContext provideGraphicContext() {
		return new GraphicContext();
	}
	*/

	// TODO: not sure if the resource manager belongs into the core
	@Provides @Singleton
	ResourceManager provideResourceManager() {
		return ResourceManager.INSTANCE;
	}

}
