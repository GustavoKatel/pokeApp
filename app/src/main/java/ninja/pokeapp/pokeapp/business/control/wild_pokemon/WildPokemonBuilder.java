package ninja.pokeapp.pokeapp.business.control.wild_pokemon;

import java.util.GregorianCalendar;

import ninja.pokeapp.pokeapp.model.pokemon.Pokemon;
import ninja.pokeapp.pokeapp.model.pokemon.PokemonCaptured;

/**
 * Created by gustavokatel on 11/22/16.
 */

public abstract class WildPokemonBuilder {

    protected Pokemon basePokemon;
    protected PokemonCaptured pokemonCaptured;

    public WildPokemonBuilder(Pokemon basePokemon) {
        this.basePokemon = basePokemon;

        this.pokemonCaptured = new PokemonCaptured(String.valueOf(System.currentTimeMillis()), basePokemon.getNome(), 0, 0, 0, (GregorianCalendar)GregorianCalendar.getInstance(), basePokemon);
    }

    public abstract void buildHp();
    public abstract void buildCp();
    public abstract void buildEv();

    public PokemonCaptured getWildPokemon() {
        return pokemonCaptured;
    }

}
