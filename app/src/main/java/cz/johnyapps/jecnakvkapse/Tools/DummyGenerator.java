package cz.johnyapps.jecnakvkapse.Tools;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import cz.johnyapps.jecnakvkapse.Omluvenky.Omluvnak;
import cz.johnyapps.jecnakvkapse.Prichody.Prichod;
import cz.johnyapps.jecnakvkapse.Prichody.Prichody;
import cz.johnyapps.jecnakvkapse.Rozvrh.Den;
import cz.johnyapps.jecnakvkapse.Rozvrh.Hodina;
import cz.johnyapps.jecnakvkapse.Rozvrh.Period;
import cz.johnyapps.jecnakvkapse.Rozvrh.Predmet;
import cz.johnyapps.jecnakvkapse.Rozvrh.Rozvrh;
import cz.johnyapps.jecnakvkapse.Score.Mark;
import cz.johnyapps.jecnakvkapse.Score.Score;
import cz.johnyapps.jecnakvkapse.Score.Subject;
import cz.johnyapps.jecnakvkapse.Singletons.User;
import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchHolder;

/**
 * Generátor na "Dummy data" (pro Demo)
 */
public class DummyGenerator {
    private static final String TAG = "DummyGenerator";

    private static final int MIN_MARKS = 2;
    private static final int MAX_MARKS = 12;
    private static final int MIN_MARK_VALUE = 1;
    private static final int MAX_MARK_VALUE = 5;

    private static final int MIN_HODIN = 4;
    private static final int MAX_HODIN = 8;

    private static final String[] predmetNazvy = new String[]{"Čj", "Aj", "M", "TV", "PA", "PV", "EnM",
            "SE", "EM", "EP"};

    private User user;
    private Random random;

    /**
     * Inicializace
     */
    public DummyGenerator() {
        user = User.getUser();
        random = new Random();
    }

    /**
     * Falešné přihlášení
     */
    public void login() {
        user.setLogin("Demo user");
        user.setLogged(true);
        user.setDummy(true);

        generateScore();
        generateRozvrh();
        generateOmluvnak();
        generatePrichody();
    }

    /**
     * Generace skóre (známky v předmětech)
     * @see cz.johnyapps.jecnakvkapse.Fragments.MainFragment_Znamky
     */
    private void generateScore() {
        Crashlytics.log(Log.INFO, TAG, "generateScore");

        ArrayList<Subject> subjects = new ArrayList<>();
        String[] subNames = new String[]{"Český jazyk", "Anglický jazyk", "Matematika", "Tělesná výchova",
                "Počítačové aplikace", "Programové vybavení", "Elektronika a mikroelektronika",
                "Silnoproudá zařízení", "Elektrotechnická měření", "Elektronické počítače"};

        for (String name : subNames) {
            Subject subject = new Subject(name);
            ArrayList<Mark> marks = generateMarks();
            subject.setMarks(marks);

            subjects.add(subject);
        }

        Score score = new Score(subjects);
        user.setScore(score);
    }

    /**
     * Generuje známky
     * @return  ArrayList známek
     */
    private ArrayList<Mark> generateMarks() {
        Crashlytics.log(Log.INFO, TAG, "generateMarks");

        ArrayList<Mark> marks = new ArrayList<>();

        int num = random.nextInt((MAX_MARKS - MIN_MARKS) + 1) + MIN_MARKS;

        for (int i = 0; i < num; i++) {
            marks.add(generateMark());
        }

        return marks;
    }

    /**
     * Generuje jednu známku
     * @return  Známka
     */
    private Mark generateMark() {
        Crashlytics.log(Log.INFO, TAG, "generateMark");

        String employee = "Uč";
        int value = random.nextInt((MAX_MARK_VALUE - MIN_MARK_VALUE) + 1) + MIN_MARK_VALUE;
        String title = "(Datum, Vyučující)";
        boolean small = random.nextBoolean();

        return new Mark(employee, String.valueOf(value), title, small);
    }

