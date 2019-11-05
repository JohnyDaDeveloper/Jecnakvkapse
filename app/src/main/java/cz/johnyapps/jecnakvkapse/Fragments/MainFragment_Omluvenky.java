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

import cz.johnyapps.jecnakvkapse.Adapters.OmluvenkyRecyclerAdapter;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.Omluvenky.OmluvenkyConvertor;
import cz.johnyapps.jecnakvkapse.Omluvenky.Omluvnak;
import cz.johnyapps.jecnakvkapse.Omluvenky.StahniOmluvenky;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Singletons.User;

/**
 * Fragment aktivity {@link cz.johnyapps.jecnakvkapse.Activities.MainActivity} pro zobrazování omluvenek
 */
public class MainFragment_Omluvenky extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainFragment_Omluvenky";
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
        omluvenky();
    }

    /**
     * Stáhne omluvekny
     * @see StahniOmluvenky
     * @see OmluvenkyConvertor
     */
    public void omluvenky() {
        Crashlytics.log(Log.INFO, TAG, "Downloading");

        StahniOmluvenky stahniOmluvenky = new StahniOmluvenky() {
            @Override
            public void onResult(String result) {
                super.onResult(result);
                ResultErrorProcess process = new ResultErrorProcess(context);

                if (process.process(result)) {
                    Crashlytics.log(Log.INFO, TAG, "Converting");
                    OmluvenkyConvertor omluvenkyConvertor = new OmluvenkyConvertor();
                    Omluvnak omluvnak = omluvenkyConvertor.convert(result);

                    user.setOmluvnak(omluvnak);

                    swipeLayout.setRefreshing(false);

                    displayOmluvenky();
                } else {
                    Crashlytics.log(Log.INFO, TAG, "Downloading error: " + result);
                }
            }
        };

        swipeLayout.setRefreshing(true);

        stahniOmluvenky.stahni();
    }

    /**
     * Pokud jsou omluvenky staženy, zobrazí je, pokud ne, stáhne je
     * @see #omluvenky()
     * @see OmluvenkyRecyclerAdapter
     * @see Omluvnak
     */
    private void displayOmluvenky() {
        Omluvnak omluvnak = user.getOmluvnak();

        if (omluvnak != null) {
            if (omluvnak.getOmluvenky().size() > 0) {
                Crashlytics.log(Log.INFO, TAG, "Displaying");

                noItems.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                OmluvenkyRecyclerAdapter adapter = new OmluvenkyRecyclerAdapter(context, omluvnak);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                noItems.setVisibility(View.VISIBLE);

                Toast.makeText(context, R.string.toasts_zadne_omluvenky, Toast.LENGTH_LONG).show();
            }
        } else {
            omluvenky();
        }
    }
}
