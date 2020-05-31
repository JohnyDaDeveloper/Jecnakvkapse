package cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky;

import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;
import cz.johnyapps.jecnakvkapse.HttpConnection.StahniData;

/**
 * Stahuje linky/odkazy na suplování
 * @see Connection
 */
public class StahniSuplarchLinky extends StahniData {
    /**
     * Inicializace
     */
    public StahniSuplarchLinky() {

    }

    /**
     * Stáhne odkazy na suplování přes {@link Connection}
     */
    public void stahni() {
        Request request = new Request("", "POST");

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
