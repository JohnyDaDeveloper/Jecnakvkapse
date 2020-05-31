package cz.johnyapps.jecnakvkapse.Profil;

import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;
import cz.johnyapps.jecnakvkapse.HttpConnection.StahniData;
import cz.johnyapps.jecnakvkapse.Singletons.User;

/**
 * Slouží ke stahování profilu
 */
public class StahniProfil extends StahniData {
    private User user;

    /**
     * Inicializace
     */
    public StahniProfil() {
        user = User.getUser();
    }

    /**
     * Stáhne profil přes {@link Connection}
     */
    public void stahni() {
        Request request = new Request("student/" + user.getLogin(), "POST");

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
