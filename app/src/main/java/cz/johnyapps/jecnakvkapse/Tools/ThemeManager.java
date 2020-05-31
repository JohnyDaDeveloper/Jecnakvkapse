package cz.johnyapps.jecnakvkapse.Tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import cz.johnyapps.jecnakvkapse.PrefsNames;
import cz.johnyapps.jecnakvkapse.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Stará se o načítání témat (themes). <b>Je potřeba přidat do onCreate KAŽDÉ AKTIVITY před setContentView</b>.
 */
public class ThemeManager {
    private static final String TAG = "ThemeManager";
    private Context context;
    private SharedPreferences prefs;

    /**
     * Inicializace
     * @param context   Context
     */
    public ThemeManager(Context context) {
        this.context = context;

        prefs = context.getSharedPreferences(PrefsNames.PREFS_NAME, MODE_PRIVATE);
    }

    /**
     *  Načte nastavené téma z paměti zařízení
     */
    public void loadTheme(Activity activity) {
        int theme = prefs.getInt(PrefsNames.SELECTED_THEME, R.id.SettingsTheme_light);
        switch (theme) {
            case R.id.SettingsTheme_dark: {
                Logger.i(TAG, "loadTheme: Dark");
                context.setTheme(R.style.DarkTheme);
                break;
            }

            case R.id.SettingsTheme_pink: {
                Logger.i(TAG, "loadTheme: Pink");
                context.setTheme(R.style.PinkTheme);
                break;
            }

            case R.id.SettingsTheme_red: {
                Logger.i(TAG, "loadTheme: Code Red");
                context.setTheme(R.style.RedTheme);
                break;
            }

            default: {
                Logger.i(TAG, "loadTheme: Default (Light)");
                context.setTheme(R.style.LightTheme);
                break;
            }
        }

        String label = context.getResources().getString(R.string.app_name);
        ColorUtils colorUtils = new ColorUtils();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            int icon = R.mipmap.ic_launcher_round;
            activity.setTaskDescription(new ActivityManager.TaskDescription(label, icon, colorUtils.getColorPrimary(context)));
        } else {
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round);
            activity.setTaskDescription(new ActivityManager.TaskDescription(label, icon, colorUtils.getColorPrimary(context)));
        }
    }
}
