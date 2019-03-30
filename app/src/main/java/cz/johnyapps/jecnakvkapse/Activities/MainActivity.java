package cz.johnyapps.jecnakvkapse.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import cz.johnyapps.jecnakvkapse.Actions.Prihlaseni;
import cz.johnyapps.jecnakvkapse.Dialogs.DialogLogin;
import cz.johnyapps.jecnakvkapse.Dialogs.DialogOdhlasit;
import cz.johnyapps.jecnakvkapse.Fragments.Main.MainFragment_Omluvenky;
import cz.johnyapps.jecnakvkapse.Fragments.Main.MainFragment_Prichody;
import cz.johnyapps.jecnakvkapse.Fragments.Main.MainFragment_Rozvrh;
import cz.johnyapps.jecnakvkapse.Fragments.Main.MainFragment_Suplarch;
import cz.johnyapps.jecnakvkapse.Fragments.Main.MainFragment_Znamky;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Singletons.User;
import cz.johnyapps.jecnakvkapse.Tools.ThemeManager;

/**
 * Hlavní aktivita. Otevře se při spuštění app.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity: ";

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
        Crashlytics.log(TAG + "Loading");

        context = this;

        ThemeManager themeManager = new ThemeManager(context);
        themeManager.loadTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    /**
     * Znemožní odejít z aplikace stiskem tlačítka zpět
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    /**
     * Načte globální proměnné, reklamu, menu {@link #Setup_Menu()}, fragment a spustí {@link #AutoLogIn()}
     */
    private void initialize() {
        prefs = getSharedPreferences("jecnakvkapse", MODE_PRIVATE);
        user = User.getUser();

        drawerLayout = findViewById(R.id.Main_layoutMain);
        navigationView = findViewById(R.id.Main_menu);

        Setup_Menu();

        fragment_selected = prefs.getInt("main_fragment", R.id.MenuMain_Znamky);

        AutoLogIn();
    }

    /**
     * Nastaví menu
     */
    private void Setup_Menu() {
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
     * Obstarává stisknutí tlačítka v menu
     * @param item  Vybraná položka z menu
     * @return      true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Crashlytics.log(TAG + "menu item selected (" + item.toString() + ")");

        drawerLayout.closeDrawers();

        switch (item.getItemId()) {
            case R.id.MenuMain_Prihlasit: {
                Dialog_LogIn();
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

        switch (id) {
            case R.id.MenuMain_Znamky: {
                Crashlytics.log(TAG + "switching fragment to Znamky");
                MainFragment_Znamky fragment = new MainFragment_Znamky();
                transaction.replace(R.id.Main_fragment, fragment);
                break;
            }

            case R.id.MenuMain_Rozvrh: {
                Crashlytics.log(TAG + "switching fragment to Rozvrh");
                MainFragment_Rozvrh fragment = new MainFragment_Rozvrh();
                transaction.replace(R.id.Main_fragment, fragment);
                break;
            }

            case R.id.MenuMain_Prichody: {
                Crashlytics.log(TAG + "switching fragment to Prichody");
                MainFragment_Prichody fragment = new MainFragment_Prichody();
                transaction.replace(R.id.Main_fragment, fragment);
                break;
            }

            case R.id.MenuMain_Omluvenky: {
                Crashlytics.log(TAG + "switching fragment to Omluvenky");
                MainFragment_Omluvenky fragment = new MainFragment_Omluvenky();
                transaction.replace(R.id.Main_fragment, fragment);
                break;
            }

            case R.id.MenuMain_Suplarch: {
                Crashlytics.log(TAG + "switching fragment to Suplarch");
                MainFragment_Suplarch fragment = new MainFragment_Suplarch();
                transaction.replace(R.id.Main_fragment, fragment);
                break;
            }

            default: {
                Crashlytics.log(TAG + "fragment not found");
                break;
            }
        }

        transaction.addToBackStack(null);
        transaction.commit();
        fragment_selected = id;
        onResume();
    }

    /**
     * Pokusí se o automatické přihlášení přes {@link #login(String, String)} pomocí dat z paméti. Pokud selže spustí {@link #Dialog_LogIn()}
     */
    private void AutoLogIn() {
        String login = prefs.getString("login", null);
        String pass = prefs.getString("pass", null);

        if (!(login == null || pass == null)) {
            login(login, pass);
        } else {
            Dialog_LogIn();
        }
    }

    /**
     * Otevře přihlašovací dialog
     * @see DialogLogin
     */
    private void Dialog_LogIn() {
        DialogLogin dialogLogin = new DialogLogin(context){
            @Override
            public void login(String login, String pass, boolean remember) {
                super.login(login, pass, remember);
                MainActivity.this.login(login, pass);
            }
        };

        dialogLogin.get().show();
    }

    /**
     * Přihlásí uživatele a načte fragment přes {@link #SwitchFragments(int)}
     * @param login Login
     * @param pass  Heslo
     */
    public void login(final String login, final String pass) {
        Prihlaseni prihlaseni = new Prihlaseni(context) {
            @Override
            public void onResult() {
                super.onResult();
                SwitchFragments(fragment_selected);
            }

            @Override
            public void error() {
                super.error();

                if (fragment_selected == R.id.MenuMain_Rozvrh) {
                    SwitchFragments(fragment_selected);
                }
            }
        };

        prihlaseni.prihlas(login, pass);
    }
}
