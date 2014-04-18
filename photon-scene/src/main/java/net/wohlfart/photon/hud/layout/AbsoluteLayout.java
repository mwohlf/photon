package net.wohlfart.photon.hud.layout;

import java.util.Map;

import net.wohlfart.photon.tools.Position;


public class AbsoluteLayout extends AbstractLayoutStrategy<AbsoluteLayout.AbsoluteLayoutConstraint> {

	public static class AbsoluteLayoutConstraint extends LayoutConstraints {
		private final Position pos = new Position();

		public void setX(float x) {
			pos.setX(x);
		}

		public void setY(float y) {
			pos.setY(y);
		}

	}

	@Override
	void calculatePositions() {
		float screenWidth = screenDimension.getWidth();
		float screenHeight = screenDimension.getHeight();

		positions.clear(); // FIXME: try not to recreate positions all the time
		for (Map.Entry<IComponent, AbsoluteLayoutConstraint> entry : components.entrySet()) {
			AbsoluteLayoutConstraint constraint = entry.getValue();
			Position pos = positions.get(entry.getKey());
			if (pos == null) {
				pos = new Position();
				positions.put(entry.getKey(), pos);
			}
			pos.setX(constraint.pos.getX());
			pos.setY(constraint.pos.getY());
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

	@Override
	public boolean isDirty() {
		return isDirty;
	}

}
