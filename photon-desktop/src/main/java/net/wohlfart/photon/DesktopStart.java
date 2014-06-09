package net.wohlfart.photon;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.inject.Inject;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

import net.wohlfart.photon.events.CommandEvent;
import net.wohlfart.photon.events.CommandEvent.CommandKey;
import net.wohlfart.photon.events.MoveEvent;
import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.events.RotateEvent;
import net.wohlfart.photon.events.Subscribe;
import net.wohlfart.photon.tools.ObjectPool.PoolableObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;

import dagger.ObjectGraph;


public class DesktopStart {
	private static final Logger LOGGER = LoggerFactory.getLogger(DesktopStart.class);

	// platform dependant drawing target, already wired with the animator loop
	protected final OpenGlCanvas<Component> canvas;

	// event Bus to listen for the quit event
	protected final PoolEventBus eventBus;

	// ILifecycleListener already wired with EventBus, RendererImpl, TimerImpl, StateManager
	protected final MainApplication game;

	// application properties read from properties file in classpath
	protected final Properties properties;


	public static void main(String[] args) {
		try {
			logJavaInfo();
			final ObjectGraph objectGraph = ObjectGraph.create(new DesktopModule());
			final DesktopStart desktop = objectGraph.get(DesktopStart.class);
			desktop.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	@Inject // constructor injection done by dagger
	public DesktopStart(MainApplication game,
			PoolEventBus eventBus,
			OpenGlCanvas<Component> canvas,
			Properties properties){
		this.game = game;
		this.eventBus = eventBus;
		this.canvas = canvas;
		this.properties = properties;
	}

	public void start() throws InvocationTargetException, InterruptedException {

		// setup
		final GLProfile glProfile = GLProfile.get(GLProfile.GL2ES2);
		GLProfile.initSingleton();
		GLCapabilities glCaps = new GLCapabilities(glProfile);


		final String title = properties.getProperty("title");
		final int width = Integer.valueOf(properties.getProperty("width"));
		final int height = Integer.valueOf(properties.getProperty("height"));


		Display display = NewtFactory.createDisplay(null);
		Screen screen = NewtFactory.createScreen(display, 0); // screen 0
		GLWindow glWindow = GLWindow.create(screen, glCaps);

		glWindow.setSize(width, height);
		glWindow.setTitle(title);


		LifecycleAdpator drawable = new LifecycleAdpator(game);
		glWindow.addGLEventListener(drawable);

		FPSAnimator animator = new FPSAnimator(30, true);
		animator.add(glWindow);



		glWindow.setVisible(true);
		animator.start();


		/*
				canvas.setPreferredSize(new Dimension(width, height));
				canvas.addLifecycleListener(game);
				canvas.addKeyListener(new KeyListener());

				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				frame.getContentPane().add(canvas.asWidget());
				frame.addWindowListener(new WindowListener());
				frame.addKeyListener(new KeyListener());
				frame.setTitle(title);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);

				eventBus.register(new ShutdownListener());
		 */


		//canvas.startAnimator(); // start the animation loop
	}

	public void fireShutdown() {
		eventBus.post(CommandEvent.exit());
		while (eventBus.hasEvent()) {
			eventBus.fireEvent();
		}
	}

	public class ShutdownListener {
		@Subscribe
		public void shutdown(CommandEvent event) {
			if (event.getKey() == CommandKey.EXIT) {
				LOGGER.info("shutdown() called");
				canvas.stopAnimator();
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						// TODO
					}
				});
			}
		}
	}

	public class WindowListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent evt) {
			eventBus.post(CommandEvent.exit());
			while (eventBus.hasEvent()) {
				eventBus.fireEvent();
			}
		}
	}

	public class KeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			float time = 0.1f;
			PoolableObject evt;
			// FIXME: need a map to be able to configure keys...
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				evt = CommandEvent.exit();
				break;
			case KeyEvent.VK_W:
				evt = MoveEvent.moveForward(time);
				break;
			case KeyEvent.VK_Y:
				evt = MoveEvent.moveBack(time);
				break;
			case KeyEvent.VK_A:
				evt = MoveEvent.moveLeft(time);
				break;
			case KeyEvent.VK_S:
				evt = MoveEvent.moveRight(time);
				break;
			case KeyEvent.VK_Q:
				evt = MoveEvent.moveUp(time);
				break;
			case KeyEvent.VK_X:
				evt = MoveEvent.moveDown(time);
				break;
			case KeyEvent.VK_LEFT:
				evt = RotateEvent.rotateLeft(time);
				break;
			case KeyEvent.VK_RIGHT:
				evt = RotateEvent.rotateRight(time);
				break;
			case KeyEvent.VK_UP:
				evt = RotateEvent.rotateUp(time);
				break;
			case KeyEvent.VK_DOWN:
				evt = RotateEvent.rotateDown(time);
				break;
			case KeyEvent.VK_PAGE_UP:
				evt = RotateEvent.rotateClockwise(time);
				break;
			case KeyEvent.VK_PAGE_DOWN:
				evt = RotateEvent.rotateCounterClockwise(time);
				break;
			case KeyEvent.VK_0:
				evt = CommandEvent.dumpScene();
				break;
			case KeyEvent.VK_9:
				evt = CommandEvent.debugRenderer();
				break;
			default:
				evt = null;
			}
			if (evt!= null) {
				eventBus.post(evt);
			}
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

