package cz.johnyapps.jecnakvkapse.Rozvrh;

/**
 * Slouží k ukládání časů hodin
 * @see RozvrhConventor
 */
public class Period {
    private int poradi;
    private String cas;

    /**
     * Inicializace
     * @param poradi    Pořadí
     * @param cas       Čas (od - do)
     */
    Period(int poradi, String cas) {
        this.poradi = poradi;
        this.cas = cas;
    }

    /**
     * Vrací pořadí
      * @return Pořadí
     */
    public int getPoradi() {
        return poradi;
    }

    /**
     * Vrací čas
     * @return  Čas
     */
    public String getCas() {
        return cas;
    }
}
