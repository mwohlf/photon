package net.wohlfart.photon.hud;


import javax.vecmath.Vector3f;

import net.wohlfart.photon.hud.layout.AbsoluteLayout;
import net.wohlfart.photon.hud.layout.AbsoluteLayout.AbsoluteLayoutConstraint;
import net.wohlfart.photon.hud.layout.ContainerImpl;
import net.wohlfart.photon.tools.Quaternion;

// an entity to hold the ui components
public class SimpleLayer extends AbstractLayer {

    private float time;

    public SimpleLayer() {
    	setup();
    }

    private void setup() {
    	ContainerImpl<AbsoluteLayoutConstraint> container = new ContainerImpl<>(new AbsoluteLayout());
    	AbsoluteLayoutConstraint constraints = container.addChild(new Label().withText("hello world"));
    	constraints.setX(-0.4f);
    	constraints.setY(-0.4f);
    	containers.add(container);
    }

    @Override
    public void update(Quaternion rot, Vector3f mov, float delta) {
        time += delta;
        if (time > 0.5) {
            time = 0;
        }
    }

}
