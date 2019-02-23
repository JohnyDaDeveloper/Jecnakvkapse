package cz.johnyapps.jecnakvkapse.Suplarch;

import java.util.ArrayList;

import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky.SuplarchLink;

/**
 * Slouží k ukládání {@link SuplarchLink}
 */
public class SuplarchHolder {
    private ArrayList<SuplarchLink> suplarchLinks;

    /**
     * Inicializace
     */
    public SuplarchHolder() {
        suplarchLinks = new ArrayList<>();
    }

    /**
     * Přidá suplarch linky
     * @param suplarchLinks Suplarch link
     * @see SuplarchLink
     */
    public void setSuplarchLinks(ArrayList<SuplarchLink> suplarchLinks) {
        this.suplarchLinks = suplarchLinks;
    }

    /**
     * Vrátí ArrayList se odkazy na suplování
     * @return  ArrayList SuplarchLinků
     * @see SuplarchLink
     */
    public ArrayList<SuplarchLink> getSuplarchLinks() {
        return suplarchLinks;
    }
}
