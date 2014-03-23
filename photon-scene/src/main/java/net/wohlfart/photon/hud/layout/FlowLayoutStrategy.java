package net.wohlfart.photon.hud.layout;

import java.util.Map;

import net.wohlfart.photon.tools.Position;


public class FlowLayoutStrategy extends AbstractLayoutStrategy<FlowContraints> {

    @Override
    public FlowContraints addLayoutComponent(IComponent<FlowContraints> comp) {
        assert (comp.getParent() != null);
        FlowContraints result = new FlowContraints(this);
        components.put(comp, result);
        isDirty = true;
        return result;
    }

    @Override
    void doLayout() {
        float x = 0;
        float y = 0;
        float offset = 0;
        positions.clear();
        for (Map.Entry<IComponent<FlowContraints>, FlowContraints> entry : components.entrySet()) {
            x = 0;
            y = 0;
            IComponent<FlowContraints> component = entry.getKey();
            Container<FlowContraints> parent = component.getParent();
            // check if this component is nested
            Container<?> grandParent = parent.getParent();
            if (grandParent != null) {
                x = grandParent.getLayoutManager().getLayoutAlignmentX((IComponent) parent);
                y = grandParent.getLayoutManager().getLayoutAlignmentY((IComponent) parent);
            }
            Position position = new Position(x, y + offset); // position relative to the parent container
            positions.put(component, position);
            offset += component.getHeight()/600f;  // FIXME: srly?? this has to go!
        }
        isDirty = false;
    }



}
