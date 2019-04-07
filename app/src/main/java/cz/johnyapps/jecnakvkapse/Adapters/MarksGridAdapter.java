package cz.johnyapps.jecnakvkapse.Adapters;

import android.content.SharedPreferences;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.Dialogs.DialogMarkBuilder;
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
        this.prefs = context.getSharedPreferences("jecnakvkapse", MODE_PRIVATE);

        this.marks = marks;
        this.inflater = (LayoutInflater.from(context));
    }

    /**
     * Vrací známku na pozici
     * @param position  Pozice
     * @return          Známka
     */
    @Override
    public Mark getItem(int position) {
        return marks.get(position);
    }

    /**
     * Vrací id známky na pozici
     * @param position  Pozice
     * @return          Pozice (bere se jako id)
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Vrací velikost ArrayListu se známkami
     * @return  Velikost
     */
    @Override
    public int getCount() {
        return marks.size();
    }

    /**
     * Vrátí view na pozici
     * @param position      Pozice
     * @param convertView   Předchozí view (Pokud není prázdné je zbytečné ho předělávat, stačí změnit data)
     * @param parent        Paret
     * @return              View s odpovídajícími daty
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = inflater.inflate(R.layout.item_score_mark, parent, false);
        Mark mark = getItem(position);

        TextView txtMark = convertView.findViewById(R.id.Mark_txtMark);
        txtMark.setText(mark.getValue());
        txtMark.setBackgroundColor(mark.getColor());

        if (mark.isSmall() && mark.rozlisovatVelikost()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) txtMark.getLayoutParams();
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            txtMark.setLayoutParams(params);
        }

        Setup_OnClick(convertView, mark);

        return convertView;
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
                    boolean pink = prefs.getBoolean("enable_pink", false);

                    if (mark.getValue().equals("5")) {
                        if (pink) {
                            prefs.edit().putBoolean("enable_pink", false).apply();
                            Toast.makeText(context, "Růžové téma zakázáno", Toast.LENGTH_LONG).show();
                        } else {
                            prefs.edit().putBoolean("enable_pink", true).apply();
                            Toast.makeText(context, "Růžové téma povoleno", Toast.LENGTH_LONG).show();
                        }
                    }

                    return false;
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Mark mark = (Mark) v.getTag();
                    String title = mark.getValue();

                    if (mark.isSmall() && mark.rozlisovatVelikost()) {
                        title += " Malá";
                    } else if (mark.rozlisovatVelikost()) {
                        title += " Velká";
                    }

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
    }
}
