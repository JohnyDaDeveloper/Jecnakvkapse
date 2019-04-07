package cz.johnyapps.jecnakvkapse.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;

import cz.johnyapps.jecnakvkapse.Rozvrh.StahniRozvrh;
import cz.johnyapps.jecnakvkapse.Adapters.RozvrhAdaper;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Rozvrh.Rozvrh;
import cz.johnyapps.jecnakvkapse.Rozvrh.RozvrhConventor;
import cz.johnyapps.jecnakvkapse.Singletons.User;
import cz.johnyapps.jecnakvkapse.Tools.OfflineMode;

/**
 * Fragment aktivity {@link cz.johnyapps.jecnakvkapse.Activities.MainActivity} pro zobrazování rozvrhu
 */
public class MainFragment_Rozvrh extends Fragment {
    private static final String TAG = "MainFragment_Rozvrh: ";
    private Context context;
    private User user;
    private OfflineMode offlineMode;

    private LinearLayout rozvrhLayout;
    private ProgressBar progressBar;

    /**
     * Nastaví content view a supustí {@link #initialize()}
     * @param savedInstanceState    Uložená instance
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Crashlytics.log(TAG + "Loading");

        super.onCreate(savedInstanceState);
        initialize();
    }

    /**
     * Načte globální proměnné
     */
    private void initialize() {
        this.context = getContext();
        this.user = User.getUser();
        this.offlineMode = new OfflineMode(context);
    }

    /**
     * Nastaví layout a načte hlaví views.
     * @param inflater              Inflater
     * @param container             Container
     * @param savedInstanceState    Uložená instance
     * @return                      Hlavní layout
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_rozvrh, container, false);

        rozvrhLayout = view.findViewById(R.id.MainFragmentRozvrh_container);
        progressBar = view.findViewById(R.id.MainFragmentRozvrh_progressBar);

        return view;
    }

    /**
     * Zobrazí rozvrh
     * @see #displayRozvrh()
     */
    @Override
    public void onStart() {
        super.onStart();

        displayRozvrh();
    }

    /**
     * Stáhne rozvrh. Pokud stahování selže, pokusí se načíst rozvrh z paměti zařízení.
     * @see StahniRozvrh
     * @see RozvrhConventor
     */
    public void rozvrh() {
        Crashlytics.log(TAG + "Downloading");
        progressBar.setVisibility(View.VISIBLE);
        String[] rozvrhStr = offlineMode.read("rozvrh");

        if (!rozvrhStr[0].equals("ERROR")) {
            RozvrhConventor conventor = new RozvrhConventor();
            Rozvrh rozvrh = conventor.convert(rozvrhStr[0]);
            user.setRozvrh(rozvrh);

            displayRozvrh();
        }

        StahniRozvrh stahniRozvrh = new StahniRozvrh() {
            @Override
            public void onResult(String result) {
                super.onResult(result);
                ResultErrorProcess error = new ResultErrorProcess(context);

                if (error.process(result)) {
                    Crashlytics.log(TAG + "Converting");
                    RozvrhConventor conventor = new RozvrhConventor();
                    Rozvrh rozvrh = conventor.convert(result);

                    offlineMode.write(result, "rozvrh", rozvrh.getDatum());
                    user.setRozvrh(rozvrh);

                    displayRozvrh();
                } else {
                    Crashlytics.log(TAG + "Downloading error: " + error);
                }

                progressBar.setVisibility(View.GONE);
            }
        };

        stahniRozvrh.stahni();
    }

    /**
     * Pokud je rozvrh stažen, zobrazí se, pokud ne, zavolá se {@link #rozvrh()}
     * @see #rozvrh()
     * @see RozvrhAdaper
     * @see Rozvrh
     * @see RozvrhConventor
     */
    public void displayRozvrh() {
        Rozvrh rozvrh = user.getRozvrh();

        if (rozvrh != null) {
            Crashlytics.log(TAG + "Displaying");
            RozvrhAdaper rozvrhAdaper = new RozvrhAdaper(context, rozvrh);
            rozvrhAdaper.adapt(rozvrhLayout);
        } else {
            rozvrh();
        }
    }
}
