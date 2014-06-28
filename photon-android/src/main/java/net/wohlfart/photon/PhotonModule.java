package net.wohlfart.photon;

/*
import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.render.RendererImpl;
import net.wohlfart.photon.time.ClockImpl;
import net.wohlfart.photon.time.TimerImpl;
*/
import android.content.Context;
import dagger.Module;

@Module( // see: http://www.joshlong.com/jl/blogPost/dependency_injection_with_dagger_on_android.html
		// enumerates all the classes that may receive injections that
		// are not defined explicitly using an @Provider-annotated method in the module
		injects = {PhotonActivity.class},
	    complete = false
)
public class PhotonModule {

	private final PhotonApplication application;
	private final Context context;

	// a nice intro to dagger:
	// http://musingsofaprogrammingaddict.blogspot.de/2012/11/dagger-new-java-dependency-injection.html
	//private final ClockImpl clockImpl = new ClockImpl();

	public PhotonModule(PhotonApplication application) {
		this.application = application;
		this.context = this.application.getApplicationContext();
	}
/*
	@Provides @Singleton
	public PoolEventBus providePoolEventBus() {
		return new PoolEventBus();
	}

	@Provides
	public RendererImpl providesRendererImpl() {
		return new RendererImpl();
	}

	@Provides
	public TimerImpl provideTimer() {
		return new TimerImpl(clockImpl);
	}
*/
}

