package net.wohlfart.photon.hud.layout;

import java.util.List;

import net.wohlfart.photon.hud.IScreenSizeListener;



/**
 * a basic element that can be layout-ed while in a containing element
 * all containers are components
 */
public interface IContainer<C extends LayoutConstraints> extends IComponent, IScreenSizeListener {

	@Override
	public float getHeight();

    @Override
	public float getWidth();

    public void removeChild(IComponent comp);

    public C addChild(IComponent comp);

	public List<IComponent> getComponents();

	public LayoutStrategy<C> getLayoutManager();
}
