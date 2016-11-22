package ninja.pokeapp.pokeapp.model.pokemon;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.HashMap;

import ninja.pokeapp.pokeapp.business.memento.CreatesMemento;
import ninja.pokeapp.pokeapp.business.memento.Memento;

/**
 * Created on 10/6/16.
 */
public class Pokemon implements CreatesMemento, Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nome;
    private double hp;
    private double cp;
    private double ev;
    private Drawable img;

    public Pokemon(String id, String nome, double hp, double cp, double ev, Drawable img) {
        this.id = id;
        this.nome = nome;
        this.hp = hp;
        this.cp = cp;
        this.ev = ev;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getCp() {
        return cp;
    }

    public void setCp(double cp) {
        this.cp = cp;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public double getEv() {
        return ev;
    }

    public void setEv(double ev) {
        this.ev = ev;
    }

    @Override
    public String getMementoId() {
        return "pokemon-" + id;
    }

    @Override
    public Memento createMemento() {
        HashMap<String, String> data = new HashMap<>();
        data.put("nome", nome);
        data.put("hp", String.valueOf(hp));
        data.put("cp", String.valueOf(cp));
        data.put("ev", String.valueOf(ev));
        return new Memento(data);
    }

    @Override
    public void setMemento(Memento memento) {

        HashMap<String, String> data = (HashMap<String, String>) memento.getData();

        this.nome = data.get("nome");
        this.hp = Double.parseDouble(data.get("hp"));
        this.cp = Double.parseDouble(data.get("cp"));
        this.ev = Double.parseDouble(data.get("ev"));

    }
}
