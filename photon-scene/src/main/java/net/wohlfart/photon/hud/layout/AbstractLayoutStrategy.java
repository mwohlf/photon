package net.wohlfart.photon.hud.layout;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.wohlfart.photon.tools.Dimension;
import net.wohlfart.photon.tools.Position;

public abstract class AbstractLayoutStrategy<C> implements LayoutStrategy<C>  {

    // the components mapped to their constraints
    protected final Map<IComponent, C> components = new LinkedHashMap<>();

    // the components mapped to their positions
    protected final Map<IComponent, Position> positions = new HashMap<>();

    protected final Dimension screenDimension = new Dimension();

    // set to true if layout needs to be recalculated
    protected boolean isDirty = true;


    @Override
    public float getLayoutAlignmentX(IComponent target) {
        return getPosition(target).getX();
    }

    @Override
    public float getLayoutAlignmentY(IComponent target) {
        return getPosition(target).getY();
    }

    @Override
    public C addLayoutComponent(IComponent comp) {
    	C value = createConstraint(comp);
    	components.put(comp, value);
        isDirty = true;
    	return value;
    }

    @Override
    public void removeLayoutComponent(IComponent comp) {
        components.remove(comp);
        isDirty = true;
    }


	@Override
	public void setScreenDimension(Dimension screenDimension) {
		this.screenDimension.set(screenDimension);
        isDirty = true;
	}

    protected Position getPosition(IComponent comp) {
    	if (isDirty) {
    		calculatePositions();
            isDirty = false;
    	}
    	assert positions.containsKey(comp) : "position was not calcualted for component, check the calculatePositions method " + comp;
    	return positions.get(comp);
    }

    abstract C createConstraint(IComponent comp);

    abstract void calculatePositions();

}
