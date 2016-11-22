package ninja.pokeapp.pokeapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import ninja.pokeapp.pokeapp.model.SerializableJson;
import ninja.pokeapp.pokeapp.model.relatorio.Relatorio;

public class RelatorioService extends IntentService {
    private static final String ACTION_SAVE = "ninja.pokeapp.pokeapp.services.relatorio.action.save";

    private static final String RELATORIO_DATA = "ninja.pokeapp.pokeapp.services.relatorio.extra.data";

    public RelatorioService() {
        super("RelatorioService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startSaveRelatorio(Context context, String data) {
        Intent intent = new Intent(context, RelatorioService.class);
        intent.setAction(ACTION_SAVE);
        intent.putExtra(RELATORIO_DATA, data);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SAVE.equals(action)) {
                final String data = intent.getStringExtra(RELATORIO_DATA);
                handleActionSave(data);
            }
        }
    }

    /**
     * Handle action Save relatorio in the provided background thread with the provided
     * parameters. This basically post the relatorio to the webservice
     */
    private void handleActionSave(String data) {
        // TODO: post to webservice
    }

}
