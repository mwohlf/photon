package net.wohlfart.photon;


import java.awt.Component;

import javax.inject.Singleton;

import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.render.RendererImpl;
import dagger.Module;
import dagger.Provides;

@Module(library=true)
public class CoreModule {

	@Provides @Singleton
	public PoolEventBus providePoolEventBus() {
		return new PoolEventBus();
	}

	@Provides
	public OpenGlCanvas<Component> provideOpenGlCanvas() {
		return new OpenGlCanvas<Component>();
	}

	@Provides
	public RendererImpl providesRendererImpl() {
		return new RendererImpl();
	}

}
