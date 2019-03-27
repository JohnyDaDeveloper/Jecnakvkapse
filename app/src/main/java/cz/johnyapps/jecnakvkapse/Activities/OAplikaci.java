package cz.johnyapps.jecnakvkapse.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cz.johnyapps.jecnakvkapse.BuildConfig;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Tools.ThemeManager;

/**
 * Aktivita zobrazující informace o aplikaci
 */
public class OAplikaci extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager themeManager = new ThemeManager(this);
        themeManager.loadTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oaplikaci);

        initialize();
    }

    /**
     * Inicializace
     */
    private void initialize() {
        context = this;
        Setup_aplikace();
    }


    /**
     * Nastaví kartu "Aplikace"
     */
    @SuppressLint("SetTextI18n")
    private void Setup_aplikace() {
        TextView verze = findViewById(R.id.OAplikaci_appVerze);
        verze.setText(context.getResources().getString(R.string.oaplikaci_app_verze) + ": " + BuildConfig.VERSION_NAME);
    }

    /**
     * Po kliknutí na View ukončí aktivitu
     * @param V View
     */
    public void back (View V) {
        finish();
    }
}