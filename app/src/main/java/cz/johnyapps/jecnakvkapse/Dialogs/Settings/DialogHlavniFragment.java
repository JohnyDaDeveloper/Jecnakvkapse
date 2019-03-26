package cz.johnyapps.jecnakvkapse.Dialogs.Settings;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.widget.TextView;
import android.widget.Toast;

import cz.johnyapps.jecnakvkapse.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Slouží k vybírání hlavního okna v nastavení
 * @see cz.johnyapps.jecnakvkapse.Activities.SettingsActivity
 */
public class DialogHlavniFragment {
    private Context context;
    private SharedPreferences prefs;
    private TextView subText;

    /**
     * Inicializace
     * @param context   Context
     * @param subText   Textové pole pod nadpisem v nastavení
     */
    public DialogHlavniFragment(Context context, TextView subText) {
        this.context = context;
        prefs = context.getSharedPreferences("jecnakvkapse", MODE_PRIVATE);
        this.subText = subText;
    }

    /**
     * Vytvoří nastavovací dialog
     * @return  Dialog
     */
    public AlertDialog get() {
        String[] strChoices = context.getResources().getStringArray(R.array.FragmentsMain_String);
        int chosen = prefs.getInt("main_fragment_pos", 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Hlavní okno")
                .setSingleChoiceItems(strChoices, chosen, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        change(which);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Zavřít", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    /**
     * Změní uložené okno
     * @param which Pozice v poli oken
     */
    private void change(int which) {
        String[] strChoices = context.getResources().getStringArray(R.array.FragmentsMain_String);
        TypedArray intChoices = context.getResources().obtainTypedArray(R.array.FragmentsMain_Frags);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("main_fragment", intChoices.getResourceId(which, R.id.MenuMain_Znamky));
        editor.putInt("main_fragment_pos", which);
        editor.apply();

        Toast.makeText(context, "Hlavní okno nastaveno na \"" + strChoices[which] + "\"", Toast.LENGTH_LONG).show();

        intChoices.recycle();
        subText.setText(strChoices[which]);
    }
}
