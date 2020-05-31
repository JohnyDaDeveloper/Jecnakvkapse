package cz.johnyapps.jecnakvkapse.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.crashlytics.android.Crashlytics;

import cz.johnyapps.jecnakvkapse.Adapters.RozvrhAdaper;
import cz.johnyapps.jecnakvkapse.Dialogs.DialogError;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.HttpConnection.StahniData;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Receivers.NetworkState;
import cz.johnyapps.jecnakvkapse.Rozvrh.Rozvrh;
import cz.johnyapps.jecnakvkapse.Rozvrh.RozvrhConventor;
import cz.johnyapps.jecnakvkapse.Rozvrh.StahniRozvrh;
import cz.johnyapps.jecnakvkapse.Singletons.User;
import cz.johnyapps.jecnakvkapse.Tools.OfflineMode;

/**
 * Fragment aktivity {@link cz.johnyapps.jecnakvkapse.Activities.MainActivity} pro zobrazování rozvrhu
 */
public class RozvrhFragment extends Fragment {
    private static final String TAG = "MainFragment_Rozvrh";
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
        Crashlytics.log(Log.INFO, TAG, "Downloading");
        String[] rozvrhStr = offlineMode.read("rozvrh");

        NetworkState networkState = new NetworkState();

        if (!rozvrhStr[0].equals("ERROR")) {
            RozvrhConventor conventor = new RozvrhConventor();
            Rozvrh rozvrh = conventor.convert(rozvrhStr[0]);
            user.setRozvrh(rozvrh);

            displayRozvrh();
        } else if (!networkState.isConnected(context)) {
            DialogError dialogError = new DialogError(context);
            dialogError.get("Nenalezen žádný uložený rozvrh").show();
        }

        if (networkState.isConnected(context)) {
            progressBar.setVisibility(View.VISIBLE);

            StahniRozvrh stahniRozvrh = new StahniRozvrh();
            stahniRozvrh.setOnCompleteListener(new StahniData.OnCompleteListener() {
                @Override
                public void onComplete(String result) {
                    ResultErrorProcess error = new ResultErrorProcess(context);

                    if (error.process(result)) {
                        Crashlytics.log(Log.INFO, TAG, "Converting");
                        RozvrhConventor conventor = new RozvrhConventor();
                        Rozvrh rozvrh = conventor.convert(result);

                        offlineMode.write(result, "rozvrh", rozvrh.getDatum());
                        user.setRozvrh(rozvrh);

                        displayRozvrh();
                    } else {
                        Crashlytics.log(Log.INFO, TAG, "Downloading error: " + error);
                    }

                    progressBar.setVisibility(View.GONE);
                }
            });
            stahniRozvrh.stahni();
        }
    }

    /**
     * Pokud je rozvrh stažen, zobrazí se, pokud ne, zavolá se {@link #rozvrh()}
     * @see #rozvrh()
     * @see RozvrhAdaper
     * @see Rozvrh
     * @see RozvrhConventor
     */
    private void displayRozvrh() {
        Rozvrh rozvrh = user.getRozvrh();

        if (rozvrh != null) {
            Crashlytics.log(Log.INFO, TAG, "Displaying");
            RozvrhAdaper rozvrhAdaper = new RozvrhAdaper(context, rozvrh);
            rozvrhAdaper.adapt(rozvrhLayout);
        } else {
            Crashlytics.log(Log.INFO, TAG, "No data");
            rozvrh();
        }
    }
}
