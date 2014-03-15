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
public class OpenGlCanvas {
	private static final int FPS = 60; // animator's target frames per second

	private final GLCanvas delegate;

	@Inject
	public OpenGlCanvas() {
		super();
		delegate = new GLCanvas(new GLCapabilities(GLProfile.getDefault()));
		FPSAnimator animator = new FPSAnimator(FPS, true);
		animator.add(delegate);
	}

	public void setPreferredSize(Dimension dimension) {
		delegate.setPreferredSize(dimension);
	}

	public void setLifecycleListener(Application listener) {
		delegate.addGLEventListener(listener);
	}

	public void addKeyListener(KeyListener keyListener) {
		delegate.addKeyListener(keyListener);
	}

	public void startAnimator() {
		delegate.getAnimator().start();
	}

	public void stopAnimator() {
		delegate.getAnimator().stop();
	}

	Component asComponent() {
		return delegate;
	}
}
