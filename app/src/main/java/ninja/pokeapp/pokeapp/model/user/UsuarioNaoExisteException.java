package ninja.pokeapp.pokeapp.model.user;

/**
 * Created by gustavokatel on 10/17/16.
 */
public class UsuarioNaoExisteException extends Exception {

    public UsuarioNaoExisteException(String msg) {
        super(msg);
    }

    public UsuarioNaoExisteException() {
        super("Usuário não existente");
    }

}
