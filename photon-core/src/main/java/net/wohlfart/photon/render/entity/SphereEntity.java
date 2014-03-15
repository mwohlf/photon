package net.wohlfart.photon.render.entity;

import net.wohlfart.photon.render.node.SphereElement;

public class SphereEntity extends Entity3DImpl {

	@Override
	public void setup() {
		renderElements.add(SphereElement.createSolid());
	}

}
