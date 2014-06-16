package net.wohlfart.photon;

import javax.inject.Singleton;

import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.render.RendererImpl;
import net.wohlfart.photon.time.ClockImpl;
import net.wohlfart.photon.time.TimerImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module( // see: http://www.joshlong.com/jl/blogPost/dependency_injection_with_dagger_on_android.html
		// enumerates all the classes that may receive injections that
		// are not defined explicitly using an @Provider-annotated method in the module
		injects = {PhotonActivity.class} )
public class PhotonModule {
	private static final Logger LOGGER = LoggerFactory.getLogger(PhotonModule.class);

	private final PhotonApplication application;
	private final Context context;

	// a nice intro to dagger:
	// http://musingsofaprogrammingaddict.blogspot.de/2012/11/dagger-new-java-dependency-injection.html
	private final ClockImpl clockImpl = new ClockImpl();

	@Provides @Singleton
	public PoolEventBus providePoolEventBus() {
		LOGGER.debug("EventBus provider called");
		return new PoolEventBus();
	}

	public PhotonModule(PhotonApplication application) {
		this.application = application;
		this.context = this.application.getApplicationContext();
	}

	@Provides
	public RendererImpl providesRendererImpl() {
		LOGGER.debug("RendererImpl provider called");
		return new RendererImpl();
	}

	@Provides
	public TimerImpl provideTimer() {
		LOGGER.debug("TimerImpl provider called");
		return new TimerImpl(clockImpl);
	}
}

