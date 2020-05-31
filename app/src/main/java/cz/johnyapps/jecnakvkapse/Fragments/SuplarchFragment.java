package cz.johnyapps.jecnakvkapse.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.Adapters.SuplarchLinkAdapter;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.HttpConnection.StahniData;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Singletons.User;
import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchHolder;
import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky.StahniSuplarchLinky;
import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky.SuplarchFindLink;
import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky.SuplarchLink;

/**
 * Fragment aktivity {@link cz.johnyapps.jecnakvkapse.Activities.MainActivity} pro zobrazování odkazů na suplování
 */
public class SuplarchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainFragment_Suplarch: ";
    private static final int PERMISSION_REQUEST_CODE = 749;

    private Context context;
    private User user;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private View noItems;

    /**
     * Nastaví content view a supustí {@link #initialize()}
     * @param savedInstanceState    Uložená instance
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Crashlytics.log(Log.INFO, TAG, "Loading");

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
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fragment_main_suplarch, container, false);
        recyclerView = view.findViewById(R.id.MainFragmentSuplarch_recycler);

        swipeLayout = view.findViewById(R.id.MainFragmentSuplarch_swipeLayout);
        swipeLayout.setOnRefreshListener(this);

        noItems = inflater.inflate(R.layout.no_items, view, false);
        view.addView(noItems);

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
     * Stará se o obnovení suplarlinků potažením dolů
     */
    @Override
    public void onRefresh() {
        suplarch();
    }

    /**
     * Zeptá se na povolení (Čtení a zapisování do úlozíště)
     */
    private void askForPermissions() {
        Crashlytics.log(Log.INFO, TAG, "Asking for permissions");
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

        Crashlytics.log(Log.INFO, TAG, "Handling permission request results");

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (processPermision(Manifest.permission.WRITE_EXTERNAL_STORAGE, permissions, grantResults) && processPermision(Manifest.permission.READ_EXTERNAL_STORAGE, permissions, grantResults)) {
                Crashlytics.log(Log.INFO, TAG, "Permission \"Write external storage\" guaranteed");
                displaySuplarchLinks();
            } else {
                Crashlytics.log(Log.INFO, TAG, "Permission \"Write external storage\" denied");
                permissionDenied();
            }
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
        Crashlytics.log(Log.INFO, TAG, "Processing \"Write external storage\" permission");

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
        Crashlytics.log(Log.INFO, TAG, "Downloading \"Suplarch linky\"");

        StahniSuplarchLinky stahniSuplarchLinky = new StahniSuplarchLinky();
        stahniSuplarchLinky.setOnCompleteListener(new StahniData.OnCompleteListener() {
            @Override
            public void onComplete(String result) {
                ResultErrorProcess process = new ResultErrorProcess(context);

                if (process.process(result)) {
                    Crashlytics.log(Log.INFO, TAG, "Converting \"Suplarch linky\"");
                    SuplarchFindLink suplarchFindLink = new SuplarchFindLink();
                    ArrayList<SuplarchLink> links = suplarchFindLink.convert(result);

                    SuplarchHolder suplarchHolder = new SuplarchHolder();
                    suplarchHolder.setSuplarchLinks(links);

                    user.setSuplarchHolder(suplarchHolder);

                    swipeLayout.setRefreshing(false);

                    displaySuplarchLinks();
                } else {
                    Crashlytics.log(Log.INFO, TAG, "Downloading \"Suplarch linky\" error: " + result);
                }
            }
        });

        swipeLayout.setRefreshing(true);

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
            Crashlytics.log(Log.INFO, TAG, "Displaying");

            if (!user.getSuplarchHolder().getSuplarchLinks().isEmpty()) {
                SuplarchLinkAdapter adapter = new SuplarchLinkAdapter(context, suplarchHolder.getSuplarchLinks());
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                noItems.setVisibility(View.VISIBLE);

                Toast.makeText(context, R.string.toasts_zadna_suplovani, Toast.LENGTH_LONG).show();
            }
        } else {
            suplarch();
        }
    }
}
