package cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Hledá linky/odkazy na suplování v HTML
 * @see cz.johnyapps.jecnakvkapse.Suplarch.SuplarchHolder
 * @see cz.johnyapps.jecnakvkapse.Singletons.User
 * @see cz.johnyapps.jecnakvkapse.Fragments.MainFragment_Suplarch
 */
public class SuplarchFindLink {
    /**
     * Inicializace
     */
    public SuplarchFindLink() {

    }

    /**
     * Konvertuje HTML na ArrayList suplarch linků. Ke zpracování událostí slouží {@link #convertEvent(Element)}.
     * @param data  HTML
     * @return      ArayList suplarchů
     * @see SuplarchLink
     */
    public ArrayList<SuplarchLink> convert(String data) {
        Document doc = Jsoup.parse(data);
        Elements table = doc.selectFirst("main").select("div[class$=event]");

        ArrayList<SuplarchLink> links = new ArrayList<>();

        for (Element event : table) {
            SuplarchLink suplarchLink = convertEvent(event);
            if (suplarchLink != null) {
                links.add(suplarchLink);
            }
        }

        return links;
    }

    /**
     * Prozkoumá HTML element s událostí a hledá suplování
     * @param event HTML element s událostí
     * @return      Pokud je událost suplování, vrátí {@link SuplarchLink}. Pokud událost není suplování, vrátí null.
     */
    private SuplarchLink convertEvent(Element event) {
        Element nazev = event.selectFirst("div[class$=name]").selectFirst("h2").selectFirst("a");
        Element files = event.selectFirst("ul[class$=files]");
        String strNazev = nazev.html();

        if (files != null) {
            Elements links = files.select("a[class$=file]");

            for (Element link : links) {
                String supl = link.attr("href");
                String[] parts = supl.split("/");

                if (parts[parts.length - 1].toLowerCase().contains("supl")) {
                    return new SuplarchLink(strNazev, supl);
                }
            }
        }

        return null;
    }
}
