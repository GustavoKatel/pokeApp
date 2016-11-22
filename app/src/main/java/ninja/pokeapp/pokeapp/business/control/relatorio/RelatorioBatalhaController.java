package ninja.pokeapp.pokeapp.business.control.relatorio;

import android.content.Context;

import ninja.pokeapp.pokeapp.model.pokemon.PokemonCaptured;
import ninja.pokeapp.pokeapp.model.relatorio.Relatorio;
import ninja.pokeapp.pokeapp.model.relatorio.RelatorioBatalha;
import ninja.pokeapp.pokeapp.model.user.Usuario;
import ninja.pokeapp.pokeapp.business.relatorio.RelatorioExportException;
import ninja.pokeapp.pokeapp.business.relatorio.RelatoriosExportStrategy;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created on 10/6/16.
 */
public class RelatorioBatalhaController {

    private List<Relatorio> relatorios;
    private RelatoriosExportStrategy exportStrategy;

    private Context context;

    public RelatorioBatalhaController(Context context, RelatoriosExportStrategy exportStrategy) {
        relatorios = new ArrayList<>();
        this.exportStrategy = exportStrategy;
        this.context = context;
    }

    public RelatorioBatalha createRelatorio(GregorianCalendar date, Usuario ganhador, Usuario perdedor, PokemonCaptured pokemonGanhador, PokemonCaptured pokemonPerdedor)
    {
        RelatorioBatalha r = new RelatorioBatalha(date, ganhador, perdedor, pokemonGanhador, pokemonPerdedor);
        relatorios.add(r);
        return r;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void saveRelatorio(RelatorioBatalha relatorio)
    {
        int i;
        for(i=0;i<relatorios.size();i++) {
            if(relatorios.get(i).getId() == relatorio.getId()) {
                relatorios.set(i, relatorio);
                break;
            }
        }

        if(i==relatorios.size()) {
            relatorios.add(relatorio);
        }

    }

    public void deleteRelatorio(RelatorioBatalha relatorio)
    {
        int i;
        for(i=0;i<relatorios.size();i++) {
            if(relatorios.get(i).getId() == relatorio.getId()) {
                relatorios.remove(i);
                break;
            }
        }
    }

    public ArrayList<Relatorio> buscaRelatorioPorPokemon(PokemonCaptured pokemon) {
        ArrayList<Relatorio> res = new ArrayList<>();

        RelatorioBatalha reb;
        for(Relatorio relatorio : this.relatorios) {
            reb = (RelatorioBatalha) relatorio;
            if(reb.getPokemonGanhador().getId().equals(pokemon.getId()) ||
                    reb.getPokemonPerdedor().getId().equals(pokemon.getId())) {
                res.add(reb);
            }
        }

        return res;
    }

    public void export() throws RelatorioExportException {
        exportStrategy.exportaRelatorios(context, (List<Relatorio>) relatorios);
    }

    public RelatoriosExportStrategy getExportStrategy() {
        return exportStrategy;
    }

    public void setExportStrategy(RelatoriosExportStrategy exportStrategy) {
        this.exportStrategy = exportStrategy;
    }
}
