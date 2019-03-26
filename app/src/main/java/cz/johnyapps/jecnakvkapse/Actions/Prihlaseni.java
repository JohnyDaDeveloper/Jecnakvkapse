package cz.johnyapps.jecnakvkapse.Actions;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.content.Context;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.Dialogs.DialogLoading;
import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Data;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.Profil.ProfilConvertor;
import cz.johnyapps.jecnakvkapse.Profil.StahniProfil;
import cz.johnyapps.jecnakvkapse.Singletons.User;

/**
 * Slouží k obstarání sessionID
 */
public class Prihlaseni {
    private Context context;
    private User user;

    /**
     * Inicializace
     * @param context   Context
     */
    protected Prihlaseni(Context context) {
        this.context = context;
        user = User.getUser();
    }

    /**
     * Kontaktuje server (O sessionID se stará {@link User})
     * @param user  String s loginem uživatele
     * @param pass  String s heslem uživatele
     * @see User
     */
    public void prihlas(String user, String pass) {
        DialogLoading dialogLoading = new DialogLoading(context);
        AlertDialog dialog = dialogLoading.get("Přihlašování...");

        ArrayList<Data> data = new ArrayList<>();
        data.add(new Data("user", user));
        data.add(new Data("pass", pass));

        this.user.setLogin(user);

        Request request = new Request("user/login", "POST");
        request.putData(data);

        @SuppressLint("StaticFieldLeak") Connection connection = new Connection(dialog) {
            @Override
            public void nextAction(String result) {
                super.nextAction(result);

                User.getUser().setLogged(true);

                stahniProfil();
            }
        };

        connection.execute(request);
    }

    /**
     * Stáhne údaje z profilu
     */
    private void stahniProfil() {
        StahniProfil stahniProfil = new StahniProfil() {
            @Override
            public void onResult(String result) {
                super.onResult(result);

                ResultErrorProcess process = new ResultErrorProcess(context);
                if (process.process(result)) {
                    ProfilConvertor profilConvertor = new ProfilConvertor();
                    user.setProfil(profilConvertor.convert(result));

                    Prihlaseni.this.onResult();
                }

                if (result.equals("ERROR")) {
                    user.setLogged(false);
                }
            }
        };
        stahniProfil.stahni();
    }

    /**
     * Spustí se po přihlášení {@link #prihlas(String, String)}
     */
    public void onResult() {

    }
}
