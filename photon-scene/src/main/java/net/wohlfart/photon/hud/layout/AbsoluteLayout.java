package net.wohlfart.photon.hud.layout;

import java.util.Map;

import net.wohlfart.photon.tools.Position;


public class AbsoluteLayout extends AbstractLayoutStrategy<AbsoluteLayout.AbsoluteLayoutConstraint> {


	public class AbsoluteLayoutConstraint extends LayoutConstraints {

	}

	@Override
	void calculatePositions() {
		positions.clear(); // FIXME: try not to recreate positions all the time
		for (Map.Entry<IComponent, AbsoluteLayoutConstraint> entry : components.entrySet()) {
			Position pos = positions.get(entry.getKey());
			if (pos == null) {
				pos = new Position();
				positions.put(entry.getKey(), pos);
			}
			pos.setX(0);
			pos.setY(0);
		}
		isDirty = false;
	}

	@Override
	public float getLayoutWidth(IComponent target) {
		return 0;
	}

	@Override
	public float getLayoutHeight(IComponent target) {
		return 0;
	}

	@Override
	AbsoluteLayoutConstraint createConstraint(IComponent comp) {
		return new AbsoluteLayoutConstraint();
	}

}
