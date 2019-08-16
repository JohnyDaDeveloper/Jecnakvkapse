package cz.johnyapps.jecnakvkapse.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
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
        themeManager.loadTheme(this);

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

    /**
     * Po kliknutí na mailovou adresu otevře mailového klienta a předvyplní údaje
     * @param V View s adresou
     */
    public void posliMail(View V) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"johnydadeveloper@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Verze aplikace: " + BuildConfig.VERSION_NAME);
        startActivity(intent);
    }

    /**
     * Po kliknutí na View otevře github
     * @param V View
     */
    public void gitHub(View V) {
        Uri uri = Uri.parse("https://github.com/JohnyDaDeveloper/Jecnakvkapse");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * Po kliknutí na View otevře github
     * @param V View
     */
    public void catosLibrary(View V) {
        Uri uri = Uri.parse("https://github.com/JohnyDaDeveloper/CatosLibrary");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
