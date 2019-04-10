package cz.johnyapps.jecnakvkapse.Prichody;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Konvertuje HTML na příchody
 * @see Prichody
 * @see cz.johnyapps.jecnakvkapse.Singletons.User
 * @see cz.johnyapps.jecnakvkapse.Fragments.MainFragment_Prichody
 */
public class PrichodyConvertor {
    /**
     * Inicializace
     */
    public PrichodyConvertor() {

    }

    /**
     * Konvertuje HTML na příchody
     * @param result    HTML
     * @return          {@link Prichody}
     * @see #convertPrichod(Element)
     */
    public Prichody convert(String result) {
        Document doc = Jsoup.parse(result);
        Element table = doc.selectFirst("table[class$=tab absence-list]").selectFirst("tbody");
        Elements rows = table.select("tr");

        ArrayList<Prichod> prichody = new ArrayList<>();

        for (Element row : rows) {
            prichody.add(convertPrichod(row));
        }

        return new Prichody(prichody);
    }

    /**
     * Konvertuje jednotlivé příchody
     * @param prichod   HTML element obsahující příchod
     * @return          {@link Prichod}
     * @see #convertCas(Element)
     */
    private Prichod convertPrichod(Element prichod) {
        Elements parts = prichod.select("td");
        Elements casy = prichod.select("p");

        String datum = parts.get(0).html().replaceAll("&nbsp;", " ");
        ArrayList<String> arrCasy = new ArrayList<>();

        for (Element cas : casy) {
            arrCasy.add(convertCas(cas));
        }

        return new Prichod(datum, arrCasy);
    }

    /**
     * Vrátí čas příchodu
     * @param cas   Element obsahující čas
     * @return      Čas
     */
    private String convertCas(Element cas) {
        return cas.html();
    }
}
