package cz.johnyapps.jecnakvkapse.Prichody;

import android.annotation.SuppressLint;
import androidx.annotation.Nullable;

import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;

/**
 * Stáhne příchody
 * @see Connection
 */
public class StahniPrichody {
    /**
     * Inicializace
     */
    protected StahniPrichody() {

    }

    /**
     * Stáhne omluvenky přes {@link Connection}
     * @param obdobi    Období které se má stáhnout
     */
    public void stahni(@Nullable String obdobi) {
        Request request = new Request("absence/passing-student", "GET");

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
     * @see PrichodyConvertor
     */
    public void onResult(String result) {

    }
}
