package net.wohlfart.photon.hud.layout;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.photon.graph.ITree;
import net.wohlfart.photon.render.AbstractRenderElement;
import net.wohlfart.photon.render.IRenderer;
import net.wohlfart.photon.render.IRenderer.IRenderNode;
import net.wohlfart.photon.tools.Dimension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @param <C> the constraints for the layout strategy of this components children

 */
public class Container<P> extends AbstractRenderElement {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Container.class);

    // the order in which the children are layout-ed and rendered is important
    private final List<IComponent<P>> children = new ArrayList<>();

    private final LayoutStrategy<P> layoutManager;

    protected float height;

    protected float width;

    protected boolean isDirty;

    public Container(LayoutStrategy<P> layoutStrategy) {
        this.layoutManager = layoutStrategy;
    }

    @Override
    public void accept(IRenderer renderer, ITree<IRenderNode> tree) {
    	Dimension dim = renderer.getScreenDimension();
    	width = dim.getWidth();
    	height = dim.getHeight();

        for (IComponent<P> comp : children) {
            comp.accept(renderer, tree);
        }
        renderer.renderChildren(tree);
    }

    public LayoutStrategy<P> getLayoutManager() {
        return layoutManager;
    }

    public List<IComponent<P>> getComponents() {
        return children;
    }

    public float getHeight() {
    	assert height > 0;
        return height;
    }

    public float getWidth() {
    	assert width > 0;
        return width;
    }

    public P add(IComponent<P> comp) {
        addComponent(children.size(), comp);
        return layoutManager.addLayoutComponent(comp);
    }

    public void remove(IComponent<P> comp) {
        children.remove(comp);
    }

    private void addComponent(int pos, IComponent<P> component) {
        children.add(pos, component);
        component.setParent(this); // components need to know their parent
    }

    // override this for subcomponents
	public Container<?> getParent() {
		return null;
	}

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [width=" + width
                + ", height=" + height + "]";
    }

}
