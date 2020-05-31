package cz.johnyapps.jecnakvkapse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.Fragments.PrichodyFragment;
import cz.johnyapps.jecnakvkapse.Prichody.Prichod;
import cz.johnyapps.jecnakvkapse.Prichody.Prichody;
import cz.johnyapps.jecnakvkapse.R;

/**
 * Adapter pro příchody
 * @see PrichodyFragment
 */
public class PrichodyRecyclerAdapter extends RecyclerView.Adapter<PrichodyRecyclerAdapter.CustomViewHolder> {
    private LayoutInflater inflater;

    private ArrayList<Prichod> prichody;

    /**
     * Inicializace
     * @param context   Context
     * @param prichody  Příchody
     * @see cz.johnyapps.jecnakvkapse.Singletons.User
     * @see Prichody
     */
    public PrichodyRecyclerAdapter(Context context, Prichody prichody) {
        inflater = LayoutInflater.from(context);

        this.prichody = prichody.getPrichody();
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Prichod prichod = prichody.get(position);

        holder.textDatum.setText(prichod.getDatum());
        holder.textPocet.setText(convertCasy(prichod.getCasy()));
    }

    /**
     * Konvertuje časy příchodů a odchdů na String
     * @param casy  Časy
     * @return      Časy ve Stringu
     * @see Prichod
     */
    private String convertCasy(ArrayList<String> casy) {
        StringBuilder strCasy = new StringBuilder();

        for (int i = 0; i < casy.size(); i++) {
            if (i != 0) strCasy.append("\n");

            strCasy.append(casy.get(i));
        }

        return strCasy.toString();
    }

    @Override
    public int getItemCount() {
        return prichody.size();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_prichod, parent, false);
        return new CustomViewHolder(view);
    }

    /**
     * Vlastní view holder
     */
    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textDatum;
        TextView textPocet;

        /**
         * Inicializace
         * @param view  View {@link cz.johnyapps.jecnakvkapse.R.layout#item_prichod}
         */
        CustomViewHolder(View view) {
            super(view);

            textDatum = view.findViewById(R.id.Prichod_textDatum);
            textPocet = view.findViewById(R.id.Prichod_textCas);
        }
    }
}
