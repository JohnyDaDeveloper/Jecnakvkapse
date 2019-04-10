package cz.johnyapps.jecnakvkapse.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.CustomViews.ZnamkyGridView;
import cz.johnyapps.jecnakvkapse.Dialogs.DialogMarkBuilder;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Score.Mark;
import cz.johnyapps.jecnakvkapse.Score.Score;
import cz.johnyapps.jecnakvkapse.Score.Subject;

/**
 * Adapter pro známky - Předměty
 * @see MarksGridAdapter
 * @see cz.johnyapps.jecnakvkapse.Fragments.MainFragment_Znamky
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

        customHolder.textView.setText(subject.getName());

        MarksGridAdapter adapter = new MarksGridAdapter(context, subject.getMarks());
        customHolder.gridView.setAdapter(adapter);

        Setup_zaverecna(customHolder, subject.getZaverecna());
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
     * Nastaví závěrečnu známku
     * @param customHolder  ViewHolder
     * @param mark          Závěrečná známka
     */
    private void Setup_zaverecna(CustomViewHolder customHolder, Mark mark) {
        if (mark != null) {
            View view = inflater.inflate(R.layout.item_score_mark, customHolder.layout_bottom, false);

            TextView zaverecna = view.findViewById(R.id.Mark_txtMark);
            zaverecna.setText(mark.getValue());
            zaverecna.setBackgroundColor(mark.getColor());

            Setup_OnClick(view, mark);

            customHolder.layout_bottom.removeAllViews();
            customHolder.layout_bottom.addView(view);
            customHolder.layout_bottom.setVisibility(View.VISIBLE);
        } else {
            customHolder.layout_bottom.setVisibility(View.GONE);
        }
    }

    /**
     * Nastavuje onClick známkám (Zobrazí dialog s dalšími informacemi)
     * @param view  View se známkou
     * @param mark  Známka
     */
    private void Setup_OnClick(View view, Mark mark) {
        view.setTag(mark);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mark mark = (Mark) v.getTag();
                String title = mark.getValue();

                title += " Závěrečná";

                DialogMarkBuilder builder = new DialogMarkBuilder(context);
                builder.setTitle(title)
                        .setHeaderColor(mark.getColor())
                        .setNegativeButton("Zavřít", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setMessage(mark.getTitle())
                        .create().show();
            }
        });
    }

    /**
     * Vlastní view holder
     */
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ZnamkyGridView gridView;
        LinearLayout layout_bottom;

        /**
         * Inicializace
         * @param view  View {@link cz.johnyapps.jecnakvkapse.R.layout#item_score}
         */
        CustomViewHolder(View view) {
            super(view);

            textView = view.findViewById(R.id.Line_textView);
            gridView = view.findViewById(R.id.Line_grid);
            layout_bottom = view.findViewById(R.id.Line_zaverecna);
        }
    }
}
