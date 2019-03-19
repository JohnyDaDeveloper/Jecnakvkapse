package cz.johnyapps.jecnakvkapse.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cz.johnyapps.jecnakvkapse.Dialogs.Settings.DialogHlavniFragment;
import cz.johnyapps.jecnakvkapse.R;

public class SettingsActivity extends AppCompatActivity {
    private Context context;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();
    }

    private void initialize() {
        context = this;
        prefs = getSharedPreferences("jecnakvkapse", MODE_PRIVATE);

        Setup_HlavniFragment();
    }

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

    public void back(View view) {
        finish();
    }
}
