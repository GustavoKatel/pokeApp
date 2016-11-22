package ninja.pokeapp.pokeapp.business.commands;

/**
 * Created by gustavokatel on 10/28/16.
 */
public class CommandInvoker {

    public CommandInvoker() {

    }

    public Object invoke(GenericCommand cmd, Object data) throws Exception {

        return cmd.execute(data);

    }

}
