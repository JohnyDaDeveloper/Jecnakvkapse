package cz.johnyapps.jecnakvkapse.Rozvrh;

import java.util.ArrayList;

/**
 * Slouží k ukládání informací o dni
 * @see RozvrhConventor
 */
public class Den {
    private String nazev;

    private ArrayList<Hodina> hodiny;

    /**
     * Inicializace
     * @param nazev Název
     */
    Den(String nazev) {
        this.nazev = nazev;
        hodiny = new ArrayList<>();
    }

    /**
     * Vrací název
     * @return  Název
     */
    public String getNazev() {
        return nazev;
    }

    /**
     * Přidává hodinu do dne
     * @param hodina    {@link Hodina}
     */
    void addHodina(Hodina hodina) {
        hodiny.add(hodina);
    }

    /**
     * Vrací hodiny
     * @return ArrayList hodin
     * @see Hodina
     */
    public ArrayList<Hodina> getHodiny() {
        return hodiny;
    }
}
