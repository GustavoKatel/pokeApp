package ninja.pokeapp.pokeapp.model.relatorio;

import org.json.JSONException;
import org.json.JSONObject;

import ninja.pokeapp.pokeapp.model.pokemon.Pokemon;
import ninja.pokeapp.pokeapp.model.user.Usuario;

import java.util.GregorianCalendar;

/**
 * Created on 10/6/16.
 */
public class RelatorioLogin extends Relatorio {

    private Usuario usuario;

    public RelatorioLogin(GregorianCalendar date, Usuario usuario) {
        super(date);
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return this.getPreMensagem() + "Novo jogador cadastrado: " + usuario.getId();
    }

    public JSONObject toJson() throws JSONException {
        return new JSONObject("{}");
    }

}
