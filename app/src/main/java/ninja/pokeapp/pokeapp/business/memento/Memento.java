package ninja.pokeapp.pokeapp.business.memento;

/**
 * Created by gustavokatel on 10/28/16.
 */
public class Memento {

    private Object data;

    public Memento(Object data) {
        this.data = data;
    }

    public Memento() {
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
