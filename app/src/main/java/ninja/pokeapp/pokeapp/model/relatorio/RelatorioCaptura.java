package ninja.pokeapp.pokeapp.model.relatorio;

import org.json.JSONException;
import org.json.JSONObject;

import ninja.pokeapp.pokeapp.model.pokemon.Pokemon;
import ninja.pokeapp.pokeapp.model.user.Usuario;

import java.util.GregorianCalendar;

/**
 * Created on 10/6/16.
 */
public class RelatorioCaptura extends Relatorio {

    private Usuario usuario;
    private Pokemon pokemon;

    public RelatorioCaptura(GregorianCalendar date, Usuario usuario, Pokemon pokemon) {
        super(date);
        this.usuario = usuario;
        this.pokemon = pokemon;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return this.getPreMensagem() + "Jogador " + usuario.getId() + " capturou pokemon " + pokemon.getId();
    }

    public JSONObject toJson() throws JSONException {
        return new JSONObject("{}");
    }

}
