package net.wohlfart.photon;

import java.awt.Dimension;
import java.awt.event.KeyListener;

import javax.media.opengl.awt.GLCanvas;

import com.jogamp.opengl.util.FPSAnimator;


public class OpenGlCanvas<T> extends GLCanvas {
	private static final long serialVersionUID = 1L;

	private static final int FPS = 120; // animator's target frames per second

	public OpenGlCanvas() {
		super();
		FPSAnimator animator = new FPSAnimator(FPS, true);
		animator.add(this);
	}

	@SuppressWarnings("unchecked")
	public T asWidget() {
		return (T) super.getUpstreamWidget();
	}

	@Override
	public void setPreferredSize(Dimension dimension) {
		super.setPreferredSize(dimension);
	}

	public void addLifecycleListener(ILifecycleListener listener) {
		super.addGLEventListener(new LifecycleAdpator(listener));
	}

	@Override
	public void addKeyListener(KeyListener keyListener) {
		super.addKeyListener(keyListener);
	}

	public void startAnimator() {
		super.getAnimator().start();
	}

	public void stopAnimator() {
		super.getAnimator().stop();
	}

}
