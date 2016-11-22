package ninja.pokeapp.pokeapp.model.relatorio;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

import ninja.pokeapp.pokeapp.model.SerializableJson;

/**
 * Created on 10/6/16.
 */
public abstract class Relatorio implements Serializable, SerializableJson {

    private static long nextId = 1;

    private GregorianCalendar date;
    private long id;

    public Relatorio(GregorianCalendar date) {
        this.date = date;
        this.id = nextId++;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    protected String getPreMensagem() {
        return date.toString() + " - ";
    }

    @Override
    public abstract String toString();

    public abstract JSONObject toJson() throws JSONException;
}
