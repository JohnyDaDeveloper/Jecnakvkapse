package cz.johnyapps.jecnakvkapse.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;

import cz.johnyapps.jecnakvkapse.PrefsNames;
import cz.johnyapps.jecnakvkapse.R;

import static android.content.Context.MODE_PRIVATE;

public class DialogEnableCrashlytics {
    private Context context;
    private SharedPreferences prefs;

    public DialogEnableCrashlytics(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PrefsNames.PREFS_NAME, MODE_PRIVATE);
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_enable_crashlytics)
                .setMessage(R.string.dialog_enable_crashlytics_message)
                .setCancelable(false)
                .setPositiveButton(R.string.povolit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        submit(true);
                    }
                })
                .setNegativeButton(R.string.zakazat, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        submit(false);
                    }
                }).create().show();
    }

    private void submit(boolean enabled) {
        prefs.edit().putBoolean(PrefsNames.ASK_FOR_CRASHLYTICS, false).apply();
        prefs.edit().putBoolean(PrefsNames.CRASHLYTICS_ENABLED, enabled).apply();

        if (onCompleteListener != null) {
            onCompleteListener.onComplete(enabled);
        }
    }

    private OnCompleteListener onCompleteListener;
    public interface OnCompleteListener {
        void onComplete(boolean enabled);
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }
}
