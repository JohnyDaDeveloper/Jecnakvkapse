package cz.johnyapps.jecnakvkapse.Dialogs;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.widget.ProgressBar;

/**
 * Dialog pro načítání
 */
public class DialogLoading {
    private Context context;

    /**
     * Inicializace
     * @param context   Context
     */
    public DialogLoading(Context context) {
        this.context = context;
    }

    /**
     * Vygeneruje dialog
     * @param message   Zprává
     * @return          Dialog se zprávou
     */
    public AlertDialog get(String message) {
        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(30, 0, 30, 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message)
                .setCancelable(false)
                .setView(progressBar);

        return builder.create();
    }
}
