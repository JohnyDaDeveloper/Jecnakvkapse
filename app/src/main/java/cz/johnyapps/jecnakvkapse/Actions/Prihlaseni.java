package cz.johnyapps.jecnakvkapse.Actions;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.Dialogs.DialogLoading;
import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Data;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.HttpConnection.StahniData;
import cz.johnyapps.jecnakvkapse.Profil.ProfilConvertor;
import cz.johnyapps.jecnakvkapse.Profil.StahniProfil;
import cz.johnyapps.jecnakvkapse.Singletons.User;

/**
 * Slouží k obstarání sessionID
 */
public class Prihlaseni extends BaseAction {
    private Context context;
    private User user;

    /**
     * Inicializace
     * @param context   Context
     */
    public Prihlaseni(Context context) {
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
        if (!User.getUser().isDummy()) {
            DialogLoading dialogLoading = new DialogLoading(context);
            AlertDialog dialog = dialogLoading.get("Přihlašování...");

            Crashlytics.log("logging in");

            ArrayList<Data> data = new ArrayList<>();
            data.add(new Data("user", user));
            data.add(new Data("pass", pass));

            this.user.setLogin(user);

            Request request = new Request("user/login", "POST");
            request.putData(data);

            Connection connection = new Connection(dialog);
            connection.setOnCompleteListener(new Connection.OnCompleteListener() {
                @Override
                public void onComplete(String result) {
                    User.getUser().setLogged(true);
                    stahniProfil();
                }
            });
            connection.execute(request);
        } else {
            completed();
        }
    }

    /**
     * Stáhne údaje z profilu
     */
    private void stahniProfil() {
        StahniProfil stahniProfil = new StahniProfil();
        stahniProfil.setOnCompleteListener(new StahniData.OnCompleteListener() {
            @Override
            public void onComplete(String result) {
                ResultErrorProcess process = new ResultErrorProcess(context);
                if (process.process(result)) {
                    ProfilConvertor profilConvertor = new ProfilConvertor();
                    user.setProfil(profilConvertor.convert(result));
                    completed();
                } else {
                    error();
                }

                if (result.equals("ERROR")) {
                    user.setLogged(false);
                }
            }
        });
        stahniProfil.stahni();
    }
}
