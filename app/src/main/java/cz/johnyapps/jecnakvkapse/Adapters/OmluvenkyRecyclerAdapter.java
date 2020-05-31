package cz.johnyapps.jecnakvkapse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.Fragments.OmluvenkyFragment;
import cz.johnyapps.jecnakvkapse.Omluvenky.Omluvenka;
import cz.johnyapps.jecnakvkapse.Omluvenky.Omluvnak;
import cz.johnyapps.jecnakvkapse.R;

/**
 * Adapter pro omluvenky
 * @see OmluvenkyFragment
 */
public class OmluvenkyRecyclerAdapter extends RecyclerView.Adapter<OmluvenkyRecyclerAdapter.CustomViewHolder> {
    private LayoutInflater inflater;

    private ArrayList<Omluvenka> omluvenky;

    /**
     * Inicializace
     * @param context   Context
     * @param omluvnak  Omluvňák
     * @see cz.johnyapps.jecnakvkapse.Singletons.User
     * @see Omluvnak
     */
    public OmluvenkyRecyclerAdapter(Context context, Omluvnak omluvnak) {
        inflater = LayoutInflater.from(context);
        omluvenky = omluvnak.getOmluvenky();
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Omluvenka omluvenka = omluvenky.get(position);

        holder.textDatum.setText(omluvenka.getDatum());
        holder.textPocet.setText(omluvenka.getHodin());
    }

    @Override
    public int getItemCount() {
        return omluvenky.size();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_omluvenka, parent, false);
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
         * @param view  View {@link cz.johnyapps.jecnakvkapse.R.layout#item_omluvenka}
         */
        CustomViewHolder(View view) {
            super(view);

            textDatum = view.findViewById(R.id.Omluvenka_textDatum);
            textPocet = view.findViewById(R.id.Omluvenka_textPocet);
        }
    }
}
