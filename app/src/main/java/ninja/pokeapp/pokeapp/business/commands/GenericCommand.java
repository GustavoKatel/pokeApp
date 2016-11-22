package ninja.pokeapp.pokeapp.business.commands;

import ninja.pokeapp.pokeapp.model.user.IdInvalidoException;
import ninja.pokeapp.pokeapp.model.user.NomeInvalidoException;
import ninja.pokeapp.pokeapp.model.user.Usuario;

/**
 * Created by gustavokatel on 10/28/16.
 */
public interface GenericCommand {

    public Object execute(Object data) throws Exception;

}
