package cz.johnyapps.jecnakvkapse.Prichody;

import androidx.annotation.Nullable;

import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;
import cz.johnyapps.jecnakvkapse.HttpConnection.StahniData;

/**
 * Stáhne příchody
 * @see Connection
 */
public class StahniPrichody extends StahniData {
    /**
     * Inicializace
     */
    public StahniPrichody() {

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
