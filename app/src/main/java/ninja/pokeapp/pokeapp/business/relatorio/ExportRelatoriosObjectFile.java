package ninja.pokeapp.pokeapp.business.relatorio;

import android.content.Context;

import ninja.pokeapp.pokeapp.model.relatorio.Relatorio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created on 10/10/16.
 */
public class ExportRelatoriosObjectFile implements RelatoriosExportStrategy {

    private String filename;

    public ExportRelatoriosObjectFile(String filename) {
        this.filename = filename;
    }

    public boolean exportaRelatorios(Context context, List<Relatorio> relatorios) throws RelatorioExportException {

        if(relatorios == null) {
            throw new RelatorioExportException("Não foi possível exportar relatório");
        }

        if(filename == null || filename.isEmpty()) {
            throw new RelatorioExportException("Arquivo com nome inválido");
        }

        // TODO: export binary file

        return true;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
