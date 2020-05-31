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

import cz.johnyapps.jecnakvkapse.Adapters.OmluvenkyRecyclerAdapter;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.HttpConnection.StahniData;
import cz.johnyapps.jecnakvkapse.Omluvenky.OmluvenkyConvertor;
import cz.johnyapps.jecnakvkapse.Omluvenky.Omluvnak;
import cz.johnyapps.jecnakvkapse.Omluvenky.StahniOmluvenky;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Singletons.User;
import cz.johnyapps.jecnakvkapse.Tools.Logger;

/**
 * Fragment aktivity {@link cz.johnyapps.jecnakvkapse.Activities.MainActivity} pro zobrazování omluvenek
 */
public class OmluvenkyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "OmluvenkyFragment";
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
        Logger.i(TAG, "Otevírám OmluvenkyFragment");

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
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fragment_main_omluvnak, container, false);
        recyclerView = view.findViewById(R.id.MainFragmentOmluvnak_recycler);

        swipeLayout = view.findViewById(R.id.MainFragmentOmluvnak_swipelayout);
        swipeLayout.setOnRefreshListener(this);

        noItems = inflater.inflate(R.layout.no_items, view, false);
        view.addView(noItems);

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
     * Stará se o obnovení omluvenek potažením dolů
     */
    @Override
    public void onRefresh() {
        stahniOmluvenky();
    }

    /**
     * Stáhne omluvekny
     * @see StahniOmluvenky
     * @see OmluvenkyConvertor
     */
    public void stahniOmluvenky() {
        Logger.i(TAG, "stahniOmluvenky");

        StahniOmluvenky stahniOmluvenky = new StahniOmluvenky();
        stahniOmluvenky.setOnCompleteListener(new StahniData.OnCompleteListener() {
            @Override
            public void onComplete(String result) {
                ResultErrorProcess process = new ResultErrorProcess(context);

                if (process.process(result)) {
                    Logger.v(TAG, "Omluvenky staženy");

                    OmluvenkyConvertor omluvenkyConvertor = new OmluvenkyConvertor();
                    Omluvnak omluvnak = omluvenkyConvertor.convert(result);

                    user.setOmluvnak(omluvnak);
                    swipeLayout.setRefreshing(false);

                    displayOmluvenky();
                } else {
                    Logger.v(TAG, "Chyba při stahování: " + result);
                }
            }
        });

        swipeLayout.setRefreshing(true);

        stahniOmluvenky.stahni();
    }

    /**
     * Pokud jsou omluvenky staženy, zobrazí je, pokud ne, stáhne je
     * @see #stahniOmluvenky()
     * @see OmluvenkyRecyclerAdapter
     * @see Omluvnak
     */
    private void displayOmluvenky() {
        Omluvnak omluvnak = user.getOmluvnak();

        if (omluvnak != null) {
            if (omluvnak.getOmluvenky().size() > 0) {
                Logger.v(TAG, "Zobrazuji omluvenky");

                noItems.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                OmluvenkyRecyclerAdapter adapter = new OmluvenkyRecyclerAdapter(context, omluvnak);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                noItems.setVisibility(View.VISIBLE);
            }
        } else {
            stahniOmluvenky();
        }
    }
}
