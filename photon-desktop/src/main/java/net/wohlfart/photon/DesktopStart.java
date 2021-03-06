package net.wohlfart.photon;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.inject.Inject;
import javax.media.nativewindow.WindowClosingProtocol;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

import net.wohlfart.photon.events.CommandEvent;
import net.wohlfart.photon.events.CommandEvent.CommandKey;
import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.events.Subscribe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;

import dagger.ObjectGraph;


public class DesktopStart {
	private static final Logger LOGGER = LoggerFactory.getLogger(DesktopStart.class);

	// event Bus to listen for the quit event
	protected final PoolEventBus eventBus;

	// ILifecycleListener already wired with EventBus, RendererImpl, TimerImpl, StateManager
	protected final MainApplication game;

	// application properties read from properties file in classpath
	protected final Properties properties;

	protected final FPSAnimator animator;

	protected final Display display;

	protected final Screen screen;

	protected final GLWindow window;

	protected final KeyListener keyListener;

	private final Object lock = new Object();


	public static void main(String[] args) {
		try {
			logJavaInfo();
			final ObjectGraph objectGraph = ObjectGraph.create(new DesktopModule());
			final DesktopStart desktop = objectGraph.get(DesktopStart.class);
			desktop.configure().start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	@Inject // constructor injection done by dagger
	public DesktopStart(MainApplication game,
			PoolEventBus eventBus,
			Properties properties,
			FPSAnimator animator,
			KeyListener keyListener){
		this.game = game;
		this.eventBus = eventBus;
		this.properties = properties;
		this.animator = animator;
		this.keyListener = keyListener;

		final GLProfile glProfile = GLProfile.get(GLProfile.GL2);
		GLProfile.initSingleton();
		final GLCapabilities glCaps = new GLCapabilities(glProfile);
		final int aaSamples = Integer.parseInt(properties.getProperty("aaSamples", "0"));
		if (aaSamples > 0) {
			glCaps.setSampleBuffers(true);
			glCaps.setNumSamples(4);
		} else {
			glCaps.setSampleBuffers(false);
		}

		this.display = NewtFactory.createDisplay(null);
		this.screen = NewtFactory.createScreen(display, 0); // screen 0
		this.window = GLWindow.create(screen, glCaps);
	}

	public DesktopStart configure() throws InvocationTargetException, InterruptedException {

		final String title = properties.getProperty("title");
		final int width = Integer.valueOf(properties.getProperty("width"));
		final int height = Integer.valueOf(properties.getProperty("height"));

		window.setSize(width, height);
		window.setTitle(title);
		window.setPointerVisible(true);
		window.setDefaultCloseOperation(WindowClosingProtocol.WindowClosingMode.DO_NOTHING_ON_CLOSE);
		window.addWindowListener(new CloseListener());

		animator.add(window);

		eventBus.register(this);

		return this;
	}

	public void start() throws InterruptedException {
		animator.start();  // need the animator to run in order to get events
		window.addGLEventListener(new LifecycleAdpator(game));
		window.addKeyListener(keyListener);
		window.setVisible(true);

		synchronized (lock) {
			lock.wait();
		}

		LOGGER.info("lock released, shutdown running");
		animator.stop();
		window.destroy();
		screen.destroy();
		display.destroy();
	}

	@Subscribe
	public void shutdown(CommandEvent event) throws InvocationTargetException, InterruptedException {
		if (event.getKey() == CommandKey.EXIT) {
			LOGGER.info("shutdown() called");
			synchronized (lock) {
				lock.notify();
			}
		}
	}

	private class CloseListener extends WindowAdapter {
		@Override
		public void windowDestroyNotify(WindowEvent evt) {
			eventBus.post(CommandEvent.exit());
		}
	}

	private static void logJavaInfo() {
		logJavaInfo(
				"java.version",
				"java.vendor",
				"java.vm.version",
				"java.vm.vendor",
				"java.vm.name"
				);
	}

	private static void logJavaInfo(String... strings) {
		for (String string : strings) {
			LOGGER.info(string + ": " + System.getProperty(string));
		}
	}

}

