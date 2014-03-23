package net.wohlfart.photon.entity;

import net.wohlfart.photon.node.SphereElement;

public class SphereEntity extends EntityImpl {

	@Override
	public void setup() {
		renderElements.add(SphereElement.createSolid());
	}

}
