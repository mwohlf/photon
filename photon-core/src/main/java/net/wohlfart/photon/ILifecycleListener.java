package net.wohlfart.photon;


public interface ILifecycleListener {

	public void init(IGraphicContext gfxContext);

	public void dispose(IGraphicContext gfxContext);

	public void display(IGraphicContext gfxContext);

	public void reshape(IGraphicContext gfxContext, int x, int y, int width, int height);

}
