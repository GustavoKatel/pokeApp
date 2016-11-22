package ninja.pokeapp.pokeapp.business;

/**
 * Created by gustavokatel on 11/22/16.
 */

public interface BidirectionalIteratorInterface<T> {

    public boolean hasNext();
    public boolean hasPrev();

    public T next();
    public T prev();

}
