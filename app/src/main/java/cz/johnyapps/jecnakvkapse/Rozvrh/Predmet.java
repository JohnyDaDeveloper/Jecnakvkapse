package cz.johnyapps.jecnakvkapse.Rozvrh;

import androidx.annotation.Nullable;

public class Predmet {
    private String nazev;
    private String vyucujici;
    private String ucebna;
    private String skupina;
    private String trida;

    Predmet(@Nullable String nazev, @Nullable String vyucujici, @Nullable String ucebna, @Nullable String skupina, @Nullable String trida) {
        this.nazev = nazev;
        this.vyucujici = vyucujici;
        this.ucebna = ucebna;
        this.skupina = skupina;
        this.trida = trida;
    }

    public String getNazev() {
        return nazev;
    }

    public String getVyucujici() {
        return vyucujici;
    }

    public String getUcebna() {
        return ucebna;
    }

    public String getSkupina() {
        return skupina;
    }

    public String getTrida() {
        return trida;
    }
}
