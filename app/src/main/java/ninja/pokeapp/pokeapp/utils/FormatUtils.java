package ninja.pokeapp.pokeapp.utils;

/**
 * Created by gustavokatel on 11/21/16.
 */

public class FormatUtils {

    public static String formatPokemonStats(double iv, double base) {
        String mask = "%.0f (%.0f + %.0f)";
        return String.format(mask, iv+base, iv, base);
    }

}
