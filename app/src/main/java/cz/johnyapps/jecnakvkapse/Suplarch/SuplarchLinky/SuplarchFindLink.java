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
 * @see cz.johnyapps.jecnakvkapse.Fragments.Main.MainFragment_Suplarch
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
        Elements table = doc.selectFirst("main").select("div[class$=event]");;

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
     * Prozkoumá HTML element s událostí. Pokud najde suplování pošle ho ke konvertování do {@link #convertSuplovani(Element)}.
     * @param event HTML element s událostí
     * @return      Pokud je událost suplování, vrátí {@link SuplarchLink}. Pokud událost není suplování, vrátí null.
     */
    private SuplarchLink convertEvent(Element event) {
        Element nazev = event.selectFirst("div[class$=name]").selectFirst("h2").selectFirst("a");
        String strNazev = nazev.html();
        String[] parts = strNazev.split(" ");

        if (parts[0].equals("Suplování")) {
            String link = convertSuplovani(event);

            return new SuplarchLink(strNazev, link);
        }

        return null;
    }

    /**
     * Najde v HTML elementu odkaz na suplování
     * @param event HTML element se suplováním
     * @return      Odkaz na suplování ve Stringu.
     */
    private String convertSuplovani(Element event) {
        Element link = event.selectFirst("ul[class$=files]").selectFirst("li").selectFirst("a[class$=file]");
        return link.attr("href");
    }
}
