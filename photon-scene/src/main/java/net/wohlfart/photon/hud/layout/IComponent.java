package net.wohlfart.photon.hud.layout;

import net.wohlfart.photon.render.IRenderer.IRenderElem;


/**
 * a basic element that can be layout-ed while in a containing element
 *
 */
public interface IComponent<C> extends IRenderElem {

    // needed by the layout manager,
    // FIXME: this should be in screen scale (0f-1f) so we don't need to divide by srceen size...
    public float getHeight();

    public float getWidth();

    public void setParent(Container<C> container);

    public Container<C> getParent();

}
