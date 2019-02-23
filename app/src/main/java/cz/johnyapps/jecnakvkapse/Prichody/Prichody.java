package cz.johnyapps.jecnakvkapse.Prichody;

import java.util.ArrayList;

/**
 * Slouží k ukládání příchodů
 * @see Prichod
 * @see PrichodyConvertor
 */
public class Prichody {
    private ArrayList<Prichod> prichody;

    /**
     * Inicializace
     * @param prichody  Příchody
     * @see Prichod
     */
    Prichody(ArrayList<Prichod> prichody) {
        this.prichody = prichody;
    }

    /**
     * Vrací příchody
     * @return  ArrayList příchodů
     * @see Prichod
     */
    public ArrayList<Prichod> getPrichody() {
        return prichody;
    }
}
