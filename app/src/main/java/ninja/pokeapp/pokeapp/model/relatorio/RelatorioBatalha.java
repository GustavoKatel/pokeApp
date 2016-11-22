package ninja.pokeapp.pokeapp.model.relatorio;

import org.json.JSONException;
import org.json.JSONObject;

import ninja.pokeapp.pokeapp.model.pokemon.PokemonCaptured;
import ninja.pokeapp.pokeapp.model.user.Usuario;

import java.util.GregorianCalendar;

/**
 * Created on 10/6/16.
 */
public class RelatorioBatalha extends Relatorio {

    private Usuario ganhador;
    private Usuario perdedor;

    private PokemonCaptured pokemonGanhador;
    private PokemonCaptured pokemonPerdedor;

    public RelatorioBatalha(GregorianCalendar date, Usuario ganhador, Usuario perdedor, PokemonCaptured pokemonGanhador, PokemonCaptured pokemonPerdedor) {
        super(date);
        this.ganhador = ganhador;
        this.perdedor = perdedor;
        this.pokemonGanhador = pokemonGanhador;
        this.pokemonPerdedor = pokemonPerdedor;
    }

    public Usuario getGanhador() {
        return ganhador;
    }

    public void setGanhador(Usuario ganhador) {
        this.ganhador = ganhador;
    }

    public Usuario getPerdedor() {
        return perdedor;
    }

    public void setPerdedor(Usuario perdedor) {
        this.perdedor = perdedor;
    }

    public PokemonCaptured getPokemonGanhador() {
        return pokemonGanhador;
    }

    public void setPokemonGanhador(PokemonCaptured pokemonGanhador) {
        this.pokemonGanhador = pokemonGanhador;
    }

    public PokemonCaptured getPokemonPerdedor() {
        return pokemonPerdedor;
    }

    public void setPokemonPerdedor(PokemonCaptured pokemonPerdedor) {
        this.pokemonPerdedor = pokemonPerdedor;
    }

    @Override
    public String toString() {
        return this.getPreMensagem() + "Jogador " + ganhador.getId() + " ganhou do jogador " + perdedor.getId();
    }

    public JSONObject toJson() throws JSONException {
        return new JSONObject("{}");
    }

}
