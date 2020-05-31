package cz.johnyapps.jecnakvkapse.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.Dialogs.DialogMarkBuilder;
import cz.johnyapps.jecnakvkapse.PrefsNames;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Score.Mark;

import static android.content.Context.MODE_PRIVATE;

/**
 * Adapter pro známky - Známky v políčkách s předměty (Podadapter adapteru {@link ScoreRecyclerAdapter})
 */
public class MarksGridAdapter extends BaseAdapter {
    private Context context;
    private SharedPreferences prefs;

    private ArrayList<Mark> marks;
    private LayoutInflater inflater;

    /**
     * Inicializace
     * @param context   Context
     * @param marks     ArrayList se známkami
     * @see cz.johnyapps.jecnakvkapse.Singletons.User
     */
    MarksGridAdapter(Context context, ArrayList<Mark> marks) {
        this.context = context;
        this.prefs = context.getSharedPreferences(PrefsNames.PREFS_NAME, MODE_PRIVATE);

        this.marks = marks;
        this.inflater = (LayoutInflater.from(context));
    }
    @Override
    public Mark getItem(int position) {
        return marks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return marks.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Mark mark = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_score_mark, parent, false);
        }

        loadMarkView(convertView, mark);

        Setup_OnClick(convertView, mark);
        return convertView;
    }

    /**
     * Načte mark view
     * @param convertView   convertView
     * @param mark          mark
     */
    private void loadMarkView(View convertView, Mark mark) {
        TextView txtMark = convertView.findViewById(R.id.Mark_txtMark);
        txtMark.setText(mark.getValue());
        txtMark.setBackgroundColor(mark.getColor());

        if (mark.isSmall() && mark.rozlisovatVelikost()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) txtMark.getLayoutParams();
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            txtMark.setLayoutParams(params);
        }
    }

    /**
     * Nastavuje onClick známkám (Zobrazí dialog s dalšími informacemi)
     * @param view  View se známkou
     * @param mark  Známka
     */
    private void Setup_OnClick(View view, Mark mark) {
        if (mark.getTitle() != null) {
            if (mark.getTitle().isEmpty()) {
                mark.setTitle("Důvod nenalezen");
            }

            view.setTag(mark);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Mark mark = (Mark) v.getTag();

                    switch (mark.getValue()) {
                        case "5":
                            handlePinkThemeChange();
                            break;
                        case "DT":
                        case "DŘ": {
                            handleCodeRedChange();
                            break;
                        }
                    }

                    return false;
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMarkDialog((Mark) v.getTag());
                }
            });
        }
    }

    private void showMarkDialog(Mark mark) {
        String title = mark.getValue() + " ";

        if (mark.isSmall() && mark.rozlisovatVelikost()) {
            title += context.getString(R.string.mala);
        } else if (mark.rozlisovatVelikost()) {
            title += context.getString(R.string.velka);
        }

        DialogMarkBuilder builder = new DialogMarkBuilder(context);
        builder.setTitle(title)
                .setHeaderColor(mark.getColor())
                .setNegativeButton(R.string.zavrit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setMessage(mark.getTitle())
                .create().show();
    }

    private void handlePinkThemeChange() {
        boolean pink = prefs.getBoolean(PrefsNames.ENABLE_PINK, false);

        if (pink) {
            prefs.edit().putBoolean(PrefsNames.ENABLE_PINK, false).apply();
            Toast.makeText(context, R.string.theme_pink_disabled, Toast.LENGTH_LONG).show();
        } else {
            prefs.edit().putBoolean(PrefsNames.ENABLE_PINK, true).apply();
            Toast.makeText(context, R.string.theme_pink_enabled, Toast.LENGTH_LONG).show();
        }
    }

    private void handleCodeRedChange() {
        boolean red = prefs.getBoolean(PrefsNames.ENABLE_CODE_RED, false);

        if (red) {
            prefs.edit().putBoolean(PrefsNames.ENABLE_CODE_RED, false).apply();
            Toast.makeText(context, R.string.theme_code_red_disabled, Toast.LENGTH_LONG).show();
        } else {
            prefs.edit().putBoolean(PrefsNames.ENABLE_CODE_RED, true).apply();
            Toast.makeText(context, R.string.theme_code_red_enabled, Toast.LENGTH_LONG).show();
        }
    }
}
