package cz.johnyapps.jecnakvkapse.Rozvrh;

import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;
import cz.johnyapps.jecnakvkapse.HttpConnection.StahniData;

/**
 * Stáhne rozvrh
 * @see Connection
 */
public class StahniRozvrh extends StahniData {
    public StahniRozvrh() {

    }

    /**
     * Stáhne rozvrh přes {@link Connection}
     */
    public void stahni() {
        Request request = new Request("timetable/class", "POST");

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
