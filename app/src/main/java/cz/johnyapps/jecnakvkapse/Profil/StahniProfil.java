package cz.johnyapps.jecnakvkapse.Profil;

import android.annotation.SuppressLint;

import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;
import cz.johnyapps.jecnakvkapse.Singletons.User;

/**
 * Slouží ke stahování profilu
 */
public class StahniProfil {
    private User user;

    /**
     * Inicializace
     */
    protected StahniProfil() {
        user = User.getUser();
    }

    /**
     * Stáhne profil přes {@link Connection}
     */
    public void stahni() {
        Request request = new Request("student/" + user.getLogin(), "POST");

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
     * @see ProfilConvertor
     */
    public void onResult(String result) {

    }
}
