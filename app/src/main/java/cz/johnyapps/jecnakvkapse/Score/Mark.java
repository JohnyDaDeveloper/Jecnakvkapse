package cz.johnyapps.jecnakvkapse.Score;

import android.graphics.Color;

/**
 * Slouží k uchovávání informací o známce
 */
public class Mark {
    private String employee;
    private String value;
    private String title;
    private boolean small;

    private boolean rozlVel;

    /**
     * Inicializace
     * @param employee  Zaměstnanec/Vyučující
     * @param value     Hodnota známky
     * @param title     Důvod
     * @param small     Malá. True - ano, Flase - ne.
     */
    Mark(String employee, String value, String title, boolean small) {
        this.employee = employee;
        this.value = value;
        this.title = convertTitle(title);
        this.small = small;

        rozlVel = true;
    }

    /**
     * Inicializace
     * @param value Hodnota známky
     * @param title Důvod
     */
    Mark(String value, String title) {
        this.value = value;
        this.title = title;

        rozlVel = true;
    }

    /**
     * Inicializace
     * @param value Hodnota známky
     */
    public Mark(String value) {
        this.value = value;

        rozlVel = true;
    }

    /**
     * Vrací vyučujícího
     * @return  Jméno
     */
    public String getEmployee() {
        return employee;
    }

    /**
     * Vrací hodnotu
     * @return  Hodnota
     */
    public String getValue() {
        return value;
    }

    /**
     * Vrací název
     * @return Název
     */
    public String getTitle() {
        return title;
    }

    /**
     * Nastavuje název
     * @param title Název
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Konveruje název pro lepší čtení
     * @param title Název
     * @return      Upravený název
     */
    private String convertTitle(String title) {
        String[] parts = title.split("\\(");
        if (parts.length == 2) {
            return parts[0] + "\n(" + parts[1];
        }

        return title;
    }

    /**
     * Vrátí small
     * @return True - je malá, False - je velká
     */
    public boolean isSmall() {
        return small;
    }

    /**
     * Vrací barvu známky podle její hodnoty
     * @return  Barva
     */
    public int getColor() {
        switch (value) {
            case "1": {
                return Color.argb(255, 85, 212, 0);
            }

            case "2": {
                return Color.argb(255, 196, 224, 80);
            }

            case "3": {
                return Color.argb(255, 255, 212, 42);
            }

            case "4": {
                return Color.argb(255, 255, 102, 0);
            }

            case "5": {
                return Color.argb(255, 255, 48, 48);
            }

            case "5?": {
                return Color.argb(255, 255, 138, 138);
            }

            case "N": {
                return Color.WHITE;
            }

            default: {
                if (title.toLowerCase().contains("pochvala")) {
                    return Color.argb(255, 85, 212, 0);
                } else if (title.toLowerCase().contains("důtka")) {
                    return Color.argb(255, 255, 48, 48);
                } else if (title.toLowerCase().contains("napomenutí")) {
                    return Color.argb(255, 255, 102, 0);
                }

                return Color.WHITE;
            }
        }
    }

    /**
     * Určuje zda se má u známky rozlišovat velikost
     * @param rozlVel   True - ano, False - ne
     */
    @SuppressWarnings("SameParameterValue")
    void rozlisovatVelikost(boolean rozlVel) {
        this.rozlVel = rozlVel;
    }

    /**
     * Nastaví zda se má u známky rozlišovat velikost. True - ano, False - ne
     */
    public boolean rozlisovatVelikost() {
        return rozlVel;
    }
}
