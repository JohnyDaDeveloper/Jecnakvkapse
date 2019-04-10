package cz.johnyapps.jecnakvkapse.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.Omluvenky.Omluvenka;
import cz.johnyapps.jecnakvkapse.Omluvenky.Omluvnak;
import cz.johnyapps.jecnakvkapse.R;

/**
 * Adapter pro omluvenky
 * @see cz.johnyapps.jecnakvkapse.Fragments.MainFragment_Omluvenky
 */
public class OmluvenkyRecyclerAdapter extends RecyclerView.Adapter {
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

    /**
     * Při zobrazení view načte příslušnou omluvenku
     * @param holder    Holder
     * @param position  Pozice
     * @see CustomViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomViewHolder customHolder = (CustomViewHolder) holder;
        Omluvenka omluvenka = omluvenky.get(position);

        customHolder.textDatum.setText(omluvenka.getDatum());
        customHolder.textPocet.setText(omluvenka.getHodin());
    }

    /**
     * Vrátí počet omluvenek
     * @return  Počet
     */
    @Override
    public int getItemCount() {
        return omluvenky.size();
    }

    /**
     * Vytvoří ViewHolder
     * @param parent    Parent
     * @param viewType  Typ view
     * @return          ViewHolder
     * @see CustomViewHolder
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_omluvenka, parent, false);
        return new CustomViewHolder(view);
    }

    /**
     * Vlastní view holder
     */
    public class CustomViewHolder extends RecyclerView.ViewHolder {
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
