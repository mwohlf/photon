package net.wohlfart.photon.entity;

import net.wohlfart.photon.node.CubeElement;

public class CubeEntity extends EntityImpl {

	public CubeEntity(float side) {
		renderElements.add(CubeElement.createGrid(side));
	}

}
