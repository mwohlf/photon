package net.wohlfart.photon;

/*
import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.render.RendererImpl;
import net.wohlfart.photon.time.ClockImpl;
import net.wohlfart.photon.time.TimerImpl;
*/
import javax.inject.Singleton;

import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.render.RendererImpl;
import net.wohlfart.photon.state.StateManager;
import net.wohlfart.photon.time.ClockImpl;
import net.wohlfart.photon.time.TimerImpl;
import net.wohlfart.photon.ui.HomeActivity;
import net.wohlfart.photon.ui.PhotonActivity;
import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module( // see: http://www.joshlong.com/jl/blogPost/dependency_injection_with_dagger_on_android.html
		// enumerates all the classes that may receive injections that
		// are not defined explicitly using an @Provider-annotated method in the module
		injects = {
			PhotonActivity.class,
			HomeActivity.class
		},
	    complete = false
)
public class PhotonModule {

	private final PhotonApplication application;
	private final Context context;

	// a nice intro to dagger:
	// http://musingsofaprogrammingaddict.blogspot.de/2012/11/dagger-new-java-dependency-injection.html
	private final ClockImpl clockImpl = new ClockImpl();

	public PhotonModule(PhotonApplication application) {
		this.application = application;
		this.context = this.application.getApplicationContext();
	}

	@Provides @Singleton
	public PoolEventBus providePoolEventBus() {
		return new PoolEventBus();
	}

	@Provides @Singleton
	public StateManager provideStateManager() {
		return new StateManager();
	}

	@Provides @Singleton
	public RendererImpl providesRendererImpl() {
		return new RendererImpl();
	}

	@Provides
	public TimerImpl provideTimer() {
		return new TimerImpl(clockImpl);
	}

}

