package net.wohlfart.photon;

import javax.inject.Singleton;

import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.resources.ResourceManager;
import net.wohlfart.photon.time.ClockImpl;
import net.wohlfart.photon.time.TimerImpl;
import dagger.Module;
import dagger.Provides;


@Module(
	injects= {Application.class, PoolEventBus.class},
	library=true
)
public class ApplicationModule {

	private final ClockImpl clockImpl = new ClockImpl();

	@Provides @Singleton
	PoolEventBus providePoolEventBus() {
		return new PoolEventBus();
	}

	@Provides @Singleton
	IGraphicContext provideGraphicContext() {
		return new GraphicContext();
	}

	@Provides @Singleton
	ResourceManager provideResourceManager() {
		return ResourceManager.INSTANCE;
	}

	@Provides
	TimerImpl provideTimer() {
		return new TimerImpl(clockImpl);
	}

}
