package cz.johnyapps.jecnakvkapse.Actions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.Dialogs.DialogLoading;
import cz.johnyapps.jecnakvkapse.HttpConnection.Connection;
import cz.johnyapps.jecnakvkapse.HttpConnection.Data;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;
import cz.johnyapps.jecnakvkapse.Profil.ProfilConvertor;
import cz.johnyapps.jecnakvkapse.Profil.StahniProfil;
import cz.johnyapps.jecnakvkapse.Singletons.User;

import static android.content.Context.MODE_PRIVATE;

/**
 * Slouží k obstarání sessionID
 */
public class Prihlaseni {
    private static final String TAG = "Login";

    private Context context;
    private SharedPreferences prefs;
    private User user;

    /**
     * Inicializace
     * @param context   Context
     */
    protected Prihlaseni(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("jecnakvkapse", MODE_PRIVATE);
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

                stahniProfil();

                User.getUser().setLogged(true);
                onResult();
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

                ProfilConvertor profilConvertor = new ProfilConvertor();
                user.setProfil(profilConvertor.convert(result));
            }
        };
        stahniProfil.stahni();
    }

    /**
     * Načte Premium status z paměti zařízení
     */
    private void loadStatus() {
        boolean status = prefs.getBoolean("premium", false);
        user.setPremium(status);

        Log.i(TAG, "premium = " + status);
    }

    /**
     * Spustí se po přihlášení {@link #prihlas(String, String)}
     */
    public void onResult() {
        loadStatus();
    }
}
