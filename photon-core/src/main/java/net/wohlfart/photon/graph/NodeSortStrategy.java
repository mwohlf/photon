package net.wohlfart.photon.graph;

import java.io.Serializable;
import java.util.Comparator;



// the final render order should be:
//
// - hud (solid)      \
// - solid           -- (front to back)
// - skybox           /
// - transparent/hud  - (back to front) depth writing off
//
public class NodeSortStrategy<T extends NodeSortStrategy.HasSortToken> implements Comparator<TreeImpl<T>>, Serializable {
    private static final long serialVersionUID = 1L;

    public static final ISortToken NULL_SORT_TOKEN = new ISortToken() {

        @Override
        public boolean isTranslucent() {
            return false;
        }

        @Override
        public double getZOrder() {
            return 0;
        }

        /*
        @Override
        public void setZOrder(double zOrder) {
            // do nothing
        }
        */

        @Override
        public String toString() {
            return "NULL_SORT_TOKEN";
        }

    };

    public interface HasSortToken  {

        ISortToken getSortToken();

    }

    public interface ISortToken {

        boolean isTranslucent();

        double getZOrder();

        // void setZOrder(double zOrder);

    }

    @Override
    public int compare(TreeImpl<T> left, TreeImpl<T> right) {
        return compare(left.value, right.value);
    }

    private int compare(HasSortToken left, HasSortToken right) {
        return compare(left.getSortToken(), right.getSortToken());
    }

    // neg : left < right
    // pos : left > right
    //  0  : left == right
    private int compare(ISortToken left, ISortToken right) {
        assert null != left;
        assert null != right;

        if (left.isTranslucent() ^ right.isTranslucent()) { // exactly one is transparent, we want to draw the solids first
            return left.isTranslucent()?+10:-10;
        }

        // both are transparent or not
        return Double.compare(left.getZOrder(), right.getZOrder()) * (left.isTranslucent()?-1:+1);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
