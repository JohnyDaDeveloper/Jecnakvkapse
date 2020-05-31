package cz.johnyapps.jecnakvkapse.Dialogs;

import android.content.Context;
import android.widget.ProgressBar;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

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


    public AlertDialog get(@StringRes int message) {
        return get(context.getResources().getString(message));
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
