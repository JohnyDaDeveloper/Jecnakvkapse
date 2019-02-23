package cz.johnyapps.jecnakvkapse.Rozvrh;

import java.util.ArrayList;

/**
 * Slouží k uchovávání rozvrhu
 */
public class Rozvrh {
    private String datum;

    private ArrayList<Den> dny;
    private ArrayList<Period> periods;

    /**
     * Inicializace
     * @param datum Datum stažení
     */
    public Rozvrh(String datum) {
        this.datum = datum;

        this.dny = new ArrayList<>();
        this.periods = new ArrayList<>();
    }

    /**
     * Vrací datum
     * @return  Datum
     */
    public String getDatum() {
        return datum;
    }

    /**
     * Přidává den do rozvrhu
     * @param den   {@link Den}
     */
    void addDen(Den den) {
        dny.add(den);
    }

    /**
     * Vrací dny
     * @return  ArrayList dnů
     * @see Den
     */
    public ArrayList<Den> getDny() {
        return dny;
    }

    /**
     * Nastaví časy hoidn
     * @param periods  ArrayList časů hodin
     * @see Period
     */
    void setPeriods(ArrayList<Period> periods) {
        this.periods = periods;
    }

    /**
     * Vrací časty hoidn
     * @return ArrayList časů hodin
     * @see Period
     */
    public ArrayList<Period> getPeriods() {
        return periods;
    }
}
