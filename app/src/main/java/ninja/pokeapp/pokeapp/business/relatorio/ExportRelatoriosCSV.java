package ninja.pokeapp.pokeapp.business.relatorio;

import android.content.Context;

import java.util.List;

import ninja.pokeapp.pokeapp.model.relatorio.Relatorio;

/**
 * Created on 10/10/16.
 */
public class ExportRelatoriosCSV implements RelatoriosExportStrategy {

    private String filename;

    public ExportRelatoriosCSV(String filename) {
        this.filename = filename;
    }

    public boolean exportaRelatorios(Context context, List<Relatorio> relatorios) throws RelatorioExportException {

        if(relatorios == null) {
            throw new RelatorioExportException("Não foi possível exportar relatório");
        }

        if(filename == null || filename.isEmpty()) {
            throw new RelatorioExportException("Arquivo com nome inválido");
        }

        // TODO: export csv

        return true;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
