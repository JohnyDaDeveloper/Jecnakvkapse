package cz.johnyapps.jecnakvkapse.Omluvenky;

import android.text.Spanned;

import cz.johnyapps.jecnakvkapse.Tools.TextUtils;

/**
 * Slouží k ukládání jednotlivých omluvenek
 * @see OmluvenkyConvertor
 */
public class Omluvenka {
    private String datum;
    private Spanned hodin;

    /**
     * Inicializace
     * @param datum Datum
     * @param hodin Počet hodin (Převede se na Spanned)
     */
    Omluvenka(String datum, String hodin) {
        TextUtils utils = new TextUtils();

        this.datum = datum;
        this.hodin = utils.fromHtml(hodin);
    }

    /**
     * Vrátí datum
     * @return  Datum
     */
    public String getDatum() {
        return datum;
    }

    /**
     * Vrátí počet hodin
     * @return  Počet hodin
     */
    public Spanned getHodin() {
        return hodin;
    }
}
