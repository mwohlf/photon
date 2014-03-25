package net.wohlfart.photon.hud.layout;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.wohlfart.photon.tools.Position;

public abstract class AbstractLayoutStrategy<C> implements LayoutStrategy<C>  {

    // the components mapped to their constraints
    protected final Map<IComponent<C>, C> components = new LinkedHashMap<>();
    // the components mapped to their positions (borders not included)
    protected final Map<IComponent<C>, Position> positions = new HashMap<>();
    // set to true if layout needs to be recalculated
    protected boolean isDirty = true;


    @Override
    public float getLayoutAlignmentX(IComponent<C> target) {
        if (isDirty) {
            doLayout();
        }
        return positions.get(target).getX();
    }

    @Override
    public float getLayoutAlignmentY(IComponent<C> target) {
        if (isDirty) {
            doLayout();
        }
        return positions.get(target).getY();
    }

    @Override
    public void removeLayoutComponent(IComponent<C> comp) {
        components.remove(comp);
        isDirty = true;
    }

    @Override
    public void setDirty() {
        this.isDirty = true;
    }


    abstract void doLayout();

}
