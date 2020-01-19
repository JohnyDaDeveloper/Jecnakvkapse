package cz.johnyapps.jecnakvkapse.Fragments;

import android.content.Context;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
public class MainFragment_Prichody extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainFragment_Prichody";
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
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fragment_main_prichody, container, false);
        recyclerView = view.findViewById(R.id.MainFragmentPrichody_recycler);

        FloatingActionButton button = view.findViewById(R.id.MainFragmentPrichody_button);
        button.setOnClickListener(this);

        swipeLayout = view.findViewById(R.id.MainFragmentPrichody_swipeLayout);
        swipeLayout.setOnRefreshListener(this);

        noItems = inflater.inflate(R.layout.no_items, view, false);
        view.addView(noItems);

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
     * Stará se o obnovení příchdů potažením dolů
     */
    @Override
    public void onRefresh() {
        prichody(null);
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
        Crashlytics.log(Log.INFO, TAG, "Downloading");

        StahniPrichody stahniPrichody = new StahniPrichody() {
            @Override
            public void onResult(String result) {
                super.onResult(result);

                ResultErrorProcess process = new ResultErrorProcess(context);

                if (process.process(result)) {
                    Crashlytics.log(Log.INFO, TAG, "Converting");

                    PrichodyConvertor prichodyConvertor = new PrichodyConvertor();
                    Prichody prichody = prichodyConvertor.convert(result);

                    user.setPrichody(prichody);

                    swipeLayout.setRefreshing(false);

                    displayPrichody();
                } else {
                    Crashlytics.log(Log.INFO, TAG, "Downloading error: " + result);
                }
            }
        };

        swipeLayout.setRefreshing(true);

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
            Crashlytics.log(Log.INFO, TAG, "Displaying");

            if (prichody.getPrichody().size() > 0) {
                noItems.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                PrichodyRecyclerAdapter adapter = new PrichodyRecyclerAdapter(context, user.getPrichody());
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                noItems.setVisibility(View.VISIBLE);

                Toast.makeText(context, R.string.toasts_zadne_prichody, Toast.LENGTH_LONG).show();
            }
        } else {
            prichody(null);
        }
    }
}
