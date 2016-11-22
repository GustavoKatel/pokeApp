package ninja.pokeapp.pokeapp.business.memento;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;

/**
 * Created by gustavokatel on 10/28/16.
 */
public class Caretaker {

    private HashMap<String, ArrayList<Memento>> stateMap;

    public Caretaker() {

        stateMap = new HashMap<>();

    }

    public void saveMemento(String id, Memento memento) {

        ArrayList<Memento> states;
        if(!stateMap.containsKey(id)) {
            states = new ArrayList<>();
            stateMap.put(id, states);
        } else {
            states = stateMap.get(id);
        }

        states.add(memento);

    }

    public MementoIterator getIterator(String id) {
        if(stateMap.containsKey(id)) {
            return new MementoIterator(stateMap.get(id));
        }
        return new MementoIterator(new ArrayList<>());
    }

    public void clear(String id) {
        if(stateMap.containsKey(id)) {
            stateMap.get(id).clear();
        }
    }


}
