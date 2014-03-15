package net.wohlfart.photon;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

public class LifecycleAdpator implements GLEventListener {

	private final IGraphicContext gfxContext = new GraphicContext();
	private final ILifecycleListener listener;


	public LifecycleAdpator(ILifecycleListener listener) {
		this.listener = listener;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		listener.init(gfxContext.init(drawable));
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		listener.dispose(gfxContext.init(drawable));
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		listener.display(gfxContext.init(drawable));

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		listener.reshape(gfxContext.init(drawable), x, y, width, height);
	}

}
