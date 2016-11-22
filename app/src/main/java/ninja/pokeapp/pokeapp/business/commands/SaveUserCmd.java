package ninja.pokeapp.pokeapp.business.commands;

import ninja.pokeapp.pokeapp.business.control.usuario.UsuarioController;
import ninja.pokeapp.pokeapp.model.user.IdInvalidoException;
import ninja.pokeapp.pokeapp.model.user.NomeInvalidoException;
import ninja.pokeapp.pokeapp.model.user.Usuario;

/**
 * Created by gustavokatel on 10/28/16.
 */
public class SaveUserCmd extends GenericUsuarioControllerCmd {

    public SaveUserCmd(UsuarioController controller) {
        super(controller);
    }

    @Override
    public Object execute(Object obj) throws NomeInvalidoException, IdInvalidoException {
        Usuario user = (Usuario) obj;

        this.controller.saveUsuario(user);

        return user;
    }
}
