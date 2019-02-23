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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.johnyapps.jecnakvkapse.Adapters.PrichodyRecyclerAdapter;
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
    private Context context;
    private Locale locale;
    private User user;

    private RecyclerView recyclerView;

    private String datum;

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
        context = getContext();
        locale = new Locale("cs", "CZ");
        user = User.getUser();
    }

    /**
     * Nastaví layout a načte hlaví views. Spustí nastavení datumu {@link #Setup_Date()}.
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

        Setup_Date();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MainFragmentPrichody_button: {
                changeDate();
                break;
            }

            default: break;
        }
    }

    private void changeDate() {

    }

    /**
     * Nastaví aktuální datum (Slouží pro měnění zobrazeného časového období).
     */
    private void Setup_Date() {
        SimpleDateFormat format = new SimpleDateFormat("MM.yyyy", locale);
        Calendar calendar = Calendar.getInstance();

        datum = format.format(calendar.getTime());
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
     * Stáhne příchody
     * @see StahniPrichody
     * @see PrichodyConvertor
     */
    private void prichody() {
        StahniPrichody stahniPrichody = new StahniPrichody(context) {
            @Override
            public void onResult(String result) {
                super.onResult(result);

                ResultErrorProcess process = new ResultErrorProcess(context);

                if (process.process(result)) {
                    PrichodyConvertor prichodyConvertor = new PrichodyConvertor();
                    Prichody prichody = prichodyConvertor.convert(result);

                    user.setPrichody(prichody);
                    displayPrichody();
                }
            }
        };

        stahniPrichody.stahni();
    }

    /**
     * Pokud jsou příchody staženy, zobrazí je, pokud ne, stáhne je
     * @see #prichody()
     * @see PrichodyRecyclerAdapter
     * @see Prichody
     */
    private void displayPrichody() {
        Prichody prichody = user.getPrichody();

        if (prichody != null) {
            PrichodyRecyclerAdapter adapter = new PrichodyRecyclerAdapter(context, user.getPrichody());
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        } else {
            prichody();
        }
    }
}
