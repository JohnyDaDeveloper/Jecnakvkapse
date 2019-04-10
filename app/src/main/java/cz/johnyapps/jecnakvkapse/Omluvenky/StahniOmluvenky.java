package cz.johnyapps.jecnakvkapse.Omluvenky;

import android.annotation.SuppressLint;

import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;

/**
 * Stáhne omluvenky
 * @see Connection
 */
public class StahniOmluvenky {
    /**
     * Inicializace
     */
    protected StahniOmluvenky() {

    }

    /**
     * Stáhne omluvenky přes {@link Connection}
     */
    public void stahni() {
        Request request = new Request("absence/student", "POST");

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
     * @see OmluvenkyConvertor
     */
    public void onResult(String result) {

    }
}
