package cz.johnyapps.jecnakvkapse.Omluvenky;

import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;
import cz.johnyapps.jecnakvkapse.HttpConnection.StahniData;

/**
 * Stáhne omluvenky
 * @see Connection
 */
public class StahniOmluvenky extends StahniData {
    /**
     * Inicializace
     */
    public StahniOmluvenky() {

    }

    /**
     * Stáhne omluvenky přes {@link Connection}
     */
    public void stahni() {
        Request request = new Request("absence/student", "POST");

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
