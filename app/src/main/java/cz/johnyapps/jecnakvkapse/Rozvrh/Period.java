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

    /**
     * Vrátí začátek hodiny
     * @return  Začátek hodiny
     */
    String getZacatek() {
        String[] cas = this.cas.split(" - ");

        if (cas.length == 2) {
            return cas[0];
        }

        return "0:00";
    }

    /**
     * Vrátí konec hodiny
     * @return  konec hodiny
     */
    String getKonec() {
        String[] cas = this.cas.split(" - ");

        if (cas.length == 2) {
            return cas[1];
        }

        return "0:00";
    }
}
