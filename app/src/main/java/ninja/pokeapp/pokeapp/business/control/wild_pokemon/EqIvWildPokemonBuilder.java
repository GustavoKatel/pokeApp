package ninja.pokeapp.pokeapp.business.control.wild_pokemon;

import java.util.Random;

import ninja.pokeapp.pokeapp.model.pokemon.Pokemon;

/**
 * Created by gustavokatel on 11/22/16.
 */

public class EqIvWildPokemonBuilder extends WildPokemonBuilder {

    private double iv;

    public EqIvWildPokemonBuilder(Pokemon basePokemon) {
        super(basePokemon);
        Random random = new Random(System.currentTimeMillis());
        iv = random.nextInt(99) / 100.0;
    }

    public double getIv() {
        return iv;
    }

    @Override
    public void buildHp() {
        pokemonCaptured.setHp(iv*basePokemon.getHp() + basePokemon.getHp());
    }

    @Override
    public void buildCp() {
        pokemonCaptured.setCp(iv*basePokemon.getCp() + basePokemon.getCp());
    }

    @Override
    public void buildEv() {
        pokemonCaptured.setEv(iv*basePokemon.getEv() + basePokemon.getEv());
    }
}
