package cz.johnyapps.jecnakvkapse.Prichody;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;

import cz.johnyapps.jecnakvkapse.Dialogs.DialogLoading;
import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;

/**
 * Stáhne příchody
 * @see Connection
 */
public class StahniPrichody {
    private Context context;

    /**
     * Inicializace
     * @param context   Context
     */
    protected StahniPrichody(Context context) {
        this.context = context;
    }

    /**
     * Stáhne omluvenky přes {@link Connection}
     */
    public void stahni() {
        DialogLoading dialogLoading = new DialogLoading(context);
        AlertDialog dialog = dialogLoading.get("Downloading...");

        Request request = new Request("absence/passing-student", "GET");

        @SuppressLint("StaticFieldLeak") Connection connection = new Connection(dialog) {
            @Override
            public void nextAction(String result) {
                super.nextAction(result);
                onResult(result);
            }
        };

        connection.execute(request);
    }

    /**
     * Spustí se při dokončení stahování
     * @param result    Výsledek
     * @see PrichodyConvertor
     */
    public void onResult(String result) {

    }
}
