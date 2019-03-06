package cz.johnyapps.jecnakvkapse.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import cz.johnyapps.jecnakvkapse.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Dialog pro přihlašování
 * @see cz.johnyapps.jecnakvkapse.Actions.Prihlaseni
 */
public class DialogLogin {
    private Context context;
    private SharedPreferences prefs;

    /**
     * Inicializace
     * @param context   Context
     */
    protected DialogLogin(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("jecnakvkapse", MODE_PRIVATE);
    }

    /**
     * Vygeneruje dialog
     * @return  AlertDialog
     */
    public AlertDialog get() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_login, null, false);

        String login = prefs.getString("login", "NEULOZENO");

        if (login != null && !login.equals("NEULOZENO")) {
            EditText edtName = view.findViewById(R.id.DialogLogIn_edtName);
            edtName.setText(login);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Přihlášení")
                .setView(view)
                .setPositiveButton("Přihlásit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog alertDialog = (AlertDialog) dialog;

                        EditText edtName = alertDialog.findViewById(R.id.DialogLogIn_edtName);
                        EditText edtPass = alertDialog.findViewById(R.id.DialogLogIn_edtPass);
                        CheckBox checkBox = alertDialog.findViewById(R.id.DialogLogIn_checkBox);

                        String login = edtName.getText().toString();
                        login = login.replaceAll(" ", "");


                        login(login, edtPass.getText().toString(), checkBox.isChecked());
                    }
                })
                .setCancelable(false);

        return builder.create();
    }

    /**
     * Uživatel zadal jméno a heslo a zmáčknul tlačítko "Přihlásit"
     * @param login     Login
     * @param pass      Heslo
     * @param remember  Pamatovat heslo
     * @see #get()
     */
    public void login(String login, String pass, boolean remember) {
        prefs.edit().putString("login", login).apply();
        if (remember) prefs.edit().putString("pass", pass).apply();
    }
}
