package net.wohlfart.photon.hud.layout;

import java.util.HashMap;
import java.util.Map;

import net.wohlfart.photon.tools.Position;

public class CornerLayoutStrategy extends AbstractLayoutStrategy<CornerConstraints> {
 
    @Override
    public CornerConstraints addLayoutComponent(IComponent<CornerConstraints> comp) {
        assert (comp.getParent() != null);
        CornerConstraints result = new CornerConstraints(this);
        components.put(comp, result);
        isDirty = true;
        return result;
    }

    @Override
    protected void doLayout() {
        positions.clear();
        // trying to avoid a case/switch here since they are ugly, not sure if this is even uglier...
        // for each stack(each constraint/each enum) we need to remember the position of the last element, 
        // this is what this map is for:
        Map<CornerConstraints.Alignment, Position> lastPositions = new HashMap<>();
        for (Map.Entry<IComponent<CornerConstraints>, CornerConstraints> entry : components.entrySet()) {
            IComponent<CornerConstraints> component = entry.getKey();
            CornerConstraints constr = entry.getValue();
            Position position = constr.alignment.stackUp(lastPositions, constr, component);
            positions.put(component, position);
        }
        isDirty = false;
    }


}
