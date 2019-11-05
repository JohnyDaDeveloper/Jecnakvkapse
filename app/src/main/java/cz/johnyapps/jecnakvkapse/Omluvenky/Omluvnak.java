package cz.johnyapps.jecnakvkapse.Omluvenky;

import java.util.ArrayList;

/**
 * Slouží k ukládání omluvného listu.
 * @see OmluvenkyConvertor
 */
public class Omluvnak {
    private ArrayList<Omluvenka> omluvenky;

    /**
     * Inicializace
     */
    public Omluvnak() {
        omluvenky = new ArrayList<>();
    }

    /**
     * Přidá omluvenku do omluvňáku
     * @param omluvenka Omluvenka
     * @see Omluvenka
     */
    void addOmluvenka(Omluvenka omluvenka) {
        omluvenky.add(omluvenka);
    }

    /**
     * Vrátí omluvenky
     * @return  ArrayList s omluvenkami
     * @see Omluvenka
     */
    public ArrayList<Omluvenka> getOmluvenky() {
        return omluvenky;
    }
}
