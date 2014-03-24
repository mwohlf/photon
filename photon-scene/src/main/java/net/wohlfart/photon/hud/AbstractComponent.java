package net.wohlfart.photon.hud;

import net.wohlfart.photon.hud.layout.Container;
import net.wohlfart.photon.hud.layout.IComponent;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.RenderConfigImpl;

// basic ui component features:  width, height layout dirty, parent, layout constraints
public abstract class AbstractComponent<C> extends AbstractRenderElement implements IComponent<C> {

    protected Container<C> container; // the parent element

    protected float height;

    protected float width;

    protected boolean isDirty;


    public AbstractComponent() {
        renderConfig = RenderConfigImpl.BLENDING_ON;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public Container<C> getParent() {
        return container;
    }

    @Override
	public void setParent(Container<C> container) {
        this.container = container;
    }

}
