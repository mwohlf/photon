package net.wohlfart.photon.hud.layout;

import net.wohlfart.photon.hud.IScreenSizeListener;



public interface LayoutStrategy<C> extends IScreenSizeListener {

    // returning the constraints that can be manipulated
    C addLayoutComponent(IComponent comp);

    // remove component from the component tree
    void removeLayoutComponent(IComponent comp);

    // [0..1] origin is right
    float getLayoutAlignmentX(IComponent target);

    // [0..1] origin is top
    float getLayoutAlignmentY(IComponent target);

    // [0..1]
    float getLayoutWidth(IComponent target);

    // [0..1]
    float getLayoutHeight(IComponent target);

	boolean isDirty();

}
