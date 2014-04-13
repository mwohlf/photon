package net.wohlfart.photon.hud.layout;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.graph.NodeSortStrategy.ISortToken;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.tools.Dimension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @param <P> the constraints for the layout strategy of this components children

 */
public class ContainerImpl<C extends LayoutConstraints> implements IContainer<C> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Container.class);

    // the order in which the children are layout-ed and rendered is important
    protected final List<IComponent> children = new ArrayList<>();

    protected final LayoutStrategy<C> layoutManager;

    protected float height;

    protected float width;

    protected boolean isDirty;



    public ContainerImpl(LayoutStrategy<C> layoutStrategy) {
        this.layoutManager = layoutStrategy;
    }

	@Override
	public ISortToken getSortToken() {
		return UI_SORT_TOKEN;
	}

    @Override
    public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
    	// not sure if the container should do rendering ???
    	//this.screenDimension = renderer.getScreenDimension();
        //renderer.setRenderConfig(shaderId, renderConfig);
        //renderer.setUniformValues(getUniformValues());
        //renderer.drawGeometry(getGeometry());
        renderer.renderChildren(tree);
    }

    @Override
    public void setScreenDimension(Dimension screenDimension) {
    	layoutManager.setScreenDimension(screenDimension);
    }

    @Override
	public LayoutStrategy<C> getLayoutManager() {
        return layoutManager;
    }

    @Override
	public List<IComponent> getComponents() {
        return children;
    }

    @Override
	public float getHeight() {
    	assert height > 0;
        return height;
    }

    @Override
	public float getWidth() {
    	assert width > 0;
        return width;
    }

    @Override
	public C addChild(IComponent comp) {
        addComponent(children.size(), comp);
        return layoutManager.addLayoutComponent(comp);
    }

    @Override
	public void removeChild(IComponent comp) {
        children.remove(comp);
    }

    private void addComponent(int pos, IComponent component) {
        children.add(pos, component);
        component.setParent(this); // components need to know their parent
    }

    // override this for subcomponents
	@Override
	public IContainer<?> getParent() {
		return null;
	}

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [width=" + width
                + ", height=" + height + "]";
    }

	@Override
	public void setParent(IContainer<? extends LayoutConstraints> container) {
		// TODO Auto-generated method stub

	}

}
