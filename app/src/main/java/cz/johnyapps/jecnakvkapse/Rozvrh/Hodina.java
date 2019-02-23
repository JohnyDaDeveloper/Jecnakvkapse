package cz.johnyapps.jecnakvkapse.Rozvrh;

import java.util.ArrayList;

/**
 * Slouží k ukládání informácí o hodině
 * @see RozvrhConventor
 */
public class Hodina {
    private int cislo;

    private ArrayList<Predmet> predmety;

    /**
     * Inicializace
     * @param cislo Číslo hodiny
     */
    Hodina(int cislo) {
        this.cislo = cislo;
        this.predmety = new ArrayList<>();
    }

    /**
     * Vrací číslo hodiny
     * @return  Číslo hodiny
     */
    public int getCislo() {
        return cislo;
    }

    /**
     * Přidává předmět do hodiny
     * @param predmet   {@link Predmet}
     */
    void addPredmet(Predmet predmet) {
        predmety.add(predmet);
    }

    /**
     * Vrací predměty
     * @return  ArrayList předmětů
     * @see Predmet
     */
    public ArrayList<Predmet> getPredmety() {
        return predmety;
    }
}
