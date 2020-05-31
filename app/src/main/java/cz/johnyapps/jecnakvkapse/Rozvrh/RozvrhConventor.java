package cz.johnyapps.jecnakvkapse.Rozvrh;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cz.johnyapps.jecnakvkapse.Fragments.RozvrhFragment;

/**
 * Konvertuje HTML na rozvrh
 * @see Rozvrh
 * @see cz.johnyapps.jecnakvkapse.Singletons.User
 * @see RozvrhFragment
 */
public class RozvrhConventor {
    /**
     * Inicializace
     */
    public RozvrhConventor() {

    }

    /**
     * Konvertuje HTML na rozvrh. Na konvertování časů hodin používá {@link #convertPeriods(Elements)}, na dny {@link #convertDen(Element, int)}.
     * @param result    HTML
     * @return          {@link Rozvrh}
     * @see Rozvrh
     */
    public Rozvrh convert(String result) {
        Locale locale = new Locale("cs", "CZ");
        SimpleDateFormat format = new SimpleDateFormat("kk:mm dd.MM.yyyy", locale);
        String datum = format.format(Calendar.getInstance().getTime());

        Document doc = Jsoup.parse(result);
        Element table = doc.selectFirst("table[class$=timetable]").selectFirst("tbody");
        Elements rows = table.select("tr");
        Elements periods = rows.get(0).select("th[class$=period]");
        rows.remove(0);

        Rozvrh rozvrh = new Rozvrh(datum);
        ArrayList<Period> periodsObj = convertPeriods(periods);
        rozvrh.setPeriods(periodsObj);

        for (int i = 0; i < rows.size(); i++) {
            rozvrh.addDen(convertDen(rows.get(i), i));
        }

        return rozvrh;
    }

    /**
     * Konvertuje časy hodin
     * @param periods   HTML element obsahující čas hodiny
     * @return          ArrayList obsahující {@link Period} (Časy hodiny)
     */
    private ArrayList<Period> convertPeriods(Elements periods) {
        ArrayList<Period> periodsObj = new ArrayList<>();

        for (int i = 0; i < periods.size(); i++) {
            Element period = periods.get(i);
            Element cas = period.selectFirst("span[class$=time]");

            periodsObj.add(new Period(i + 1, cas.html()));
        }

        return periodsObj;
    }

    /**
     * Konvertuje HTML na {@link Den}. Na konvertování hodin slouží {@link #convertHodina(Element, int)}.
     * @param den       HTML element obsahující informace o dni
     * @param poradi    Pořadí v rozvrhu
     * @return          {@link Den}
     */
    private Den convertDen(Element den, int poradi) {
        Den objDen = new Den(den.selectFirst("th[class$=day]").html());
        Elements hodiny = den.select("td");

        for (Element hodina : hodiny) {
            objDen.addHodina(convertHodina(hodina, poradi));
        }

        return objDen;
    }

    /**
     * Konvertuje HTML na {@link Hodina}. Na konvertování předmětů slouží {@link #convertPredmet(Element)}.
     * @param hodina    HTML element obsahující informace o hodině
     * @param poradi    Pořadí v rozvrhu
     * @return          {@link Hodina}
     */
    private Hodina convertHodina(Element hodina, int poradi) {
        Hodina objHod = new Hodina(poradi);
        Elements predmety = hodina.select("div");

        for (Element predmet : predmety) {
            objHod.addPredmet(convertPredmet(predmet));
        }

        return objHod;
    }

    /**
     * Konvertuje HTML na {@link Predmet}
     * @param predmet   HTML element obsahující informace o předmětu
     * @return          {@link Predmet}
     */
    private Predmet convertPredmet(Element predmet) {
        String nazev = null;
        String ucebna = null;
        String vyucujici = null;
        String skupina = null;
        String trida = null;

        Elements data = predmet.select("span");

        for (Element d : data) {
            if (d.hasClass("subject")) nazev = d.html();
            if (d.hasClass("group")) skupina = d.html();
        }

        data = predmet.select("a");

        for (Element d : data) {
            if (d.hasClass("room")) ucebna = d.html();
            if (d.hasClass("employee")) vyucujici = d.html();
            if (d.hasClass("class")) trida = d.html();
        }

        return new Predmet(nazev, ucebna, vyucujici, skupina, trida);
    }
}
