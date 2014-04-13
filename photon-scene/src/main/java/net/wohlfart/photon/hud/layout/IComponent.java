package net.wohlfart.photon.hud.layout;

import net.wohlfart.photon.graph.NodeSortStrategy.ISortToken;
import net.wohlfart.photon.render.IRenderer.IRenderNode;

public interface IComponent extends IRenderNode {

    static final ISortToken UI_SORT_TOKEN = new ISortToken() {

        @Override
        public boolean isTranslucent() {
            return true;
        }

        @Override
        public double getZOrder() {
            return 0;
        }

        @Override
        public String toString() {
            return "UI_SORT_TOKEN";
        }

    };

    // height in pixel
    public float getHeight();

    // width in pixel
    public float getWidth();

    public void setParent(IContainer<? extends LayoutConstraints> container);

    public IContainer<? extends LayoutConstraints> getParent();

}