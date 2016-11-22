package ninja.pokeapp.pokeapp.business.commands;

import ninja.pokeapp.pokeapp.business.control.usuario.UsuarioController;
import ninja.pokeapp.pokeapp.model.user.IdInvalidoException;
import ninja.pokeapp.pokeapp.model.user.NomeInvalidoException;
import ninja.pokeapp.pokeapp.model.user.UsuarioNaoExisteException;

/**
 * Created by gustavokatel on 10/28/16.
 */
public class DeleteUserCmd extends GenericUsuarioControllerCmd {

    public DeleteUserCmd(UsuarioController controller) {
        super(controller);
    }

    @Override
    public Object execute(Object obj) throws NomeInvalidoException, IdInvalidoException, UsuarioNaoExisteException {
        String id = (String) obj;

        return this.controller.deleteUsuario(id);
    }
}
