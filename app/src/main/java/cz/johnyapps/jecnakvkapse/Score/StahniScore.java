package cz.johnyapps.jecnakvkapse.Score;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;

import cz.johnyapps.jecnakvkapse.Dialogs.DialogLoading;
import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;

/**
 * Stáhne rozvrh
 * @see Connection
 */
public class StahniScore {
    private Context context;

    /**
     * Inicializace
     * @param context   Context
     */
    protected StahniScore(Context context) {
        this.context = context;
    }

    /**
     * Stáhne známky přes {@link Connection}
     * @param obdobi    Období pro které se stáhnou data
     */
    public void stahni(@Nullable String obdobi) {
        DialogLoading dialogLoading = new DialogLoading(context);
        AlertDialog dialog = dialogLoading.get("Downloading...");

        Request request = new Request("score/student", "POST");

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
     * @see MarkConvertor
     */
    public void onResult(String result) {

    }
}