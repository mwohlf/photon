package net.wohlfart.photon;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import com.jogamp.opengl.util.glsl.ShaderUtil;

public class LifecycleAdpator implements GLEventListener {

	private final GraphicContext gfxContext = new GraphicContext();
	private final ILifecycleListener listener;


	public LifecycleAdpator(ILifecycleListener listener) {
		this.listener = listener;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		System.err.println(Thread.currentThread()+" Entering initialization");
		System.err.println(Thread.currentThread()+" GL Profile: "+gl.getGLProfile());
		System.err.println(Thread.currentThread()+" GL:" + gl);
		System.err.println(Thread.currentThread()+" GL_VERSION=" + gl.glGetString(GL.GL_VERSION));
		System.err.println(Thread.currentThread()+" GL_EXTENSIONS:");
		System.err.println(Thread.currentThread()+"   " + gl.glGetString(GL.GL_EXTENSIONS));
		System.err.println(Thread.currentThread()+" swapInterval: (GL: "+gl.getSwapInterval()+")");
		System.err.println(Thread.currentThread()+" isShaderCompilerAvailable: " + ShaderUtil.isShaderCompilerAvailable(gl));
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
