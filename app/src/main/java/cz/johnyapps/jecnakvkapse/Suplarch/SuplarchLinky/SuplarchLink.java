package cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky;

/**
 * Slouží k ukládání odkazu na suplování.
 * @see SuplarchFindLink
 */
public class SuplarchLink {
    private String nazev;
    private String link;

    /**
     * Inicializace
     * @param nazev Název
     * @param link  Odkaz
     */
    SuplarchLink(String nazev, String link) {
        this.nazev = nazev;
        this.link = link;
    }

    /**
     * Vrátí název
     * @return  Název
     */
    public String getNazev() {
        return nazev;
    }

    /**
     * Vrátí odkaz na suplování
     * @return  Odkaz
     */
    public String getLink() {
        return "https://www.spsejecna.cz" + link;
    }

    /**
     * Vrátí název dokumentu
     * @return  Název
     */
    public String getDocName() {
        String[] parts = link.split("/");
        return "SPSE-Jecna_" + parts[parts.length - 1].replace("+", " ");
    }
}
