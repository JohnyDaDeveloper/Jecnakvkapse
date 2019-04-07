package cz.johnyapps.jecnakvkapse.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import cz.johnyapps.jecnakvkapse.Actions.Prihlaseni;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Tools.ThemeManager;

public class LoginActivity extends AppCompatActivity {
    private Context context;
    private SharedPreferences prefs;

    private EditText edtLogin;
    private EditText edtHeslo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context = this;

        ThemeManager manager = new ThemeManager(context);
        manager.loadTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    private void initialize() {
        prefs = getSharedPreferences("jecnakvkapse", MODE_PRIVATE);

        edtLogin = findViewById(R.id.LogIn_edtName);
        edtHeslo = findViewById(R.id.LogIn_edtPass);

        Setup_Login();
        //AutoLogin();
    }

    private void Setup_Login() {
        String login = prefs.getString("login", "NEULOZENO");
        String heslo = prefs.getString("pass", "NEULOZENO");

        if (login != null && !login.equals("NEULOZENO")) {
            edtLogin.setText(login);
        }

        if (heslo != null && !heslo.equals("NEULOZENO")) {
            edtHeslo.setText(heslo);
        }
    }

    private void AutoLogin() {
        String login = prefs.getString("login", "NEULOZENO");
        String heslo = prefs.getString("pass", "NEULOZENO");

        if (login != null && heslo !=null) {
            if (!login.equals("NEULOZENO") && !heslo.equals("NEULOZENO")) {
                login(login, heslo);
            }
        }
    }

    public void login(View V) {
        CheckBox chck = findViewById(R.id.LogIn_checkBox);

        String login = edtLogin.getText().toString();
        String heslo = edtHeslo.getText().toString();
        boolean pamatovat = chck.isChecked();

        if (pamatovat) {
            prefs.edit().putString("pass", heslo).apply();
        }

        login(login, heslo);
    }

    public void login(final String login, final String pass) {
        Prihlaseni prihlaseni = new Prihlaseni(context) {
            @Override
            public void onResult() {
                super.onResult();

                startMain();
            }

            @Override
            public void error() {
                super.error();
            }
        };

        prihlaseni.prihlas(login, pass);
    }

    private void startMain() {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        finish();
    }
}
