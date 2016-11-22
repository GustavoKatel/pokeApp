package ninja.pokeapp.pokeapp.business.memento;

/**
 * Created by gustavokatel on 10/28/16.
 */
public interface CreatesMemento {

    public String getMementoId();

    public Memento createMemento();

    public void setMemento(Memento memento);

}
