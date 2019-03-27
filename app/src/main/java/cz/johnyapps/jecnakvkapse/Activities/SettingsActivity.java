package cz.johnyapps.jecnakvkapse.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cz.johnyapps.jecnakvkapse.Dialogs.Settings.DialogHlavniFragment;
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
     * @see DialogHlavniFragment
     */
    private void Setup_HlavniFragment() {
        String[] frags = context.getResources().getStringArray(R.array.FragmentsMain_String);
        int pos = prefs.getInt("main_fragment_pos", 0);

        TextView subText = findViewById(R.id.FirstFragment_selected);
        subText.setText(frags[pos]);

        View view = findViewById(R.id.Settings_HlavniFragment);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView subText = v.findViewById(R.id.FirstFragment_selected);

                DialogHlavniFragment dialog = new DialogHlavniFragment(context, subText);
                dialog.get().show();
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
