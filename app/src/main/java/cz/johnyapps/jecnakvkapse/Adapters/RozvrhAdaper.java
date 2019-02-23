package cz.johnyapps.jecnakvkapse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Rozvrh.Den;
import cz.johnyapps.jecnakvkapse.Rozvrh.Hodina;
import cz.johnyapps.jecnakvkapse.Rozvrh.Period;
import cz.johnyapps.jecnakvkapse.Rozvrh.Predmet;
import cz.johnyapps.jecnakvkapse.Rozvrh.Rozvrh;

/**
 * Adapter pro rozvrh
 * @see cz.johnyapps.jecnakvkapse.Fragments.Main.MainFragment_Rozvrh
 */
public class RozvrhAdaper {
    private Context context;
    private LayoutInflater inflater;

    private Rozvrh rozvrh;

    /**
     * Inicializace
     * @param context   Context
     * @param rozvrh    Rozvrh
     * @see cz.johnyapps.jecnakvkapse.Singletons.User
     * @see Rozvrh
     */
    public RozvrhAdaper(Context context, Rozvrh rozvrh) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        this.rozvrh = rozvrh;
    }

    /**
     * Vytvoří rozvrh
     * @param layout    LinearLayout na kterém se rozvrh zobrazí
     */
    public void adapt(LinearLayout layout) {
        //Creating period layout
        LinearLayout.LayoutParams periodLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout periodLayout = new LinearLayout(context);
        periodLayout.setOrientation(LinearLayout.HORIZONTAL);
        periodLayout.setLayoutParams(periodLayoutParams);

        //Creating periods
        for (Period period : rozvrh.getPeriods()) {
            periodLayout.addView(createPeriod(period, periodLayout));
        }

        //Creating days
        LinearLayout daysLayout = (LinearLayout) inflater.inflate(R.layout.item_dny, layout, false);

        for (Den den : rozvrh.getDny()) {
            daysLayout.addView(createDen(den));
        }

        layout.removeAllViews();

        layout.addView(periodLayout);
        layout.addView(daysLayout);
    }

    /**
     * Vytvoří časy hodin
     * @param period    Perioda
     * @param parent    Parent
     * @return          View s časem hodiny
     */
    private View createPeriod(Period period, ViewGroup parent) {
        View periodLayout = inflater.inflate(R.layout.item_period, parent, false);
        TextView hodina = periodLayout.findViewById(R.id.Period_hodina);
        TextView cas = periodLayout.findViewById(R.id.Period_cas);

        hodina.setText(String.valueOf(period.getPoradi()));
        cas.setText(period.getCas());

        return periodLayout;
    }

    /**
     * Vytvoří den
     * @param den   Den
     * @return      View se dnem
     */
    private LinearLayout createDen(Den den) {
        LinearLayout.LayoutParams denLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        denLayoutParams.weight = 1;

        LinearLayout denLayout = new LinearLayout(context);
        denLayout.setLayoutParams(denLayoutParams);

        for (Hodina hodina : den.getHodiny()) {
            denLayout.addView(createHodina(hodina, denLayout));
        }

        return denLayout;
    }

    /**
     * Vytvoří hodinu
     * @param hodina    Hodina
     * @param parent    Parent
     * @return          View s hodinou
     */
    private LinearLayout createHodina(final Hodina hodina, ViewGroup parent) {
        LinearLayout hodinaLayout = (LinearLayout) inflater.inflate(R.layout.item_hodina, parent, false);

        int weight = 6;
        if (hodina.getPredmety().size() != 0) weight /= hodina.getPredmety().size();

        for (Predmet predmet : hodina.getPredmety()) {
            hodinaLayout.addView(createPredmet(predmet, hodinaLayout, weight));
        }

        return hodinaLayout;
    }

    /**
     * Vytvoří předmět
     * @param predmet   Předmět
     * @param parent    Parent
     * @param weight    Váha v Layoutu (Podle počtu skupin)
     * @return          View s předmětem
     */
    private RelativeLayout createPredmet(Predmet predmet, ViewGroup parent, int weight) {
        LinearLayout.LayoutParams predmetLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, weight);

        RelativeLayout predmetLayout = (RelativeLayout) inflater.inflate(R.layout.item_predmet, parent, false);
        predmetLayout.setLayoutParams(predmetLayoutParams);

        TextView vyucujiciTxt = predmetLayout.findViewById(R.id.Predmet_vyucujici);
        if (predmet.getVyucujici() != null) {
            vyucujiciTxt.setText(predmet.getVyucujici());
        } else {
            vyucujiciTxt.setVisibility(View.INVISIBLE);
        }

        TextView ucebnaTxt = predmetLayout.findViewById(R.id.Predmet_ucebna);
        if (predmet.getUcebna() != null) {
            ucebnaTxt.setText(predmet.getUcebna());
        } else {
            ucebnaTxt.setVisibility(View.INVISIBLE);
        }

        TextView predmetTxt = predmetLayout.findViewById(R.id.Predmet_predmet);
        if (predmet.getNazev() != null) {

            predmetTxt.setText(predmet.getNazev());
        } else {
            predmetTxt.setVisibility(View.INVISIBLE);
        }

        TextView tridaTxt = predmetLayout.findViewById(R.id.Predmet_trida);
        if (predmet.getTrida() != null) {
            tridaTxt.setText(predmet.getTrida());
        } else {
            tridaTxt.setVisibility(View.INVISIBLE);
        }

        TextView skupinaTxt = predmetLayout.findViewById(R.id.Predmet_skupina);
        if (predmet.getSkupina() != null) {
            skupinaTxt.setText(predmet.getSkupina());
        } else {
            skupinaTxt.setVisibility(View.INVISIBLE);
        }

        return predmetLayout;
    }
}
