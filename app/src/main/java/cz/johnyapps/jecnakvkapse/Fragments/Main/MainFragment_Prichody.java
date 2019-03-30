package cz.johnyapps.jecnakvkapse.Fragments.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;

import cz.johnyapps.jecnakvkapse.Adapters.PrichodyRecyclerAdapter;
import cz.johnyapps.jecnakvkapse.Dialogs.DialogChangePeriod;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.Prichody.Prichody;
import cz.johnyapps.jecnakvkapse.Prichody.PrichodyConvertor;
import cz.johnyapps.jecnakvkapse.Prichody.StahniPrichody;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Singletons.User;

/**
 * Fragment aktivity {@link cz.johnyapps.jecnakvkapse.Activities.MainActivity} pro zobrazování příchodů
 */
public class MainFragment_Prichody extends Fragment implements View.OnClickListener {
    private static final String TAG = "MainFragment_Prichody";
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
        context = getContext();
        user = User.getUser();
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
        View view = inflater.inflate(R.layout.fragment_main_prichody, container, false);
        recyclerView = view.findViewById(R.id.MainFragmentPrichody_recycler);

        FloatingActionButton button = view.findViewById(R.id.MainFragmentPrichody_button);
        button.setOnClickListener(this);

        return view;
    }

    /**
     * Zobrazí příchody
     * @see #displayPrichody()
     */
    @Override
    public void onStart() {
        super.onStart();

        displayPrichody();
    }

    /**
     * Stará se o kliknutí na view
     * @param view  View
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MainFragmentPrichody_button: {
                zmenitObdobi();
                break;
            }

            default: break;
        }
    }

    /**
     * Zobrazí dialog na změnu období
     */
    private void zmenitObdobi() {
        DialogChangePeriod dialogChangePeriod = new DialogChangePeriod(context) {
            @Override
            public void zobrazit(String obdobi) {
                super.zobrazit(obdobi);

                prichody(obdobi);
            }

            @Override
            public void aktualni() {
                super.aktualni();

                prichody(null);
            }
        };
        dialogChangePeriod.getMesice().show();
    }

    /**
     * Stáhne příchody
     * @param obdobi    Období pro které se stáhnou data
     * @see StahniPrichody
     * @see PrichodyConvertor
     */
    private void prichody(@Nullable String obdobi) {
        Crashlytics.log(TAG + "Downloading");

        StahniPrichody stahniPrichody = new StahniPrichody(context) {
            @Override
            public void onResult(String result) {
                super.onResult(result);

                ResultErrorProcess process = new ResultErrorProcess(context);

                if (process.process(result)) {
                    Crashlytics.log(TAG + "Converting");

                    PrichodyConvertor prichodyConvertor = new PrichodyConvertor();
                    Prichody prichody = prichodyConvertor.convert(result);

                    user.setPrichody(prichody);
                    displayPrichody();
                } else {
                    Crashlytics.log(TAG + "Downloading error: " + result);
                }
            }
        };

        stahniPrichody.stahni(obdobi);
    }

    /**
     * Pokud jsou příchody staženy, zobrazí je, pokud ne, stáhne je
     * @see #prichody(String)
     * @see PrichodyRecyclerAdapter
     * @see Prichody
     */
    private void displayPrichody() {
        Prichody prichody = user.getPrichody();

        if (prichody != null) {
            Crashlytics.log(TAG + "Displaying");
            PrichodyRecyclerAdapter adapter = new PrichodyRecyclerAdapter(context, user.getPrichody());
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        } else {
            prichody(null);
        }
    }
}
