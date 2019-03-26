package cz.johnyapps.jecnakvkapse.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
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
     * Nastavení volby módu
     */
    private void Setup_Theme() {
        String theme = prefs.getString("theme", "light");

        if (theme != null) {
            boolean dark = theme.equals("dark");
            Switch sw = findViewById(R.id.Theme_title);

            if (dark) {
                sw.setChecked(true);
            } else {
                sw.setChecked(false);
            }

            sw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Switch sw = (Switch) v;

                    if (sw.isChecked()) {
                        prefs.edit().putString("theme", "dark").apply();
                        restart();
                    } else {
                        prefs.edit().putString("theme", "light").apply();
                        restart();
                    }
                }
            });
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
     * Restartuje aktivitu
     */
    public void restart() {
        Intent intent = new Intent(this, this.getClass());
        startActivity(intent);
        finish();
    }
}
