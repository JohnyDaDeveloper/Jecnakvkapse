package cz.johnyapps.jecnakvkapse.Tools;

import android.content.Context;
import android.content.SharedPreferences;

import cz.johnyapps.jecnakvkapse.R;

import static android.content.Context.MODE_PRIVATE;

public class ThemeManager {
    private Context context;
    private SharedPreferences prefs;

    public ThemeManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("jecnakvkapse", MODE_PRIVATE);
    }

    public void loadTheme() {
        int theme = prefs.getInt("selected_theme", R.id.SettingsTheme_light);



        switch (theme) {
            case R.id.SettingsTheme_dark: {
                context.setTheme(R.style.DarkTheme);
                break;
            }

            case R.id.SettingsTheme_pink: {
                context.setTheme(R.style.PinkTheme);
                break;
            }

            default: {
                context.setTheme(R.style.LightTheme);
                break;
            }
        }
    }
}
