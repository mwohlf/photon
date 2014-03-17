package net.wohlfart.photon.entity;

import net.wohlfart.photon.node.SphereElement;

public class SphereEntity extends Entity3DImpl {

	@Override
	public void setup() {
		renderElements.add(SphereElement.createSolid());
	}

}
