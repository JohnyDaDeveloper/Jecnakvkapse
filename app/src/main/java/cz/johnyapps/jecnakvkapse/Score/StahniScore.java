package cz.johnyapps.jecnakvkapse.Score;

import android.annotation.SuppressLint;
import androidx.annotation.Nullable;

import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;

/**
 * Stáhne rozvrh
 * @see Connection
 */
public class StahniScore {
    /**
     * Inicializace
     */
    protected StahniScore() {

    }

    /**
     * Stáhne známky přes {@link Connection}
     * @param obdobi    Období pro které se stáhnou data
     */
    public void stahni(@Nullable String obdobi) {

        Request request = new Request("score/student", "POST");

        if (obdobi != null) {
            request.addObdobi(obdobi);
        }

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
     * @see ScoreConvertor
     */
    public void onResult(String result) {

    }
}