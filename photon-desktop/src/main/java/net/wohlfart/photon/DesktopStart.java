package net.wohlfart.photon;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
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

import dagger.ObjectGraph;


public class DesktopStart {
	private static final Logger LOGGER = LoggerFactory.getLogger(DesktopStart.class);

	private static String TITLE = "JOGL 2.0 Setup (GLCanvas)";  // window's title
	private static final int CANVAS_WIDTH = 800;  // width of the drawable
	private static final int CANVAS_HEIGHT = 600; // height of the drawable



	// platform dependant drawing target, already wired with the animator loop
	protected final OpenGlCanvas canvas;

	// the application that has to implement the rendering callbacks
	//protected final Application application;


	protected final PoolEventBus eventBus;

	protected final JFrame frame;

	protected final Game game;


	@Inject
	public DesktopStart(/*Application application,*/ PoolEventBus eventBus, OpenGlCanvas canvas){
		//this.application = application;
		this.eventBus = eventBus;
		this.canvas = canvas;
		this.frame = new JFrame();
		this.game = new Game();
	}


	public void start() throws InvocationTargetException, InterruptedException {

		EventQueue.invokeAndWait(new Runnable() {

			@Override
			public void run() {

				canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
				canvas.addLifecycleListener(game);
				canvas.addKeyListener(new KeyListener());

				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				frame.getContentPane().add(canvas.asComponent());
				frame.addWindowListener(new WindowListener());
				frame.addKeyListener(new KeyListener());
				frame.setTitle(TITLE);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);

				eventBus.register(new ShutdownListener());
			}
		});

		canvas.startAnimator(); // start the animation loop
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
				canvas.stopAnimator();
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


	// -- the static bootup code

	public static void main(String[] args) {
		try {
			logBootInfo();
			final ObjectGraph objectGraph = ObjectGraph.create(new DesktopModule());
			final DesktopStart desktop = objectGraph.get(DesktopStart.class);
			desktop.start();
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

}

