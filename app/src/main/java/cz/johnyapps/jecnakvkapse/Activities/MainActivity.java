package cz.johnyapps.jecnakvkapse.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import cz.johnyapps.jecnakvkapse.Actions.BaseAction;
import cz.johnyapps.jecnakvkapse.Actions.Prihlaseni;
import cz.johnyapps.jecnakvkapse.AnalyticsNames;
import cz.johnyapps.jecnakvkapse.Dialogs.DialogEnableCrashlytics;
import cz.johnyapps.jecnakvkapse.Dialogs.DialogLogin;
import cz.johnyapps.jecnakvkapse.Dialogs.DialogOdhlasit;
import cz.johnyapps.jecnakvkapse.Fragments.OmluvenkyFragment;
import cz.johnyapps.jecnakvkapse.Fragments.PrichodyFragment;
import cz.johnyapps.jecnakvkapse.Fragments.RozvrhFragment;
import cz.johnyapps.jecnakvkapse.Fragments.SuplarchFragment;
import cz.johnyapps.jecnakvkapse.Fragments.ZnamkyFragment;
import cz.johnyapps.jecnakvkapse.PrefsNames;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Singletons.User;
import cz.johnyapps.jecnakvkapse.Tools.CacheManager;
import cz.johnyapps.jecnakvkapse.Tools.Logger;
import cz.johnyapps.jecnakvkapse.Tools.ThemeManager;

