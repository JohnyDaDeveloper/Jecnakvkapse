package cz.johnyapps.jecnakvkapse.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Suplarch.StahniSuplarch;
import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky.SuplarchLink;

/**
 * Adapter pro odkazy na stažení Suplarchu
 * @see cz.johnyapps.jecnakvkapse.Fragments.MainFragment_Suplarch
 */
public class SuplarchLinkAdapter extends RecyclerView.Adapter {
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

    /**
     * Při zobrazení view načte příslušný link
     * @param holder    ViewHolder
     * @param position  Pozice
     * @see CustomViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomViewHolder customHolder = (CustomViewHolder) holder;
        SuplarchLink link = links.get(position);

        customHolder.setTitle(link.getNazev());
        customHolder.setLink(link.getDocName());
        customHolder.setTag(link);
        customHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadSuplarch((SuplarchLink) v.getTag());
            }
        });
    }

    /**
     * Spustí stahování Suplarchu
     * @param link  SuplarchLink
     * @see SuplarchLink
     * @see StahniSuplarch
     */
    private void downloadSuplarch(final SuplarchLink link) {
        Crashlytics.log(Log.INFO, TAG, "Suplarch download");
        StahniSuplarch stahniSuplarch = new StahniSuplarch(context);
        stahniSuplarch.stahni(link);
    }

    /**
     * Vrátí počet linků
     * @return  Počet
     */
    @Override
    public int getItemCount() {
        return links.size();
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
        View view = inflater.inflate(R.layout.item_suplarch_link, parent, false);
        return new CustomViewHolder(view);
    }

    /**
     * Vlastní ViewHolder
     */
    public class CustomViewHolder extends RecyclerView.ViewHolder {
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
         * Nastaví onClickListener (ten pak spoští stahování {@link #downloadSuplarch(SuplarchLink)})
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
