package cz.johnyapps.jecnakvkapse.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cz.johnyapps.jecnakvkapse.Adapters.PrichodyRecyclerAdapter;
import cz.johnyapps.jecnakvkapse.Dialogs.DialogChangePeriod;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.HttpConnection.StahniData;
import cz.johnyapps.jecnakvkapse.Prichody.Prichody;
import cz.johnyapps.jecnakvkapse.Prichody.PrichodyConvertor;
import cz.johnyapps.jecnakvkapse.Prichody.StahniPrichody;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Singletons.User;
import cz.johnyapps.jecnakvkapse.Tools.Logger;

/**
 * Fragment aktivity {@link cz.johnyapps.jecnakvkapse.Activities.MainActivity} pro zobrazování příchodů
 */
public class PrichodyFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "PrichodyFragment";
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
        Logger.i(TAG, "Otevírám PrichodyFragment");

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
        if (view.getId() == R.id.MainFragmentPrichody_button) {
            zmenitObdobi();
        }
    }

    /**
     * Stará se o obnovení příchdů potažením dolů
     */
    @Override
    public void onRefresh() {
        stahniPrichody(null);
    }

    /**
     * Zobrazí dialog na změnu období
     */
    private void zmenitObdobi() {
        DialogChangePeriod dialogChangePeriod = new DialogChangePeriod(context) {
            @Override
            public void zobrazit(String obdobi) {
                super.zobrazit(obdobi);

                stahniPrichody(obdobi);
            }

            @Override
            public void aktualni() {
                super.aktualni();

                stahniPrichody(null);
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
    private void stahniPrichody(@Nullable String obdobi) {
        Logger.i(TAG, "stahniPrichody");

        StahniPrichody stahniPrichody = new StahniPrichody();
        stahniPrichody.setOnCompleteListener(new StahniData.OnCompleteListener() {
            @Override
            public void onComplete(String result) {
                ResultErrorProcess process = new ResultErrorProcess(context);

                if (process.process(result)) {
                    Logger.v(TAG, "Příchody staženy");

                    PrichodyConvertor prichodyConvertor = new PrichodyConvertor();
                    Prichody prichody = prichodyConvertor.convert(result);

                    user.setPrichody(prichody);
                    swipeLayout.setRefreshing(false);

                    displayPrichody();
                } else {
                    Logger.w(TAG, "Chyba při stahování: " + result);
                }
            }
        });

        swipeLayout.setRefreshing(true);

        stahniPrichody.stahni(obdobi);
    }

    /**
     * Pokud jsou příchody staženy, zobrazí je, pokud ne, stáhne je
     * @see #stahniPrichody(String)
     * @see PrichodyRecyclerAdapter
     * @see Prichody
     */
    private void displayPrichody() {
        Prichody prichody = user.getPrichody();

        if (prichody != null) {
            Logger.i(TAG, "Zobrazuji příchody");

            if (prichody.getPrichody().size() > 0) {
                noItems.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                PrichodyRecyclerAdapter adapter = new PrichodyRecyclerAdapter(context, user.getPrichody());
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                noItems.setVisibility(View.VISIBLE);
            }
        } else {
            stahniPrichody(null);
        }
    }
}
