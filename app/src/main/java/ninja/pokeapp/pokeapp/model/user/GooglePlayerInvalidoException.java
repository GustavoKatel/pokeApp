package ninja.pokeapp.pokeapp.model.user;

/**
 * Created by gustavokatel on 10/17/16.
 */
public class GooglePlayerInvalidoException extends Exception {

    public GooglePlayerInvalidoException(String msg) {
        super(msg);
    }

    public GooglePlayerInvalidoException() {
        super("Google Play Services Id inválido");
    }

}
