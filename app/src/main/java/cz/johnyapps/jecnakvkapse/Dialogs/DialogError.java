package cz.johnyapps.jecnakvkapse.Dialogs;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import cz.johnyapps.jecnakvkapse.R;

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

    public AlertDialog get(@StringRes int message) {
        return get(context.getResources().getString(message));
    }

    /**
     * Vygeneruje dialog
     * @param message   Zpráva
     * @return          AlertDialog se zprávou
     */
    public AlertDialog get(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_error_title)
                .setMessage(message)
                .setNegativeButton(R.string.zavrit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}
