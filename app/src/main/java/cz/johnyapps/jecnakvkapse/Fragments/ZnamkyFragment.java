package cz.johnyapps.jecnakvkapse.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cz.johnyapps.jecnakvkapse.Adapters.ScoreRecyclerAdapter;
import cz.johnyapps.jecnakvkapse.Dialogs.DialogChangePeriod;
import cz.johnyapps.jecnakvkapse.Dialogs.DialogError;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.HttpConnection.StahniData;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Score.Score;
import cz.johnyapps.jecnakvkapse.Score.ScoreConvertor;
import cz.johnyapps.jecnakvkapse.Score.StahniScore;
import cz.johnyapps.jecnakvkapse.Singletons.User;
import cz.johnyapps.jecnakvkapse.Tools.Logger;

/**
 * Fragment aktivity {@link cz.johnyapps.jecnakvkapse.Activities.MainActivity} pro zobrazování odkazů na suplování
 */
public class ZnamkyFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ZnamkyFragment";
    private static final String ZOBRAZOVANE_OBDOBI = "obdobi";

    private Context context;
    private User user;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;

    private String zobrazovaneObdobi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.i(TAG, "Otevírám ZnamkyFragment");

        super.onCreate(savedInstanceState);
        initialize(savedInstanceState);
    }

    private void initialize(Bundle savedInstanceState) {
        this.context = getContext();
        this.user = User.getUser();

        if (savedInstanceState != null) {
            zobrazovaneObdobi = savedInstanceState.getString(ZOBRAZOVANE_OBDOBI, null);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(ZOBRAZOVANE_OBDOBI, zobrazovaneObdobi);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_znamky, container, false);
        recyclerView = view.findViewById(R.id.MainFragmentZnamky_recycler);

        FloatingActionButton button = view.findViewById(R.id.MainFragmentZnamky_button);
        button.setOnClickListener(this);

        swipeLayout = view.findViewById(R.id.MainFragmentZnamky_swipelayout);
        swipeLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        displayMarks();
    }

    /**
     * Stará se o kliknutí na view
     * @param view  View
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.MainFragmentZnamky_button) {
            zmenitObdobi();
        }
    }

    /**
     * Stará se o obnovení známek potažením dolů
     */
    @Override
    public void onRefresh() {
        if (user.isDummy()) {
            displayMarks();
        } else {
            swipeLayout.setRefreshing(false);
            stahniMarks(null);
        }
    }

    /**
     * Zobrazí dialog na změnu období
     */
    private void zmenitObdobi() {
        DialogChangePeriod dialogChangePeriod = new DialogChangePeriod(context);
        dialogChangePeriod.setOnZmenObdobiListener(new DialogChangePeriod.OnZmenObdobiListener() {
            @Override
            public void zobrazitObdobi(String obdobi) {
                stahniMarks(obdobi);
            }

            @Override
            public void zobrazitAktualni() {
                stahniMarks();
            }
        });
        dialogChangePeriod.getPololeti(zobrazovaneObdobi).show();
    }

    /**
     * Stáhne známky
     * @see #stahniMarks(String)
     */
    private void stahniMarks() {
        stahniMarks(null);
    }

    /**
     * Stáhne známky
     * @param obdobi    Období pro které se stáhnou data
     * @see StahniScore
     * @see ScoreConvertor
     */
    public void stahniMarks(@Nullable String obdobi) {
        Logger.i(TAG, "stahniMarks");
        String sessionId = user.getSessionId();

        if (sessionId != null) {
            StahniScore stahniScore = new StahniScore();
            stahniScore.setOnCompleteListener(new StahniData.OnCompleteListener() {
                @Override
                public void onComplete(String result) {
                    ResultErrorProcess error = new ResultErrorProcess(context);

                    if (error.process(result)) {
                        zobrazovaneObdobi = obdobi;
                        Logger.v(TAG, "Známky staženy");
                        convertScore(result);
                        swipeLayout.setRefreshing(false);
                    } else {
                        Logger.w(TAG, "Chyba při stahování: " + result);
                    }
                }
            });

            swipeLayout.setRefreshing(true);
            stahniScore.stahni(obdobi);
        }
    }

    private void convertScore(String result) {
        ScoreConvertor scoreConvertor = new ScoreConvertor();
        scoreConvertor.setOnCompleteListener(new ScoreConvertor.OnCompleteListener() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(Score score) {
                user.setScore(score);
                displayMarks();
            }

            @Override
            public void onFailure(String message) {
                showConvertFailMessage(message);
            }
        });
        scoreConvertor.convert(result);
    }

    private void showConvertFailMessage(String message) {
        Logger.e(TAG, "showConvertFailMessage: " + message);
        DialogError dialogError = new DialogError(context);

        if (message.equals(ScoreConvertor.ERROR_TABLE_NOT_FOUND)) {
            dialogError.get(R.string.error_no_score_table).show();
        } else {
            dialogError.get(R.string.error_score_convert);
        }
    }

    /**
     * Pokud jsou známky staženy, zobrazí je, pokud ne, stáhne je
     * @see #stahniMarks(String)
     * @see ScoreRecyclerAdapter
     * @see Score
     */
    public void displayMarks() {
        Score score = user.getScore();

        if (score != null) {
            Logger.i(TAG, "Zobrazuji známky");
            ScoreRecyclerAdapter adapter = new ScoreRecyclerAdapter(context, score);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        } else {
            stahniMarks(null);
        }
    }
}
