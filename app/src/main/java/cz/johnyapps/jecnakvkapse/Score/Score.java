package cz.johnyapps.jecnakvkapse.Score;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Slouží k ukládání předmětů
 */
public class Score {
    private ArrayList<Subject> subjects;
    private String datum;

    /**
     * Inicializace
     * @param subjects  ArrayList s předměty
     * @see Subject
     */
    public Score(ArrayList<Subject> subjects) {
        this.subjects = subjects;

        Locale locale = new Locale("cs", "CZ");
        SimpleDateFormat format = new SimpleDateFormat("kk:mm dd.MM.yyyy", locale);
        this.datum = format.format(Calendar.getInstance().getTime());
    }

    /**
     * Vrací předměty
     * @return  ArrayList předmětů
     * @see Subject
     */
    public ArrayList<Subject> geSubjects() {
        return subjects;
    }

    /**
     * Vrací datum
     * @return  Datum
     */
    public String getDatum() {
        return datum;
    }
}
