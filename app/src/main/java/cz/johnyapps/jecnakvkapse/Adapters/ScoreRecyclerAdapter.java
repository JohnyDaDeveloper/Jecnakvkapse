package cz.johnyapps.jecnakvkapse.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.CustomViews.ZnamkyGridView;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Score.Score;
import cz.johnyapps.jecnakvkapse.Score.Subject;

/**
 * Adapter pro známky - Předměty
 * @see MarksGridAdapter
 * @see cz.johnyapps.jecnakvkapse.Fragments.Main.MainFragment_Znamky
 */
public class ScoreRecyclerAdapter extends RecyclerView.Adapter {
    private ArrayList<Subject> subjects;

    private Context context;
    private LayoutInflater inflater;

    /**
     * Inicializace
     * @param context   Context
     * @param score     Skóre (Známky v předmětech)
     * @see Score
     * @see cz.johnyapps.jecnakvkapse.Singletons.User
     */
    public ScoreRecyclerAdapter(Context context, Score score) {
        this.subjects = score.geSubjects();

        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * Vrátí počet předmětů
     * @return  Počet
     */
    @Override
    public int getItemCount() {
        return subjects.size();
    }

    /**
     * Vrátí předmět na pozici
     * @param position  Pozice
     * @return          Pŕedmět
     * @see Subject
     */
    private Subject getItem(int position) {
        return subjects.get(position);
    }

    /**
     * Při zobrazení view načte příslušný předmět
     * @param holder    ViewHolder
     * @param position  Pozice
     * @see CustomViewHolder
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Subject subject = getItem(position);

        CustomViewHolder customHolder = (CustomViewHolder) holder;

        customHolder.textView.setText(subject.getName() + " " + subject.getZaverecna());

        MarksGridAdapter adapter = new MarksGridAdapter(context, subject.getMarks());
        customHolder.gridView.setAdapter(adapter);
    }

    /**
     * Vytvoří ViewHolder
     * @param parent    Parent
     * @param viewType  View type
     * @return          ViewHolder
     * @see CustomViewHolder
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_score, parent, false);
        return new CustomViewHolder(itemView);
    }

    /**
     * Vlastní view holder
     */
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ZnamkyGridView gridView;

        /**
         * Inicializace
         * @param view  View {@link cz.johnyapps.jecnakvkapse.R.layout#item_score}
         */
        CustomViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.Line_textView);
            gridView = view.findViewById(R.id.Line_grid);
        }
    }
}
