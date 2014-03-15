package net.wohlfart.photon;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import net.wohlfart.photon.events.CommandEvent;
import net.wohlfart.photon.events.CommandEvent.CommandKey;
import net.wohlfart.photon.events.MoveEvent;
import net.wohlfart.photon.events.PoolEventBus;
import net.wohlfart.photon.events.RotateEvent;
import net.wohlfart.photon.events.Subscribe;
import net.wohlfart.photon.tools.ObjectPool.PoolableObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;

import dagger.ObjectGraph;


public class DesktopStart {
	private static final Logger LOGGER = LoggerFactory.getLogger(DesktopStart.class);

	private static String TITLE = "JOGL 2.0 Setup (GLCanvas)";  // window's title
	private static final int CANVAS_WIDTH = 800;  // width of the drawable
	private static final int CANVAS_HEIGHT = 600; // height of the drawable
	private static final int FPS = 60; // animator's target frames per second

	private volatile AnimatorBase animator;

	private volatile JFrame frame;

	private volatile PoolEventBus eventBus;


	public static void main(String[] args) {
		logBootInfo();
		final ObjectGraph objectGraph = ObjectGraph.create(new ApplicationModule());
		// platform independent stuff
		final Application app = objectGraph.get(Application.class);
		final PoolEventBus eventBus = objectGraph.get(PoolEventBus.class);
		try {
			new DesktopStart() .start(app, eventBus);
		} catch (InvocationTargetException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}


	private static void logBootInfo() {
		logBootInfo(
				"java.version",
				"java.vendor",
				"java.vm.version",
				"java.vm.vendor",
				"java.vm.name"
				);
	}

	private static void logBootInfo(String... strings) {
		for (String string : strings) {
			LOGGER.info(string + ": " + System.getProperty(string));
		}
	}




	public void start(final Application app, final PoolEventBus eventBus) throws InvocationTargetException, InterruptedException {

		this.eventBus = eventBus;

		EventQueue.invokeAndWait(new Runnable() {

			@Override
			public void run() {

				final GLProfile glp = GLProfile.getDefault();
				// Specifies a set of OpenGL capabilities, based on your profile.
				GLCapabilities caps = new GLCapabilities(glp);
				// Allocate a GLDrawable, based on your OpenGL capabilities.
				final GLCanvas canvas = new GLCanvas(caps);
				canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
				canvas.addGLEventListener(app);

				// Create a animator that drives canvas' display() at the specified FPS.
				animator = new FPSAnimator(canvas, FPS, true);

				canvas.addKeyListener(new KeyListener());
				// animator = new Animator(canvas);
				// animator.setRunAsFastAsPossible(true);

				frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				frame.getContentPane().add(canvas);
				frame.addWindowListener(new WindowListener());
				frame.addKeyListener(new KeyListener());
				frame.setTitle(TITLE);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				eventBus.register(new ShutdownListener());
			}
		});

		animator.start(); // start the animation loop
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
			LOGGER.info("shutdown() called");
			if (event.getKey() == CommandKey.EXIT) {
				animator.stop();
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						frame.setVisible(false);
						frame.dispose();
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
			default:
				evt = null;
			}
			if (evt!= null) {
				eventBus.post(evt);
			}
		}
	}
}

