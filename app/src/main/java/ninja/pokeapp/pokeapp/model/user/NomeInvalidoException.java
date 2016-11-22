package ninja.pokeapp.pokeapp.model.user;

/**
 * Created by gustavokatel on 10/17/16.
 */
public class NomeInvalidoException extends Exception {

    public NomeInvalidoException(String msg) {
        super(msg);
    }

    public NomeInvalidoException() {
        super("Nome de usuário inválido");
    }

}
