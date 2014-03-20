package net.wohlfart.photon.hud.layout;

import java.util.Map;

import net.wohlfart.photon.tools.Position;


/**
 * does all the calculations for the layouting
 */
public class CornerConstraints {
    protected LayoutStrategy<CornerConstraints> layoutManager;
    protected Alignment alignment = Alignment.TOP_LEFT;
    protected int top, left, bottom, right;  // the borders
    
    protected CornerConstraints(LayoutStrategy<CornerConstraints> layoutManager) {
        this.layoutManager = layoutManager;
    }

    public CornerConstraints alignToTopLeft() {
        setAlignment(Alignment.TOP_LEFT);
        return this;
    }
    
    public CornerConstraints alignToTopRight() {
        setAlignment(Alignment.TOP_RIGHT);
        return this;
    }
    
    public CornerConstraints alignToTopMiddle() {
        setAlignment(Alignment.TOP_MIDDLE);
        return this;
    }

    public CornerConstraints alignToBottomLeft() {
        setAlignment(Alignment.BOTTOM_LEFT);
        return this;
    }
    
    public CornerConstraints alignToBottomRight() {
        setAlignment(Alignment.BOTTOM_RIGHT);
        return this;
    }
    
    public CornerConstraints alignToBottomMiddle() {
        setAlignment(Alignment.BOTTOM_MIDDLE);
        return this;
    }
      
    public CornerConstraints withRightBorder(int pixel) {
        setBorder(top, left, bottom, pixel);
        return this;
    }
    
    public CornerConstraints withBorder(int pixel) {
        setBorder(pixel, pixel, pixel, pixel);
        return this;
    }
     
    public CornerConstraints add(IComponent<CornerConstraints> comp) {
        return layoutManager.addLayoutComponent(comp);
    }
   
    public LayoutStrategy<CornerConstraints> getLayoutManager() {
        return layoutManager;
    }
    
    private void setBorder(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
        layoutManager.setDirty();
    }
    
    private void setAlignment(Alignment align) {
        alignment = align;
        layoutManager.setDirty();
    }

    enum Alignment { 
        TOP_LEFT {
            @Override
            Position stackUp(Map<Alignment, Position> lastPosition, CornerConstraints constr, IComponent<CornerConstraints> comp) {
                Container<CornerConstraints> container = comp.getParent();
                Position p = lastPosition.get(this);
                if (p == null) {
                    p = new Position(0,0);
                    lastPosition.put(this, p);
                }
                Position value = new Position(
                        (float) (p.getX() + constr.left) / (float)container.getWidth(), 
                        (float) (p.getY() + constr.top) / (float)container.getHeight());
                p.setY(p.getY() + constr.top + comp.getHeight() + constr.bottom);
                return value;
            }               
        },
        TOP_MIDDLE {
            @Override
            Position stackUp(Map<Alignment, Position> lastPosition, CornerConstraints constr, IComponent<CornerConstraints> comp) {
                Container<CornerConstraints> container = comp.getParent();
                Position p = lastPosition.get(this);
                if (p == null) {
                    p = new Position(container.getWidth()/2f, 0);
                    lastPosition.put(this, p);
                }
                Position value = new Position(
                        (float) (p.getX() - ((constr.left + constr.right + comp.getWidth()) / 2f) + constr.left) / (float)container.getWidth(), 
                        (float) (p.getY() + constr.top) / (float)container.getHeight());
                p.setY(p.getY() + constr.top + comp.getHeight() + constr.bottom);
                return value;
            }               
        },
        TOP_RIGHT {
            @Override
            Position stackUp(Map<Alignment, Position> lastPosition, CornerConstraints constr, IComponent<CornerConstraints> comp) {
                Container<CornerConstraints> container = comp.getParent();
                Position p = lastPosition.get(this);
                if (p == null) {
                    p = new Position(container.getWidth(),0);
                    lastPosition.put(this, p);
                }
                Position value = new Position(
                        (float) (p.getX() - (constr.right + constr.left + comp.getWidth())) / (float)container.getWidth(), 
                        (float) (p.getY() + constr.top) / (float)container.getHeight());
                p.setY(p.getY() + constr.top + comp.getHeight() + constr.bottom);
                return value;
            }   
        },
        BOTTOM_LEFT {
            @Override
            Position stackUp(Map<Alignment, Position> lastPosition, CornerConstraints constr, IComponent<CornerConstraints> comp) {
                Container<CornerConstraints> container = comp.getParent();
                Position p = lastPosition.get(this);
                if (p == null) {
                    p = new Position(0, container.getHeight());
                    lastPosition.put(this, p);
                }
                Position value = new Position(
                        (float) (p.getX() + constr.left) / (float)container.getWidth(), 
                        (float) (p.getY() - (constr.top + constr.bottom + comp.getHeight())) / (float)container.getHeight());
                p.setY(p.getY() - (constr.top + comp.getHeight() + constr.bottom));
                return value;
            }               
        },
        BOTTOM_MIDDLE {
            @Override
            Position stackUp(Map<Alignment, Position> lastPosition, CornerConstraints constr, IComponent<CornerConstraints> comp) {
                Container<CornerConstraints> container = comp.getParent();
                Position p = lastPosition.get(this);
                if (p == null) {
                    p = new Position(container.getWidth()/2f, container.getHeight());
                    lastPosition.put(this, p);
                }
                Position value = new Position(
                        (float) (p.getX() - ((constr.left + constr.right + comp.getWidth()) / 2f) + constr.left) / (float)container.getWidth(), 
                        (float) (p.getY() - (constr.top + constr.bottom + comp.getHeight())) / (float)container.getHeight());
                p.setY(p.getY() - (constr.top + comp.getHeight() + constr.bottom));
                return value;
            }               
        },
        BOTTOM_RIGHT {
            @Override
            Position stackUp(Map<Alignment, Position> lastPosition, CornerConstraints constr, IComponent<CornerConstraints> comp) {
                Container<CornerConstraints> container = comp.getParent();
                Position p = lastPosition.get(this);
                if (p == null) {
                    p = new Position(container.getWidth(), container.getHeight());
                    lastPosition.put(this, p);
                }
                Position value = new Position(
                        (float) (p.getX() - (constr.right + constr.left + comp.getWidth())) / (float)container.getWidth(), 
                        (float) (p.getY() - (constr.top + constr.bottom + comp.getHeight())) / (float)container.getHeight());
                p.setY(p.getY() - (constr.top + comp.getHeight() + constr.bottom));
                return value;
            }   
        };
               
        abstract Position stackUp(Map<Alignment, Position> lastPosition, CornerConstraints constrains, IComponent<CornerConstraints> component);
        
    }
}