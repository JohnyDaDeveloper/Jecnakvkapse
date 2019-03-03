package cz.johnyapps.jecnakvkapse.Profil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Konvertuje HTML na profil
 * @see Profil
 * @see cz.johnyapps.jecnakvkapse.Singletons.User
 */
public class ProfilConvertor {
    /**
     * Inicializace
     */
    public ProfilConvertor() {

    }

    /**
     * Konvertuje HTML na profil. Ke konvertování jednotlivých údajů slouží {@link #convertRow(Element, Profil)}.
     * @param result    HTML
     * @return          {@link Profil}
     * @see Profil
     */
    public Profil convert(String result) {
        Profil profil = new Profil();

        Document doc = Jsoup.parse(result);
        Element table = doc.selectFirst("table[class$=userprofile]");
        Elements rows = table.select("tr");

        for (Element row : rows) {
            convertRow(row, profil);
        }

        return profil;
    }

    /**
     * Konvertuje údaj
     * @param row       Řada
     * @param profil    Profil
     */
    private void convertRow(Element row, Profil profil) {
        Element title = row.selectFirst("span[class$=label]");
        Element value = row.selectFirst("span[class$=value]");

        switch (title.html()) {
            case "Třída, skupiny": {
                profil.setTridaASkupiny(value.html());
                break;
            }

            default:
                break;
        }
    }
}
