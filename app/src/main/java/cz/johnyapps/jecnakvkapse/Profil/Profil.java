package cz.johnyapps.jecnakvkapse.Profil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Slouží k uchovávání profilu
 */
public class Profil {
    private String trida;
    private ArrayList<String> skupiny;

    /**
     * Inicializace
     */
    Profil() {
        skupiny = new ArrayList<>();
    }


    /**
     * Nastaví třídu a skupiny. Konvertuje string s daty.
     * @param data  Data
     */
    void setTridaASkupiny(String data) {
        String parts[] = data.split(":");
        trida = parts[0].split(",")[0];

        if (parts.length > 1) {
            String skupiny = parts[1];
            skupiny = skupiny.replaceAll(" ", "");

            String skupinyParts[] = skupiny.split(",");

            this.skupiny.addAll(Arrays.asList(skupinyParts));
        }
    }

    /**
     * Vrátí třídu
     * @return  Třída
     */
    public String getTrida() {
        return trida;
    }

    /**
     * Vrátí skupiny
     * @return  Skupiny v ArrayListu
     */
    public ArrayList<String> getSkupiny() {
        return skupiny;
    }
}
