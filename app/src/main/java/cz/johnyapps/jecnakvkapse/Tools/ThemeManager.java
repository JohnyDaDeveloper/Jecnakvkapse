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
        String theme = prefs.getString("theme", "light");

        if (theme != null) {
            switch (theme) {
                case "dark": {
                    context.setTheme(R.style.DarkTheme);
                    break;
                }

                case "pink": {
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
}
