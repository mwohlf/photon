package net.wohlfart.photon.hud.layout;


public interface LayoutStrategy<C> {
    
    // returning the constraints that can be manipulated 
    C addLayoutComponent(IComponent<C> comp);

    // [0..1] origin right
    float getLayoutAlignmentX(IComponent<C> target);

    // [0..1] origin top
    float getLayoutAlignmentY(IComponent<C> target);

    void removeLayoutComponent(IComponent<C> comp);

    void setDirty();
     
}
