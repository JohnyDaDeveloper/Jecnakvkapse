package cz.johnyapps.jecnakvkapse.Omluvenky;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Konvertuje HTML na omluvekny
 * @see cz.johnyapps.jecnakvkapse.Fragments.Main.MainFragment_Omluvenky
 * @see Omluvnak
 * @see cz.johnyapps.jecnakvkapse.Singletons.User
 */
public class OmluvenkyConvertor {
    /**
     * Inicializace
     */
    public OmluvenkyConvertor() {

    }

    /**
     * Konveruje HTML a vrátí omluvňák (Používá k tomu {@link #convertOmluvenka(Element)})
     * @param result    HTML
     * @return          {@link Omluvnak}
     */
    public Omluvnak convert(String result) {
        Omluvnak omluvnak = new Omluvnak();

        Document doc = Jsoup.parse(result);
        Element table = doc.selectFirst("table[class$=absence-list]");
        Elements rows = table.select("tr");

        for (Element row : rows) {
            omluvnak.addOmluvenka(convertOmluvenka(row));
        }

        return omluvnak;
    }

    /**
     * Konvertuje jednotlivé omluvenky
     * @param omluvenka HTML element s omluvenkou
     * @return          {@link Omluvenka}
     */
    private Omluvenka convertOmluvenka(Element omluvenka) {
        Element date = omluvenka.selectFirst("a[class$=nounderline]");
        Element count = omluvenka.selectFirst("td[class$=count]");

        String strCount = count.html().replace("<strong>", "").replace("</strong>", "");

        return new Omluvenka(date.html(), strCount);
    }
}
