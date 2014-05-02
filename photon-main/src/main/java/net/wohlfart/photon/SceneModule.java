package net.wohlfart.photon;

import net.wohlfart.photon.time.ClockImpl;
import net.wohlfart.photon.time.TimerImpl;
import dagger.Module;
import dagger.Provides;


@Module(
	includes = {CoreModule.class},
	injects=SceneApplication.class,
	library=true)
public class SceneModule {

	private final ClockImpl clockImpl = new ClockImpl();

	// a nice intro to dagger:
	// http://musingsofaprogrammingaddict.blogspot.de/2012/11/dagger-new-java-dependency-injection.html


	@Provides
	TimerImpl provideTimer() {
		return new TimerImpl(clockImpl);
	}

}
