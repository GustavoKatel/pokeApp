package ninja.pokeapp.pokeapp.business.commands;

import ninja.pokeapp.pokeapp.business.control.usuario.UsuarioController;
import ninja.pokeapp.pokeapp.model.user.GooglePlayerInvalidoException;
import ninja.pokeapp.pokeapp.model.user.IdInvalidoException;
import ninja.pokeapp.pokeapp.model.user.NomeInvalidoException;
import ninja.pokeapp.pokeapp.model.user.UsuarioNaoExisteException;

/**
 * Created by gustavokatel on 10/28/16.
 */
public abstract class GenericUsuarioControllerCmd implements GenericCommand {

    protected UsuarioController controller;

    public GenericUsuarioControllerCmd(UsuarioController controller) {
        this.controller = controller;
    }

    public abstract Object execute(Object data) throws NomeInvalidoException, IdInvalidoException, UsuarioNaoExisteException, GooglePlayerInvalidoException;
}
