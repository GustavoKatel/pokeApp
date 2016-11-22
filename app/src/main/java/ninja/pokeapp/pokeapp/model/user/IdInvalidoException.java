package ninja.pokeapp.pokeapp.model.user;

/**
 * Created by gustavokatel on 10/17/16.
 */
public class IdInvalidoException extends Exception {

    public IdInvalidoException(String msg) {
        super(msg);
    }

    public IdInvalidoException() {
        super("ID de usuário inválido");
    }

}
