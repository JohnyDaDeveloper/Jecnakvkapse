package cz.johnyapps.jecnakvkapse.Fragments.Main;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.johnyapps.jecnakvkapse.Score.StahniScore;
import cz.johnyapps.jecnakvkapse.Adapters.ScoreRecyclerAdapter;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Score.MarkConvertor;
import cz.johnyapps.jecnakvkapse.Score.Score;
import cz.johnyapps.jecnakvkapse.Singletons.User;
import cz.johnyapps.jecnakvkapse.Tools.OfflineMode;

/**
 * Fragment aktivity {@link cz.johnyapps.jecnakvkapse.Activities.MainActivity} pro zobrazování odkazů na suplování
 */
public class MainFragment_Znamky extends Fragment {
    private Context context;
    private User user;
    private OfflineMode offlineMode;

    private RecyclerView recyclerView;

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
        this.context = getContext();
        this.user = User.getUser();
        this.offlineMode = new OfflineMode(context);
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_znamky, container, false);
        recyclerView = view.findViewById(R.id.MainFragmentZnamky_recycler);

        return view;
    }

    /**
     * Zobrazí odkazy na suplování
     * @see #displayMarks()
     */
    @Override
    public void onStart() {
        super.onStart();
        displayMarks();
    }

    /**
     * Stáhne známky
     * @see StahniScore
     * @see MarkConvertor
     */
    public void marks() {
        String sessionId = user.getSessionId();

        if (sessionId != null) {
            StahniScore stahniScore = new StahniScore(context) {
                @Override
                public void onResult(String result) {
                    super.onResult(result);
                    ResultErrorProcess error = new ResultErrorProcess(context);

                    if (error.process(result)) {
                        MarkConvertor markConvertor = new MarkConvertor();
                        Score score = markConvertor.convert(result);

                        offlineMode.write(result, "znamky", score.getDatum());
                        user.setScore(score);

                        displayMarks();
                    }
                }
            };

            stahniScore.stahni();
        }
    }

    /**
     * Pokud jsou známky staženy, zobrazí je, pokud ne, stáhne je
     * @see #marks()
     * @see ScoreRecyclerAdapter
     * @see Score
     */
    public void displayMarks() {
        Score score = user.getScore();

        if (score != null) {
            ScoreRecyclerAdapter adapter = new ScoreRecyclerAdapter(context, score);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        } else {
            marks();
        }
    }
}
