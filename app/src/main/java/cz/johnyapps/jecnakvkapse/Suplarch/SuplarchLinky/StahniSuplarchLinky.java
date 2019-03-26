package cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.content.Context;

import cz.johnyapps.jecnakvkapse.Dialogs.DialogLoading;
import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;

/**
 * Stahuje linky/odkazy na suplování
 * @see Connection
 */
public class StahniSuplarchLinky {
    private Context context;

    /**
     * Inicializace
     * @param context   Context
     */
    protected StahniSuplarchLinky(Context context) {
        this.context = context;
    }

    /**
     * Stáhne odkazy na suplování přes {@link Connection}
     */
    public void stahni() {
        DialogLoading dialogLoading = new DialogLoading(context);
        AlertDialog dialog = dialogLoading.get("Downloading...");

        Request request = new Request("", "POST");

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
     * @see SuplarchFindLink
     */
    public void onResult(String result) {

    }
}
