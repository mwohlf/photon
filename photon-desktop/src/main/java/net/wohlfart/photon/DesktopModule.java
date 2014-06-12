package net.wohlfart.photon;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Singleton;

import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.render.RendererImpl;
import net.wohlfart.photon.time.ClockImpl;
import net.wohlfart.photon.time.TimerImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.util.FPSAnimator;

import dagger.Module;
import dagger.Provides;


@Module(
	injects = {DesktopStart.class},
	library = false
)
public class DesktopModule {
	private static final Logger LOGGER = LoggerFactory.getLogger(DesktopModule.class);

	// a nice intro to dagger:
	// http://musingsofaprogrammingaddict.blogspot.de/2012/11/dagger-new-java-dependency-injection.html
	private final ClockImpl clockImpl = new ClockImpl();

	@Provides @Singleton
	public PoolEventBus providePoolEventBus() {
		LOGGER.debug("EventBus provider called");
		return new PoolEventBus();
	}

	@Provides @Singleton
	public Properties provideProperties() {
		LOGGER.debug("Properties provider called");
		final Properties properties = new Properties();
		InputStream in = null;
		try {
			in = getClass().getResourceAsStream("/desktop.properties");
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) { /* ignored */ }
			}
		}
		return properties;
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

	@Provides @Singleton
	public FPSAnimator providesAnimator(Properties properties) {
		LOGGER.debug("Animator provider called");
		return new FPSAnimator(30, true);
	}


	@Provides @Singleton
	public KeyListener providesKeyListener(PoolEventBus poolEventBus) {
		LOGGER.debug("KeyListener provider called");
		return new KeyListener(poolEventBus);
	}

}
