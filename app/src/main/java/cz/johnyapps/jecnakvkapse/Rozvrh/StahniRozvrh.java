package cz.johnyapps.jecnakvkapse.Rozvrh;

import android.annotation.SuppressLint;

import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;

/**
 * Stáhne rozvrh
 * @see Connection
 */
public class StahniRozvrh {
    protected StahniRozvrh() {

    }

    /**
     * Stáhne rozvrh přes {@link Connection}
     */
    public void stahni() {
        Request request = new Request("timetable/class", "POST");

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
     * @see RozvrhConventor
     */
    public void onResult(String result) {

    }
}
