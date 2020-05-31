package cz.johnyapps.jecnakvkapse.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.analytics.FirebaseAnalytics;

import cz.johnyapps.jecnakvkapse.AnalyticsNames;
import cz.johnyapps.jecnakvkapse.PrefsNames;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Tools.Logger;
import cz.johnyapps.jecnakvkapse.Tools.ThemeManager;

/**
 * Aktivita sloužící k nastavování aplikace
 */
public class SettingsActivity extends AppCompatActivity {
    private Context context;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;

        ThemeManager themeManager = new ThemeManager(context);
        themeManager.loadTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();
    }

    /**
     * Inicializace
     */
    private void initialize() {
        prefs = getSharedPreferences(PrefsNames.PREFS_NAME, MODE_PRIVATE);

        setupHlavniFragment();
        setupTheme();
        setupCrashlytics();
    }

    /**
     * Nastavení volby hlavního okna
     */
    private void setupHlavniFragment() {
        int selected = prefs.getInt("main_fragment", R.id.MenuMain_Znamky);

        switch (selected) {
            case R.id.MenuMain_Rozvrh: {
                RadioButton button = findViewById(R.id.FirstFragment_rozvrh);
                button.setChecked(true);
                break;
            }

            case R.id.MenuMain_Prichody: {
                RadioButton button = findViewById(R.id.FirstFragment_prichody);
                button.setChecked(true);
                break;
            }

            case R.id.MenuMain_Omluvenky: {
                RadioButton button = findViewById(R.id.FirstFragment_omluvenky);
                button.setChecked(true);
                break;
            }

            case R.id.MenuMain_Suplarch: {
                RadioButton button = findViewById(R.id.FirstFragment_suplovani);
                button.setChecked(true);
                break;
            }

            default: {
                RadioButton button = findViewById(R.id.FirstFragment_znamky);
                button.setChecked(true);
                break;
            }
        }

        RadioGroup group = findViewById(R.id.FirstFragment_selected);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor = prefs.edit();

                switch (checkedId) {
                    case R.id.FirstFragment_rozvrh: {
                        editor.putInt(PrefsNames.MAIN_FRAGMENT, R.id.MenuMain_Rozvrh);
                        break;
                    }

                    case R.id.FirstFragment_prichody: {
                        editor.putInt(PrefsNames.MAIN_FRAGMENT, R.id.MenuMain_Prichody);
                        break;
                    }

                    case R.id.FirstFragment_omluvenky: {
                        editor.putInt(PrefsNames.MAIN_FRAGMENT, R.id.MenuMain_Omluvenky);
                        break;
                    }

                    case R.id.FirstFragment_suplovani: {
                        editor.putInt(PrefsNames.MAIN_FRAGMENT, R.id.MenuMain_Suplarch);
                        break;
                    }

                    default: {
                        editor.putInt(PrefsNames.MAIN_FRAGMENT, R.id.MenuMain_Znamky);
                        break;
                    }
                }

                editor.apply();
            }
        });
    }

    /**
     * Nastavení volby motivu
     */
    private void setupTheme() {
        int theme = prefs.getInt(PrefsNames.SELECTED_THEME, R.id.SettingsTheme_light);
        boolean pink = prefs.getBoolean(PrefsNames.ENABLE_PINK, false);
        boolean red = prefs.getBoolean(PrefsNames.ENABLE_CODE_RED, false);

        if (pink) {
            RadioButton button = findViewById(R.id.SettingsTheme_pink);
            button.setVisibility(View.VISIBLE);
        }

        if (red) {
            RadioButton button = findViewById(R.id.SettingsTheme_red);
            button.setVisibility(View.VISIBLE);
        }

        RadioGroup radioGroup = findViewById(R.id.SettingsTheme_themes);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int theme = prefs.getInt(PrefsNames.SELECTED_THEME, R.id.SettingsTheme_light);

                switch (checkedId) {
                    case R.id.SettingsTheme_dark: {
                        if (theme != R.id.SettingsTheme_dark) {
                            prefs.edit().putInt(PrefsNames.SELECTED_THEME, R.id.SettingsTheme_dark).apply();
                            restart();
                        }

                        break;
                    }

                    case R.id.SettingsTheme_pink: {
                        if (theme != R.id.SettingsTheme_pink) {
                            prefs.edit().putInt(PrefsNames.SELECTED_THEME, R.id.SettingsTheme_pink).apply();
                            restart();
                        }

                        break;
                    }

                    case R.id.SettingsTheme_red: {
                        if (theme != R.id.SettingsTheme_red) {
                            prefs.edit().putInt("selected_theme", R.id.SettingsTheme_red).apply();
                            restart();
                        }

                        break;
                    }

                    default: {
                        if (theme != R.id.SettingsTheme_light) {
                            prefs.edit().putInt("selected_theme", R.id.SettingsTheme_light).apply();
                            restart();

                        }

                        break;
                    }
                }
            }
        });

        switch (theme) {
            case R.id.SettingsTheme_dark: {
                RadioButton button = findViewById(R.id.SettingsTheme_dark);
                button.setChecked(true);
                break;
            }

            case R.id.SettingsTheme_pink: {
                RadioButton button = findViewById(R.id.SettingsTheme_pink);
                button.setChecked(true);
                break;
            }

            case R.id.SettingsTheme_red: {
                RadioButton button = findViewById(R.id.SettingsTheme_red);
                button.setChecked(true);
                break;
            }

            default: {
                RadioButton button = findViewById(R.id.SettingsTheme_light);
                button.setChecked(true);
                break;
            }
        }
    }

    private void setupCrashlytics() {
        SwitchCompat crashlyticsSwitch = findViewById(R.id.crashlyticsEnabledSwitch);
        crashlyticsSwitch.setChecked(prefs.getBoolean(PrefsNames.CRASHLYTICS_ENABLED, false));
        crashlyticsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                prefs.edit().putBoolean(PrefsNames.CRASHLYTICS_ENABLED, b).apply();

                if (b) {
                    Logger.getInstance().enableCrashlytics();
                    FirebaseAnalytics.getInstance(context).setUserProperty(AnalyticsNames.CRASHLYTICS, AnalyticsNames.ENABLED);
                } else {
                    Logger.getInstance().disableCrashlytics();
                    FirebaseAnalytics.getInstance(context).setUserProperty(AnalyticsNames.CRASHLYTICS, AnalyticsNames.DISABLED);
                }
            }
        });
    }

    /**
     * Po kliknutí na šipku v horním rohu vrátí uživatele zpět do předchozí aktivity
     * @param view  Šipka
     */
    public void back(View view) {
        finish();
    }

    /**
     * Restartuje aplikaci
     */
    public void restart() {
        Intent intent = new Intent(context, context.getClass());
        startActivity(intent);
        finish();
    }
}
