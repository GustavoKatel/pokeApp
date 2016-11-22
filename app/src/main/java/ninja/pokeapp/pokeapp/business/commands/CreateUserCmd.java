package ninja.pokeapp.pokeapp.business.commands;

import com.google.android.gms.games.Player;

import ninja.pokeapp.pokeapp.business.control.usuario.UsuarioController;
import ninja.pokeapp.pokeapp.model.user.GooglePlayerInvalidoException;
import ninja.pokeapp.pokeapp.model.user.IdInvalidoException;
import ninja.pokeapp.pokeapp.model.user.NomeInvalidoException;
import ninja.pokeapp.pokeapp.model.user.Usuario;

/**
 * Created by gustavokatel on 10/28/16.
 */
public class CreateUserCmd extends GenericUsuarioControllerCmd {

    public CreateUserCmd(UsuarioController controller) {
        super(controller);
    }

    @Override
    public Object execute(Object obj) throws NomeInvalidoException, IdInvalidoException, GooglePlayerInvalidoException {
        Player player = (Player) obj;

        return this.controller.createUsuario(player);
    }
}
