package cz.johnyapps.jecnakvkapse.Fragments.Main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky.StahniSuplarchLinky;
import cz.johnyapps.jecnakvkapse.Adapters.SuplarchLinkAdapter;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Singletons.User;
import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky.SuplarchFindLink;
import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchHolder;
import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky.SuplarchLink;

/**
 * Fragment aktivity {@link cz.johnyapps.jecnakvkapse.Activities.MainActivity} pro zobrazování odkazů na suplování
 */
public class MainFragment_Suplarch extends Fragment {
    private static final int PERMISSION_REQUEST_CODE = 749;

    private Context context;
    private User user;

    private RecyclerView recyclerView;

    /**
     * Nastaví content view a supustí {@link #initialize()}
     * @param savedInstanceState    Uložená instance
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    /**
     * Načte globální proměnné
     */
    private void initialize() {
        this.context = getContext();
        this.user = User.getUser();
    }

    /**
     * Nastaví layout a načte hlaví views
     * @param inflater              Inflater
     * @param container             Container
     * @param savedInstanceState    Uložená instance
     * @return                      Hlavní layout
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_suplarch, container, false);
        recyclerView = view.findViewById(R.id.MainFragmentSuplarch_recycler);

        return view;
    }

    /**
     * Zobrazí odkazy na suplování
     * @see #displaySuplarchLinks()
     */
    @Override
    public void onStart() {
        super.onStart();
        askForPermissions();
    }

    /**
     * Zeptá se na povolení (Čtení a zapisování do úlozíště)
     */
    private void askForPermissions() {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions, PERMISSION_REQUEST_CODE);
    }

    /**
     * Zpracuje output po {@link #askForPermissions()} pomocí {@link #processPermision(String, String[], int[])}
     * @param requestCode   Kód requestu
     * @param permissions   Povolení
     * @param grantResults  Výsledek (povoleno, zamítnuto)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (processPermision(Manifest.permission.WRITE_EXTERNAL_STORAGE, permissions, grantResults) && processPermision(Manifest.permission.READ_EXTERNAL_STORAGE, permissions, grantResults)) {
                    displaySuplarchLinks();
                } else {
                    permissionDenied();
                }
                break;
            }

            default: break;
        }
    }

    /**
     * Zobrází zprávu "Povolení zamítnuto"
     */
    private void permissionDenied() {
        Toast.makeText(context, "Povolení zamítnuto", Toast.LENGTH_SHORT).show();
    }

    /**
     * Zprocesuje povolení
     * @param askedPermission   Povolení, na které se ptáme
     * @param permissions       Povolení
     * @param grantResult       Output
     * @return                  True - povoleno, False - zamítnuto
     */
    private boolean processPermision(String askedPermission, String[] permissions, int[] grantResult) {
        if (permissions.length > 0) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(askedPermission)) {
                    return grantResult[i] == PackageManager.PERMISSION_GRANTED;
                }
            }
        }

        return false;
    }

    /**
     * Stáhne odkazy na suplování
     * @see StahniSuplarchLinky
     * @see SuplarchFindLink
     */
    public void suplarch() {
        StahniSuplarchLinky stahniSuplarchLinky = new StahniSuplarchLinky(context) {
            @Override
            public void onResult(String result) {
                super.onResult(result);

                ResultErrorProcess process = new ResultErrorProcess(context);

                if (process.process(result)) {
                    SuplarchFindLink suplarchFindLink = new SuplarchFindLink();
                    ArrayList<SuplarchLink> links = suplarchFindLink.convert(result);

                    SuplarchHolder suplarchHolder = new SuplarchHolder();
                    suplarchHolder.setSuplarchLinks(links);

                    user.setSuplarchHolder(suplarchHolder);

                    displaySuplarchLinks();
                }
            }
        };

        stahniSuplarchLinky.stahni();
    }

    /**
     * Pokud jsou linky staženy, zobrazí je, pokud ne, stáhne je
     * @see #suplarch()
     * @see SuplarchLinkAdapter
     * @see SuplarchLink
     * @see SuplarchHolder
     */
    public void displaySuplarchLinks() {
        SuplarchHolder suplarchHolder = user.getSuplarchHolder();

        if (suplarchHolder != null) {
            SuplarchLinkAdapter adapter = new SuplarchLinkAdapter(context, suplarchHolder.getSuplarchLinks());
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        } else {
            suplarch();
        }
    }
}