/**
 * Hlavní aktivita. Otevře se při spuštění app.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    private Context context;
    private SharedPreferences prefs;
    private User user;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView = null;

    private int fragment_selected;

    /**
     * Nastaví content view a supustí {@link #initialize()}
     * @param savedInstanceState    Uložená instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;

        ThemeManager themeManager = new ThemeManager(context);
        themeManager.loadTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    /**
     * Načte globální proměnné, reklamu, menu {@link #setupMenu()} a fragment
     */
    private void initialize() {
        prefs = getSharedPreferences(PrefsNames.PREFS_NAME, MODE_PRIVATE);
        user = User.getUser();
        drawerLayout = findViewById(R.id.Main_layoutMain);
        navigationView = findViewById(R.id.Main_menu);
        fragment_selected = prefs.getInt(PrefsNames.MAIN_FRAGMENT, R.id.MenuMain_Znamky);

        FirebaseAnalytics.getInstance(context).setAnalyticsCollectionEnabled(true);

        setupMenu();
        clearChache();
        setupCrashlytics();
        autoLogin();
    }

    private void setupCrashlytics() {
        boolean ask = prefs.getBoolean(PrefsNames.ASK_FOR_CRASHLYTICS, true);

        if (ask) {
            DialogEnableCrashlytics dialogEnableCrashlytics = new DialogEnableCrashlytics(context);
            dialogEnableCrashlytics.setOnCompleteListener(new DialogEnableCrashlytics.OnCompleteListener() {
                @Override
                public void onComplete(boolean enabled) {
                    Logger.getInstance().initialize(enabled);
                    reportCrashlyticsStatus(enabled);
                }
            });
            dialogEnableCrashlytics.show();
        } else {
            boolean enabled = prefs.getBoolean(PrefsNames.CRASHLYTICS_ENABLED, false);
            Logger.getInstance().initialize(enabled);
            reportCrashlyticsStatus(enabled);
        }
    }

    private void reportCrashlyticsStatus(boolean enabled) {
        String crashlyticsStatus = enabled ? AnalyticsNames.ENABLED : AnalyticsNames.DISABLED;
        FirebaseAnalytics.getInstance(context).setUserProperty(AnalyticsNames.CRASHLYTICS, crashlyticsStatus);
    }

    /**
     * Nastaví menu
     */
    private void setupMenu() {
        navigationView.setNavigationItemSelectedListener(this);

        user.setLoggedListener(new User.LoggedListener() {
            @Override
            public void onLoggedChange(boolean logged) {
                if (logged) {
                    navigationView.getMenu().findItem(R.id.MenuMain_Prihlasit).setVisible(false);
                    navigationView.getMenu().findItem(R.id.MenuMain_Odhlasit).setVisible(true);
                } else {
                    navigationView.getMenu().findItem(R.id.MenuMain_Prihlasit).setVisible(true);
                    navigationView.getMenu().findItem(R.id.MenuMain_Odhlasit).setVisible(false);
                }
            }
        });
    }

    /**
     * Vymaže cache
     */
    private void clearChache() {
        CacheManager cacheManager = new CacheManager(context);
        cacheManager.clearAll();
    }

    /**
     * Automatické přihlášení s uloženými daty
     */
    private void autoLogin() {
        String login = prefs.getString(PrefsNames.LOGIN, "NEULOZENO");
        String heslo = prefs.getString(PrefsNames.PASSWORD, "NEULOZENO");

        if (!login.equals("NEULOZENO") && !heslo.equals("NEULOZENO")) {
            login(login, heslo);
        } else {
            ShowDialog_Login();
        }
    }

    /**
     * Znemožní odejít z aplikace stiskem tlačítka zpět
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    /**
     * Obstarává stisknutí tlačítka v menu
     * @param item  Vybraná položka z menu
     * @return      true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Logger.v(TAG, "onNavigationItemSelected: " + item.getTitle());

        drawerLayout.closeDrawers();

        switch (item.getItemId()) {
            case R.id.MenuMain_Prihlasit: {
                ShowDialog_Login();
                break;
            }

            case R.id.MenuMain_Odhlasit: {
                DialogOdhlasit odhlasit = new DialogOdhlasit(context);
                odhlasit.get().show();
                break;
            }

            case R.id.MenuMain_Nastaveni: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.MenuMain_OAplikaci: {
                Intent intent = new Intent(this, OAplikaci.class);
                startActivity(intent);
                break;
            }

            default: {
                if (item.getItemId() != fragment_selected) {
                    SwitchFragments(item.getItemId());
                }

                break;
            }
        }

        return true;
    }

    public void ShowDialog_Login() {
        DialogLogin login = new DialogLogin(context) {
            @Override
            public void login(String login, String pass, boolean remember) {
                super.login(login, pass, remember);

                MainActivity.this.login(login, pass);
            }
        };

        login.get().show();
    }

    /**
     * Otevře menu po stisknutí tlačítka
     * @param V Tlačítko
     */
    public void openMenu(View V) {
        drawerLayout.openDrawer(navigationView);
    }

    /**
     * Přepíná fragmenty
     * @param id    ID fragmentu na který se má přepnout
     */
    private void SwitchFragments(int id) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment;

        switch (id) {
            case R.id.MenuMain_Rozvrh: {
                Logger.i(TAG, "switching fragment to RozvrhFragment");
                fragment = new RozvrhFragment();
                break;
            }

            case R.id.MenuMain_Prichody: {
                Logger.i(TAG, "switching fragment to PrichodyFragment");
                fragment = new PrichodyFragment();
                break;
            }

            case R.id.MenuMain_Omluvenky: {
                Logger.i(TAG, "switching fragment to OmluvenkyFragment");
                fragment = new OmluvenkyFragment();
                break;
            }

            case R.id.MenuMain_Suplarch: {
                Logger.i(TAG, "switching fragment to SuplarchFragment");
                fragment = new SuplarchFragment();
                break;
            }

            default: {
                Logger.i(TAG, "switching fragment to ZnamkyFragment");
                fragment = new ZnamkyFragment();
                break;
            }
        }

        transaction.replace(R.id.Main_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        fragment_selected = id;
        onResume();
    }

    /**
     * Přihlásí uživatele
     * @param login Login
     * @param pass  Heslo
     */
    public void login(final String login, final String pass) {
        Prihlaseni prihlaseni = new Prihlaseni(context);
        prihlaseni.setOnCompleteListener(new BaseAction.OnCompleteListener() {
            @Override
            public void onComplete() {
                SwitchFragments(fragment_selected);
            }

            @Override
            public void onError() {
                SwitchFragments(fragment_selected);
            }
        });
        prihlaseni.prihlas(login, pass);
    }
}
