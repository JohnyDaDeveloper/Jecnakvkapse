package cz.johnyapps.jecnakvkapse.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cz.johnyapps.jecnakvkapse.R;
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
        themeManager.loadTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();
    }

    /**
     * Inicializace
     */
    private void initialize() {
        prefs = getSharedPreferences("jecnakvkapse", MODE_PRIVATE);

        Setup_HlavniFragment();
        Setup_Theme();
    }

    /**
     * Nastavení volby hlavního okna
     */
    private void Setup_HlavniFragment() {
        int selected = prefs.getInt("main_fragment", R.id.MenuMain_Znamky);

        switch (selected) {
            case R.id.FirstFragment_znamky: {
                RadioButton button = findViewById(R.id.FirstFragment_znamky);
                button.setChecked(true);
                break;
            }

            case R.id.FirstFragment_rozvrh: {
                RadioButton button = findViewById(R.id.FirstFragment_rozvrh);
                button.setChecked(true);
                break;
            }

            case R.id.FirstFragment_prichody: {
                RadioButton button = findViewById(R.id.FirstFragment_prichody);
                button.setChecked(true);
                break;
            }

            case R.id.FirstFragment_omluvenky: {
                RadioButton button = findViewById(R.id.FirstFragment_omluvenky);
                button.setChecked(true);
                break;
            }

            case R.id.FirstFragment_suplovani: {
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
                    case R.id.FirstFragment_znamky: {
                        editor.putInt("main_fragment", R.id.MenuMain_Znamky);
                        break;
                    }

                    case R.id.FirstFragment_rozvrh: {
                        editor.putInt("main_fragment", R.id.MenuMain_Rozvrh);
                        break;
                    }

                    case R.id.FirstFragment_prichody: {
                        editor.putInt("main_fragment", R.id.MenuMain_Prichody);
                        break;
                    }

                    case R.id.FirstFragment_omluvenky: {
                        editor.putInt("main_fragment", R.id.MenuMain_Omluvenky);
                        break;
                    }

                    case R.id.FirstFragment_suplovani: {
                        editor.putInt("main_fragment", R.id.MenuMain_Suplarch);
                        break;
                    }

                    default: editor.putInt("main_fragment", R.id.MenuMain_Znamky);
                }

                editor.apply();
            }
        });
    }

    /**
     * Nastavení volby motivu
     */
    private void Setup_Theme() {
        int theme = prefs.getInt("selected_theme", R.id.SettingsTheme_light);
        boolean pink = prefs.getBoolean("enable_pink", false);

        if (pink) {
            RadioButton button = findViewById(R.id.SettingsTheme_pink);
            button.setVisibility(View.VISIBLE);
        }

        RadioGroup radioGroup = findViewById(R.id.SettingsTheme_themes);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int theme = prefs.getInt("selected_theme", R.id.SettingsTheme_light);

                switch (checkedId) {
                    case R.id.SettingsTheme_dark: {
                        if (theme != R.id.SettingsTheme_dark) {
                            prefs.edit().putInt("selected_theme", R.id.SettingsTheme_dark).apply();
                            restart();
                        }

                        break;
                    }

                    case R.id.SettingsTheme_pink: {
                        if (theme != R.id.SettingsTheme_pink) {
                            prefs.edit().putInt("selected_theme", R.id.SettingsTheme_pink).apply();
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

            default: {
                RadioButton button = findViewById(R.id.SettingsTheme_light);
                button.setChecked(true);
                break;
            }
        }
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
