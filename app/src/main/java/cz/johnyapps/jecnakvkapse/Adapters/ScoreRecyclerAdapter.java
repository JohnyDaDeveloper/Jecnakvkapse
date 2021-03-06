package cz.johnyapps.jecnakvkapse.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.CustomViews.ZnamkyGridView;
import cz.johnyapps.jecnakvkapse.Dialogs.DialogMarkBuilder;
import cz.johnyapps.jecnakvkapse.Fragments.ZnamkyFragment;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Score.Mark;
import cz.johnyapps.jecnakvkapse.Score.Score;
import cz.johnyapps.jecnakvkapse.Score.Subject;

/**
 * Adapter pro známky - Předměty
 * @see MarksGridAdapter
 * @see ZnamkyFragment
 */
public class ScoreRecyclerAdapter extends RecyclerView.Adapter<ScoreRecyclerAdapter.CustomViewHolder> {
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

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    private Subject getItem(int position) {
        return subjects.get(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Subject subject = getItem(position);
        holder.textView.setText(subject.getName());

        MarksGridAdapter adapter = new MarksGridAdapter(context, subject.getMarks());
        holder.gridView.setAdapter(adapter);

        Setup_zaverecna(holder, subject.getZaverecna());
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
            View view = loadMarkView(customHolder.layout_bottom, mark);

            Setup_OnClick(view, mark);

            customHolder.layout_bottom.removeAllViews();
            customHolder.layout_bottom.addView(view);
            customHolder.layout_bottom.setVisibility(View.VISIBLE);
        } else {
            customHolder.layout_bottom.setVisibility(View.GONE);
        }
    }

    private View loadMarkView(ViewGroup parent, Mark mark) {
        View view = inflater.inflate(R.layout.item_score_mark, parent, false);

        TextView zaverecna = view.findViewById(R.id.Mark_txtMark);
        zaverecna.setText(mark.getValue());
        zaverecna.setBackgroundColor(mark.getColor());

        return view;
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
    public static class CustomViewHolder extends RecyclerView.ViewHolder {
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
