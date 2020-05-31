package cz.johnyapps.jecnakvkapse.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import cz.johnyapps.jecnakvkapse.PrefsNames;
import cz.johnyapps.jecnakvkapse.R;
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
    public DialogOdhlasit(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PrefsNames.PREFS_NAME, MODE_PRIVATE);
        user = User.getUser();
    }

    /**
     * Vrátí odhlašovací dialog
     * @return  Dialog
     */
    public AlertDialog get() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_odhlasit_title)
                .setPositiveButton(R.string.ano, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        odhlasit();
                    }
                })
                .setNegativeButton(R.string.zrusit, new DialogInterface.OnClickListener() {
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
        prefs.edit().remove(PrefsNames.PASSWORD).apply();
        user.setLogged(false);
        Toast.makeText(context, R.string.dialog_odhlasit_odhlaseno, Toast.LENGTH_SHORT).show();
    }
}
