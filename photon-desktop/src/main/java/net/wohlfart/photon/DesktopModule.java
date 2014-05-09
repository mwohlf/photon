package net.wohlfart.photon;

import java.awt.Component;

import javax.inject.Singleton;

import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.render.RendererImpl;
import net.wohlfart.photon.time.ClockImpl;
import net.wohlfart.photon.time.TimerImpl;
import dagger.Module;
import dagger.Provides;


@Module(
	injects = {DesktopStart.class},
	library = false
)
public class DesktopModule {

	// a nice intro to dagger:
	// http://musingsofaprogrammingaddict.blogspot.de/2012/11/dagger-new-java-dependency-injection.html
	private final ClockImpl clockImpl = new ClockImpl();

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

	@Provides
	TimerImpl provideTimer() {
		return new TimerImpl(clockImpl);
	}

}
