package cz.johnyapps.jecnakvkapse.Tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.crashlytics.android.Crashlytics;

import cz.johnyapps.jecnakvkapse.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Stará se o načítání témat (themes). <b>Je potřeba přidat do onCreate KAŽDÉ AKTIVITY před setContentView</b>.
 */
public class ThemeManager {
    private static final String TAG = "ThemeManager: ";
    private Context context;
    private SharedPreferences prefs;

    /**
     * Inicializace
     * @param context   Context
     */
    public ThemeManager(Context context) {
        this.context = context;

        prefs = context.getSharedPreferences("jecnakvkapse", MODE_PRIVATE);
    }

    /**
     *  Načte nastavené téma z paměti zařízení
     */
    public void loadTheme() {
        int theme = prefs.getInt("selected_theme", R.id.SettingsTheme_light);

        switch (theme) {
            case R.id.SettingsTheme_dark: {
                Crashlytics.log(TAG + "switching theme to Dark");
                context.setTheme(R.style.DarkTheme);
                break;
            }

            case R.id.SettingsTheme_pink: {
                Crashlytics.log(TAG + "switching theme to Pink");
                context.setTheme(R.style.PinkTheme);
                break;
            }

            default: {
                Crashlytics.log(TAG + "switching theme to Default (light)");
                context.setTheme(R.style.LightTheme);
                break;
            }
        }
    }
}
