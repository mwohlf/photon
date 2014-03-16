package net.wohlfart.photon;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyListener;

import javax.inject.Inject;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.FPSAnimator;




// wrap the Canvas for clients so they don't depend on jogl2
// TODO: probably different kinds of components needed for different platforms, use generics..
public class OpenGlCanvas {
	private static final int FPS = 120; // animator's target frames per second

	private final GLCanvas canvas;


	@Inject
	public OpenGlCanvas() {
		super();
		canvas = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
		FPSAnimator animator = new FPSAnimator(FPS, true);
		animator.add(canvas);
	}

	public void setPreferredSize(Dimension dimension) {
		canvas.setPreferredSize(dimension);
	}

	public void addLifecycleListener(ILifecycleListener listener) {
		canvas.addGLEventListener(new LifecycleAdpator(listener));
	}

	public void addKeyListener(KeyListener keyListener) {
		canvas.addKeyListener(keyListener);
	}

	public void startAnimator() {
		canvas.getAnimator().start();
	}

	public void stopAnimator() {
		canvas.getAnimator().stop();
	}

	// generics ?
	Component asComponent() {
 		return canvas;
	}
}