    /**
     * Generuje rozvrh
     * @see cz.johnyapps.jecnakvkapse.Fragments.MainFragment_Rozvrh
     */
    private void generateRozvrh() {
        Locale locale = new Locale("cs", "CZ");
        SimpleDateFormat format = new SimpleDateFormat("kk:mm dd.MM.yyyy", locale);
        String datum = format.format(Calendar.getInstance().getTime());

        Rozvrh rozvrh = new Rozvrh(datum);
        rozvrh.setPeriods(generatePeriods());

        String[] names = new String[]{"Po", "Út", "St", "Čt", "Pá"};

        for (String name : names) {
            Den den = generateDen(name);
            rozvrh.addDen(den);
        }

        user.setRozvrh(rozvrh);
    }

    /**
     * Generuje ArrayList dat (period) pro horní lištu s časy a čísly hodin
     * @return  ArrayList period
     */
    private ArrayList<Period> generatePeriods() {
        String[] casy = new String[]{"7:30 - 8:15", "8:25 - 9:10", "9:20 - 10:05", "10:20 - 11:05",
                "11:15 - 12:00", "12:10 - 12:55", "13:05 - 13:50", "14:00 - 14:45", "14:55 - 15:40", "15:50 - 16:35"};

        ArrayList<Period> periods = new ArrayList<>();

        for (int i = 0; i < casy.length; i++) {
            Period period = new Period(i + 1, casy[i]);
            periods.add(period);
        }

        return periods;
    }

    /**
     * Generuje den z rozvrhu
     * @param name  Zkratka dne (např. Po)
     * @return      Den
     */
    private Den generateDen(String name) {
        int hodin = random.nextInt((MAX_HODIN - MIN_HODIN) + 1) + MIN_HODIN;

        Den den = new Den(name);

        for (int i = 0; i < hodin; i++) {
            Hodina hodina = generateHodina(i + 1);
            den.addHodina(hodina);
        }

        return den;
    }

    /**
     * Generuje hodinu z rozvrhu
     * @param cislo Číslo hodiny (odpovídá číslu periody)
     * @return      Hodina
     */
    private Hodina generateHodina(int cislo) {
        int multi = random.nextInt(3);
        int predmetu = 1;

        if (multi == 0) {
            predmetu = random.nextInt(3) + 1;
        }

        Hodina hodina = new Hodina(cislo);
        int iNazev = random.nextInt(predmetNazvy.length);
        String nazev = predmetNazvy[iNazev];

        for (int i = 0; i < predmetu; i++) {
            Predmet predmet = generatePredmet(nazev,i + 1, predmetu > 1);
            hodina.addPredmet(predmet);
        }

        return hodina;
    }

    /**
     * Generuje předmět z rozvrhu
     * @param nazev         Zkratka předmětu (Např. TV)
     * @param cisloSkupiny  Číslo skupiny
     * @param pripsat       True pokud se má vypisovat číslo skupiny
     * @return              Předmět
     */
    private Predmet generatePredmet(String nazev, int cisloSkupiny, boolean pripsat) {
        int iUcebna = random.nextInt(4) + 1;

        String vyucujici = "Uč";
        String ucebna = "U" + iUcebna;
        String skupina = "S";
        String trida = "A4";

        if (pripsat) {
            skupina += cisloSkupiny;
        }

        return new Predmet(nazev, vyucujici, ucebna, skupina, trida);
    }

    /**
     * Generuje omluvný list (prázdný)
     */
    private void generateOmluvnak() {
        Omluvnak omluvnak = new Omluvnak();
        user.setOmluvnak(omluvnak);
    }

    /**
     * Generuje příchody (prázdné)
     */
    private void generatePrichody() {
        ArrayList<Prichod> arrayList = new ArrayList<>();
        Prichody prichody = new Prichody(arrayList);

        user.setPrichody(prichody);
    }

    /**
     * Generuje suplování (prázdné)
     */
    private void generateSuplarchHolder() {
        SuplarchHolder suplarchHolder = new SuplarchHolder();
        user.setSuplarchHolder(suplarchHolder);
    }
}
