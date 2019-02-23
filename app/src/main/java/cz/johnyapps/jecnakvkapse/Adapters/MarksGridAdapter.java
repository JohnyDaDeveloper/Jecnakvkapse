package cz.johnyapps.jecnakvkapse.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Score.Mark;

/**
 * Adapter pro známky - Známky v políčkách s předměty (Podadapter adapteru {@link ScoreRecyclerAdapter})
 */
public class MarksGridAdapter extends BaseAdapter {
    private Context context;

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
        if (convertView == null) convertView = inflater.inflate(R.layout.item_mark, parent, false);
        Mark mark = getItem(position);

        TextView txtMark = convertView.findViewById(R.id.Mark_txtMark);
        txtMark.setText(mark.getValue());
        txtMark.setBackgroundColor(mark.getColor());

        if (mark.isSmall()) {
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
    private void Setup_OnClick(View view, final Mark mark) {
        if (mark.getTitle() != null) {
            if (mark.getTitle().isEmpty()) {
                mark.setTitle("Důvod nenalezen");
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = mark.getValue();

                    if (mark.isSmall()) {
                        title += " Malá";
                    } else {
                        title += " Velká";
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(title)
                            .setMessage(mark.getTitle())
                            .setNegativeButton("Zavřít", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }
            });
        }
    }
}
