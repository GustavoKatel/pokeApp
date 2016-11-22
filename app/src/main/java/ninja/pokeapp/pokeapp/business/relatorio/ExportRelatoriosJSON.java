package ninja.pokeapp.pokeapp.business.relatorio;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import ninja.pokeapp.pokeapp.model.relatorio.Relatorio;
import ninja.pokeapp.pokeapp.services.RelatorioService;

import java.util.List;

/**
 * Created on 10/10/16.
 */
public class ExportRelatoriosJSON implements RelatoriosExportStrategy {

    private String filename;

    public ExportRelatoriosJSON(String filename) {
        this.filename = filename;
    }

    public boolean exportaRelatorios(Context context, List<Relatorio> relatorios) throws RelatorioExportException {

        if(relatorios == null) {
            throw new RelatorioExportException("Não foi possível exportar relatório");
        }

        if(filename == null || filename.isEmpty()) {
            throw new RelatorioExportException("Arquivo com nome inválido");
        }

        JSONArray jsonArray = new JSONArray();

        for( Relatorio re : relatorios ) {
            try {
                jsonArray.put(re.toJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        RelatorioService.startSaveRelatorio(context, jsonArray.toString());

        return true;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
