package ninja.pokeapp.pokeapp.model.pokemon;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.io.Serializable;
import java.util.GregorianCalendar;

import ninja.pokeapp.pokeapp.business.memento.CreatesMemento;
import ninja.pokeapp.pokeapp.business.memento.Memento;

/**
 * Created by gustavokatel on 11/20/16.
 */

public class PokemonCaptured implements CreatesMemento, Serializable {


    private static final long serialVersionUID = 1L;


    private String nome;
    private double hp;
    private double cp;
    private double ev;
    private String id;
    private GregorianCalendar capturedDate;

    private Pokemon basePokemon;

    public PokemonCaptured(String id, String nome, double hp, double cp, double ev, GregorianCalendar capturedDate, Pokemon basePokemon) {
        this.id = id;
        this.nome = nome;
        this.hp = hp;
        this.cp = cp;
        this.ev = ev;
        this.capturedDate = capturedDate;
        this.basePokemon = basePokemon;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public void setCp(double cp) {
        this.cp = cp;
    }

    public void setEv(double ev) {
        this.ev = ev;
    }

    public String getNome() {
        return nome;
    }

    public double getHp() {
        return hp;
    }

    public double getCp() {
        return cp;
    }

    public double getEv() {
        return ev;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GregorianCalendar getCapturedDate() {
        return capturedDate;
    }

    public void setCapturedDate(GregorianCalendar capturedDate) {
        this.capturedDate = capturedDate;
    }

    public String getBasePokemonId() {
        return basePokemon.getId();
    }

    public Pokemon getBasePokemon() {
        return basePokemon;
    }

    @Override
    public String getMementoId() {
        return "pokemoncaptured-"+this.id;
    }

    @Override
    public Memento createMemento() {
        Bundle bundle = new Bundle();
        bundle.putString("name", this.nome);
        return new Memento(bundle);
    }

    @Override
    public void setMemento(Memento memento) {
        if(memento == null || memento.getData() == null) return;
        Bundle bundle = (Bundle) memento.getData();
        this.nome = bundle.getString("name");
    }
}
