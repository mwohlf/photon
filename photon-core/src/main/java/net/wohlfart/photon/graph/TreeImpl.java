package net.wohlfart.photon.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.annotation.CheckReturnValue;
import javax.annotation.meta.When;

import com.google.common.collect.UnmodifiableIterator;


/**
 * implementing a 2 way navigate-able tree where the tree data structure is
 * responsible for keeping the children/parent relationships not the elements in the tree
 *
 * this class is not thread-safe at all
 */
class TreeImpl<T> implements ITree<T> {

    protected final T value;

    protected final TreeImpl<T> parent;

    protected final List<TreeImpl<T>> children; // never null, size 0 for a leaf node

    // there is only one instance per tree in the root node,
    // each child has a link to the parent's removeValues
    private final Set<T> removeValues;


    // for testing only so far
    private final ChildIterator iterator = new ChildIterator();


    TreeImpl(T value) {
        assert value != null;
        this.parent = null;
        this.removeValues = new HashSet<T>();
        this.value = value;
        this.children = new ArrayList<TreeImpl<T>>();
    }

    TreeImpl(TreeImpl<T> parent, T value) {
        assert value != null;
        assert parent != null;
        this.parent = parent;
        this.removeValues = parent.removeValues;
        this.value = value;
        this.children = new ArrayList<TreeImpl<T>>();
    }

    TreeImpl(TreeImpl<T> parent, T value, List<TreeImpl<T>> children) {
        assert value != null;
        assert parent != null;
        assert children != null;
        this.parent = parent;
        this.removeValues = parent.removeValues;
        this.value = value;
        this.children = children;
    }

    @Override
    public Iterator<TreeImpl<T>> getChildren() {
        return iterator.reset();
    }

    @Override
    public T getValue() {
        return value;
    }

    protected TreeImpl<T> getParent() {
        return parent;
    }

    protected void add(T... values) {
        addAll(Arrays.asList(values));
    }

    protected void addAll(Collection<T> values) {
        for (T value : values) {
            add(value);
        }
    }

    // override this method to customize the add behavior
    @Override
    @CheckReturnValue(when=When.NEVER)
    public TreeImpl<T> add(T value) {
        TreeImpl<T> result = new TreeImpl<T>(this, value);
        children.add(result);
        return result;
    }

    public void removeAll(Collection<? extends T> values) {
        assert values != null;
        for (T value : values) {
            remove(value);
        }
    }

    @Override
    public void remove(T value) {
        removeValues.add(value);
    }

    // creates a new tree iterator instance from the root node
    protected Iterator<T> createPreOrderTreeIterator() {
        if (this.parent != null) {
            return this.parent.createPreOrderTreeIterator();
        }
        return new PreOrderTreeIterator<T>(this);
    }

    class ChildIterator extends UnmodifiableIterator<TreeImpl<T>> {
        private int index = 0;

        @Override
        public boolean hasNext() {
            while (index < children.size() && removeValues.contains(children.get(index).value)) {
                removeValues.remove(children.remove(index).value);
            }
            return index < children.size();
        }

        @Override
        public TreeImpl<T> next() {
            assert index < children.size();
            assert !removeValues.contains(children.get(index).value);
            return children.get(index++);
        }

        Iterator<TreeImpl<T>> reset() {
            index = 0;
            return this;
        }

    };


    // TODO: this iterator does not handle the removes!
    private class PreOrderTreeIterator<U> extends UnmodifiableIterator<U> {
        private final Deque<TreeImpl<U>> serializedTrees;

        private PreOrderTreeIterator(TreeImpl<U> tree) {
            this.serializedTrees = new ArrayDeque<TreeImpl<U>>();
            serializedTrees.addLast(tree);
        }

        @Override
        public boolean hasNext() {
            return !(serializedTrees.isEmpty());
        }

        @Override
        public U next() throws NoSuchElementException {
            TreeImpl<U> tree = serializedTrees.getLast(); // throws NSEE
            serializedTrees.removeLast();
            //if (tree.children != null) {
                int size = tree.children.size();
                for (int i = size -1; i >= 0; i--) {
                    serializedTrees.addLast(tree.children.get(i));
                }
            //}
            return tree.getValue();
        }
    }


}
