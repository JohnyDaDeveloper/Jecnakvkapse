package cz.johnyapps.jecnakvkapse.Score;

import java.util.ArrayList;

/**
 * Slouží k ukládání informací o jednom předmětu a jeho známek
 */
public class Subject {
    private String name;
    private Mark zaverecna;
    private ArrayList<Mark> marks;

    /**
     * Inicializace
     * @param name  Název předmětu
     */
    Subject(String name) {
        this.name = name;
        this.zaverecna = null;
        this.marks = new ArrayList<>();
    }

    /**
     * Vrací název
     * @return  Název
     */
    public String getName() {
        return name;
    }

    /**
     * Nastaví závěrečnou známku
     * @param zaverecna Závěrečná známka
     */
    void setZaverecna(Mark zaverecna) {
        this.zaverecna = zaverecna;
    }

    /**
     * Vrátí závěrečnou známku
     * @return  Závěrečná známka
     */
    public Mark getZaverecna() {
        return zaverecna;
    }

    /**
     * Nastaví známky
     * @param marks ArrayList známek
     * @see Mark
     */
    void setMarks(ArrayList<Mark> marks) {
        this.marks = marks;
    }

    /**
     * Vrací známky
     * @return  ArrayList známek
     * @see Mark
     */
    public ArrayList<Mark> getMarks() {
        return marks;
    }
}
