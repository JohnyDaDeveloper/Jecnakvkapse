package cz.johnyapps.jecnakvkapse.Fragments.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import cz.johnyapps.jecnakvkapse.Omluvenky.StahniOmluvenky;
import cz.johnyapps.jecnakvkapse.Adapters.OmluvenkyRecyclerAdapter;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.Omluvenky.OmluvenkyConvertor;
import cz.johnyapps.jecnakvkapse.Omluvenky.Omluvnak;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Singletons.User;

/**
 * Fragment aktivity {@link cz.johnyapps.jecnakvkapse.Activities.MainActivity} pro zobrazování omluvenek
 */
public class MainFragment_Omluvenky extends Fragment {
    private static final String TAG = "MainFragment_Omluvenky";
    private Context context;
    private User user;

    private RecyclerView recyclerView;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_omluvnak, container, false);
        recyclerView = view.findViewById(R.id.MainFragmentOmluvnak_recycler);

        return view;
    }

    /**
     * Zobrazí omluvenky
     * @see #displayOmluvenky()
     */
    @Override
    public void onStart() {
        super.onStart();

        displayOmluvenky();
    }

    /**
     * Stáhne omluvekny
     * @see StahniOmluvenky
     * @see OmluvenkyConvertor
     */
    public void omluvenky() {
        Crashlytics.log(TAG + "Downloading");

        StahniOmluvenky stahniOmluvenky = new StahniOmluvenky(context) {
            @Override
            public void onResult(String result) {
                super.onResult(result);
                ResultErrorProcess process = new ResultErrorProcess(context);

                if (process.process(result)) {
                    Crashlytics.log(TAG + "Converting");
                    OmluvenkyConvertor omluvenkyConvertor = new OmluvenkyConvertor();
                    Omluvnak omluvnak = omluvenkyConvertor.convert(result);

                    user.setOmluvnak(omluvnak);
                    displayOmluvenky();
                } else {
                    Crashlytics.log(TAG + "Downloading error: " + result);
                }
            }
        };

        stahniOmluvenky.stahni();
    }

    /**
     * Pokud jsou omluvenky staženy, zobrazí je, pokud ne, stáhne je
     * @see #omluvenky()
     * @see OmluvenkyRecyclerAdapter
     * @see Omluvnak
     */
    public void displayOmluvenky() {
        Omluvnak omluvnak = user.getOmluvnak();

        if (omluvnak != null) {
            if (omluvnak.getOmluvenky().size() > 0) {
                Crashlytics.log(TAG + "Displaying");
                OmluvenkyRecyclerAdapter adapter = new OmluvenkyRecyclerAdapter(context, omluvnak);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(context, "Žádné omluvenky", Toast.LENGTH_LONG).show();
            }
        } else {
            omluvenky();
        }
    }
}
