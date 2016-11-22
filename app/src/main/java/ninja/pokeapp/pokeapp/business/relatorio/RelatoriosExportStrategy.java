package ninja.pokeapp.pokeapp.business.relatorio;

import android.content.Context;

import ninja.pokeapp.pokeapp.model.relatorio.Relatorio;
import java.util.List;

/**
 * Created on 10/6/16.
 */
public interface RelatoriosExportStrategy {

    public boolean exportaRelatorios(Context context, List<Relatorio> relatorios) throws RelatorioExportException;

}
