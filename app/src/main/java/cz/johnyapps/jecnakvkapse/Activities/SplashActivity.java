package cz.johnyapps.jecnakvkapse.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import cz.johnyapps.jecnakvkapse.Tools.Logger;

/**
 * Aktivita která zobrazí iconu na modrém pozadí během načítání {@link MainActivity}
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.getInstance().initialize(true);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
