package cz.johnyapps.jecnakvkapse.Prichody;

import java.util.ArrayList;

/**
 * Slouží k ukládání jednotlivých příchodů
 * @see PrichodyConvertor
 */
public class Prichod {
    private String datum;
    private ArrayList<String> casy;

    /**
     * Inicializace
     * @param datum Datum
     * @param casy  Casy v ArrayListu (String)
     */
    Prichod(String datum, ArrayList<String> casy) {
        this.datum = datum;
        this.casy = casy;
    }

    /**
     * Vrátí datum
     * @return  Datum
     */
    public String getDatum() {
        return datum;
    }

    /**
     * Vrátí časy
     * @return ArrayList s časy ve Stringu
     */
    public ArrayList<String> getCasy() {
        return casy;
    }
}
