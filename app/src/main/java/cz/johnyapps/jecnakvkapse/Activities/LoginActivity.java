package cz.johnyapps.jecnakvkapse.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import cz.johnyapps.jecnakvkapse.Actions.Prihlaseni;
import cz.johnyapps.jecnakvkapse.BuildConfig;
import cz.johnyapps.jecnakvkapse.Fragments.MainFragment_Rozvrh;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Receivers.NetworkStateReceiver;
import cz.johnyapps.jecnakvkapse.Singletons.User;
import cz.johnyapps.jecnakvkapse.Tools.ThemeManager;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity implements NetworkStateReceiver.ConnectivityReceiverListener {
    private static final String TAG = "LoginActivity: ";

    private Context context;
    private SharedPreferences prefs;

    private NetworkStateReceiver networkStateReceiver;

    private EditText edtLogin;
    private EditText edtHeslo;
    private CheckBox chck;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Crashlytics.log(TAG + "Loading");

        this.context = this;

        // Set up Crashlytics, disabled for debug builds
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(this, crashlyticsKit);

        ThemeManager manager = new ThemeManager(context);
        manager.loadTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    @Override
    protected void onResume() {
        Crashlytics.log(TAG + "onResume");
        super.onResume();

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.setConnectivityReceiverListener(this);
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        Crashlytics.log(TAG + "onPause");
        super.onPause();

        unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void onNetworkConnectionChanged(boolean connected) {
        btnLogin.setEnabled(connected);
    }

    /**
     * Inicializace
     */
    private void initialize() {
        prefs = getSharedPreferences("jecnakvkapse", MODE_PRIVATE);

        edtLogin = findViewById(R.id.LogIn_edtName);
        edtHeslo = findViewById(R.id.LogIn_edtPass);
        chck = findViewById(R.id.LogIn_checkBox);
        btnLogin = findViewById(R.id.LogIn_buttonLogin);

        Setup_Login();
        AutoLogin();
    }

    /**
     * Nastavení přihlašování (Předvyplnění dat atd.)
     */
    private void Setup_Login() {
        String login = prefs.getString("login", "NEULOZENO");
        String heslo = prefs.getString("pass", "NEULOZENO");

        if (login != null && !login.equals("NEULOZENO")) {
            edtLogin.setText(login);
        }

        if (heslo != null && !heslo.equals("NEULOZENO")) {
            edtHeslo.setText(heslo);
            chck.setChecked(true);
        }
    }

    /**
     * Automatické přihlášení s uloženými daty
     */
    private void AutoLogin() {
        String login = prefs.getString("login", "NEULOZENO");
        String heslo = prefs.getString("pass", "NEULOZENO");

        if (login != null && heslo !=null) {
            if (!login.equals("NEULOZENO") && !heslo.equals("NEULOZENO")) {
                login(login, heslo);
            }
        }
    }

    /**
     * Po kliknutí na View spustí offline mód aplikace
     * @param V View
     */
    public void offline(View V) {
        V.setEnabled(false);
        User.getUser().setOfflineModeEnabled(true);

        View login = findViewById(R.id.LogIn_loginLayout);
        login.setVisibility(View.GONE);

        View rozvrh = findViewById(R.id.LogIn_rozvrhLayout);
        rozvrh.setVisibility(View.VISIBLE);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MainFragment_Rozvrh mainFragment_rozvrh = new MainFragment_Rozvrh();
        transaction.add(R.id.LogIn_rozvrhLayout, mainFragment_rozvrh).commit();
    }

    /**
     * Po kliknutí na View spustí přihlášování
     * @param V View
     */
    public void login(View V) {
        String login = edtLogin.getText().toString();
        String heslo = edtHeslo.getText().toString();
        boolean pamatovat = chck.isChecked();

        login = login.replaceAll(" ", "");

        if (login.equals("crash")) {
            Crashlytics.getInstance().crash();
        }

        prefs.edit().putString("login", login).apply();
        if (pamatovat) {
            prefs.edit().putString("pass", heslo).apply();
        }

        login(login.toLowerCase(), heslo);
    }

    /**
     * Samotné přihlašobání pomocí loginu a hesla
     * @param login Login
     * @param pass  Heslo
     */
    public void login(final String login, final String pass) {
        btnLogin.setEnabled(false);

        Prihlaseni prihlaseni = new Prihlaseni(context) {
            @Override
            public void onResult() {
                super.onResult();

                startMain();
            }

            @Override
            public void error() {
                super.error();
                btnLogin.setEnabled(true);
            }
        };

        prihlaseni.prihlas(login, pass);
    }

    /**
     * Nastartuje aktivitu {@link MainActivity}
     */
    private void startMain() {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        finish();
    }

    /**
     * Zabrání vypnutí aplikace při zmáčknutí tlačítka zpět
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
