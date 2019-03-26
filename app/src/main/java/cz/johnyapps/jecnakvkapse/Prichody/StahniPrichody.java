package cz.johnyapps.jecnakvkapse.Prichody;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;

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
     * @param obdobi    Období které se má stáhnout
     */
    public void stahni(@Nullable String obdobi) {
        DialogLoading dialogLoading = new DialogLoading(context);
        AlertDialog dialog = dialogLoading.get("Downloading...");

        Request request = new Request("absence/passing-student", "GET");

        if (obdobi != null) {
            request.addObdobi(obdobi);
        }

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
