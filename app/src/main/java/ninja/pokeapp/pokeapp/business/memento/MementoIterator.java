package ninja.pokeapp.pokeapp.business.memento;

import java.util.ArrayList;
import java.util.Deque;

import ninja.pokeapp.pokeapp.business.BidirectionalIteratorInterface;

/**
 * Created by gustavokatel on 11/22/16.
 */

public class MementoIterator implements BidirectionalIteratorInterface<Memento> {

    private ArrayList<Memento> mementos;
    private int index;

    public MementoIterator(ArrayList<Memento> mementos) {
        this.mementos = mementos;
        this.index = mementos.size();
    }

    public void commit() {
        while(index < mementos.size() && index >= 0) {
            mementos.remove(index);
        }
    }

    @Override
    public boolean hasNext() {
        return mementos.size()>0 && index < mementos.size();
    }

    @Override
    public boolean hasPrev() {
        return mementos.size()>0 && index-1 >= 0;
    }

    @Override
    public Memento next() {
        return mementos.get(index++);
    }

    @Override
    public Memento prev() {
        return mementos.get(--index);
    }
}
