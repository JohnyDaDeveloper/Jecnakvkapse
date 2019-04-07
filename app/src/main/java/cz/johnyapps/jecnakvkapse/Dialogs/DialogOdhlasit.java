package cz.johnyapps.jecnakvkapse.Dialogs;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;

import cz.johnyapps.jecnakvkapse.Activities.LoginActivity;
import cz.johnyapps.jecnakvkapse.Singletons.User;

import static android.content.Context.MODE_PRIVATE;

/**
 * Dialog na odhlášení.
 */
public class DialogOdhlasit {
    private Context context;
    private SharedPreferences prefs;
    private User user;

    /**
     * Inicializace
     * @param context   Context
     */
    protected DialogOdhlasit(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("jecnakvkapse", MODE_PRIVATE);
        user = User.getUser();
    }

    /**
     * Vrátí odhlašovací dialog
     * @return  Dialog
     */
    public AlertDialog get() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Odhlásit?")
                .setPositiveButton("Ano", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        odhlasit();
                    }
                })
                .setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    /**
     * Odhlásí uživatele po kliknutí na tlačítko odhlásit v dialogu z {@link #get()}
     */
    private void odhlasit() {
        prefs.edit().remove("pass").apply();
        user.setLogged(false);

        Toast.makeText(context, "Odhášeno", Toast.LENGTH_SHORT).show();

        finished();
    }

    /**
     * Spustí se při dokončení odhlášení
     */
    public void finished() {

    }
}
