package cz.johnyapps.jecnakvkapse.Dialogs;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Dialog pro errory
 */
public class DialogError {
    private Context context;

    /**
     * Inicializace
     * @param context   Context
     */
    public DialogError(Context context) {
        this.context = context;
    }

    /**
     * Vygeneruje dialog
     * @param message   Zpráva
     * @return          AlertDialog se zprávou
     */
    public AlertDialog get(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error")
                .setMessage(message)
                .setNegativeButton("Zavřít", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}
