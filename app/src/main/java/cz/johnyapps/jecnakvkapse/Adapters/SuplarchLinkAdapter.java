package cz.johnyapps.jecnakvkapse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.Fragments.SuplarchFragment;
import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Suplarch.StahniSuplarch;
import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky.SuplarchLink;
import cz.johnyapps.jecnakvkapse.Tools.Logger;

/**
 * Adapter pro odkazy na stažení Suplarchu
 * @see SuplarchFragment
 */
public class SuplarchLinkAdapter extends RecyclerView.Adapter<SuplarchLinkAdapter.CustomViewHolder> {
    private static final String TAG = "SuplarchLinkAdapter: ";

    private Context context;
    private LayoutInflater inflater;

    private ArrayList<SuplarchLink> links;

    /**
     * Inicializace
     * @param context   Context
     * @param links     ArrayList s odkazy
     */
    public SuplarchLinkAdapter(Context context, ArrayList<SuplarchLink> links) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        this.links = links;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        SuplarchLink link = links.get(position);

        holder.setTitle(link.getNazev());
        holder.setLink(link.getDocName());
        holder.setTag(link);
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stahniSuplarch((SuplarchLink) v.getTag());
            }
        });
    }

    /**
     * Spustí stahování Suplarchu
     * @param link  SuplarchLink
     * @see SuplarchLink
     * @see StahniSuplarch
     */
    private void stahniSuplarch(final SuplarchLink link) {
        Logger.i(TAG, "stahniSuplarch");
        StahniSuplarch stahniSuplarch = new StahniSuplarch(context);
        stahniSuplarch.stahni(link);
    }

    @Override
    public int getItemCount() {
        return links.size();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_suplarch_link, parent, false);
        return new CustomViewHolder(view);
    }

    /**
     * Vlastní ViewHolder
     */
    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        private View view;

        private TextView title;
        private TextView link;

        /**
         * Inicializace
         * @param view  View {@link cz.johnyapps.jecnakvkapse.R.layout#item_suplarch_link}
         */
        CustomViewHolder(View view) {
            super(view);

            this.view = view;

            title = view.findViewById(R.id.SuplarchLink_title);
            link = view.findViewById(R.id.SuplarchLink_link);
        }

        /**
         * Nastaví nadpis
         * @param title Nadpis
         */
        void setTitle(String title) {
            this.title.setText(title);
        }

        /**
         * Nastaví odkaz/link
         * @param link  Odkaz/link
         */
        void setLink(String link) {
            this.link.setText(link);
        }

        /**
         * Nastaví onClickListener (ten pak spoští stahování {@link #stahniSuplarch(SuplarchLink)})
         * @param listener  Listener
         */
        void setOnClickListener(View.OnClickListener listener) {
            view.setOnClickListener(listener);
        }

        /**
         * Nastaví příslušný SuplarchLink jako tag
         * @param tag   {@link cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky.SuplarchLink}
         */
        void setTag(SuplarchLink tag) {
            view.setTag(tag);
        }
    }
}
