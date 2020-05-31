package cz.johnyapps.jecnakvkapse.Score;

import androidx.annotation.Nullable;

import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;
import cz.johnyapps.jecnakvkapse.HttpConnection.StahniData;

/**
 * Stáhne rozvrh
 * @see Connection
 */
public class StahniScore extends StahniData {
    /**
     * Inicializace
     */
    public StahniScore() {

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

        Connection connection = new Connection();
        connection.setOnCompleteListener(new Connection.OnCompleteListener() {
            @Override
            public void onComplete(String result) {
                completed(result);
            }
        });
        connection.execute(request);
    }
}