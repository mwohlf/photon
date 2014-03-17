package net.wohlfart.photon.entity;

import net.wohlfart.photon.node.QuadElement;

public class QuadEntity extends Entity3DImpl {

	@Override
	public void setup() {
		renderElements.add(QuadElement.createSolid());
	}

}

