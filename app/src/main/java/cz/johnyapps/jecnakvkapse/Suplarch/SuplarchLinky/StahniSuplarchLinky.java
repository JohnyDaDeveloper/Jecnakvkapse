package cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky;

import android.annotation.SuppressLint;

import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;

/**
 * Stahuje linky/odkazy na suplování
 * @see Connection
 */
public class StahniSuplarchLinky {
    /**
     * Inicializace
     */
    protected StahniSuplarchLinky() {

    }

    /**
     * Stáhne odkazy na suplování přes {@link Connection}
     */
    public void stahni() {
        Request request = new Request("", "POST");

        @SuppressLint("StaticFieldLeak") Connection connection = new Connection() {
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
