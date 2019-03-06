package cz.johnyapps.jecnakvkapse.Score;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Konvetuje HTML na známky
 * @see Score
 * @see cz.johnyapps.jecnakvkapse.Singletons.User
 * @see cz.johnyapps.jecnakvkapse.Fragments.Main.MainFragment_Znamky
 */
public class MarkConvertor {
    /**
     * Inicializace
     */
    public MarkConvertor() {

    }

    /**
     * Konvetuje HTML na známky.
     * @param data  HTML
     * @return      Známky
     * @see Score
     * @see #convertRow(Elements, String)
     * @see #convertZaverecna(Element)
     */
    public Score convert(String data) {
        Document doc = Jsoup.parse(data);
        Element table = doc.selectFirst("table[class$=score]").selectFirst("tbody");
        Elements rows = table.select("tr");

        ArrayList<Subject> subjects = new ArrayList<>();

        for (Element row : rows) {
            String name = row.selectFirst("th").html();
            name = name.replace("&nbsp;", "");
            Elements strongs = row.select("strong");

            if (strongs.size() == 0) {
                Subject subject = convertRow(row.selectFirst("td").select("a"), name);
                Element zaverecna = row.select("td").get(1);

                subject.setZaverecna(convertZaverecna(zaverecna));
                subjects.add(subject);
            } else {
                Elements elements = row.getAllElements();
                elements.remove(elements.size() - 1);

                Elements cviceni = new Elements();
                Elements teorie = new Elements();
                boolean flag = true;

                for (Element element : elements) {
                    if (element.tagName().equals("strong")) {
                        if (element.html().contains("Teorie")) flag = false;
                    } else if (element.tagName().equals("a")) {
                        if (flag) cviceni.add(element);
                        else teorie.add(element);
                    }
                }

                Element zaver = row.select("td").get(1);

                Subject subCvic = convertRow(cviceni, name + " - Cvičení");
                subCvic.setZaverecna(convertZaverecna(zaver));
                subjects.add(subCvic);

                Subject subTeor = convertRow(teorie, name + " - Teorie");
                subTeor.setZaverecna(convertZaverecna(zaver));
                subjects.add(subTeor);
            }
        }

        return new Score(subjects);
    }

    /**
     * Konvetuje známky z jednoho předmětu
     * @param scores    HTML element se známkami
     * @param name      Název předmětu
     * @return          {@link Subject}
     */
    private Subject convertRow(Elements scores, String name) {
        Subject subject = new Subject(name);
        ArrayList<Mark> markArrayList = new ArrayList<>();

        for (Element score : scores) {
            markArrayList.add(convertScore(score));
        }

        subject.setMarks(markArrayList);
        return subject;
    }

    /**
     * Konvertuje jednotlivé známky
     * @param score HTML obsahující informace o známce
     * @return      {@link Mark}
     */
    private Mark convertScore(Element score) {
        Element value = score.selectFirst("span[class$=value]");
        Element emplyee = score.selectFirst("span[class$=employee]");

        String strVal = "";
        if (value != null) strVal = value.html();

        String strEmp = "";
        if (emplyee != null) strEmp = emplyee.html();

        boolean small = score.hasClass("scoreSmall");

        return new Mark(strEmp, strVal, score.attr("title"), small);
    }

    /**
     * Konvertuje element zaver na string s hodnotou závěrné známky
     * @param zaver Element se závěrnou známkou
     * @return      String se závěrnou známkou
     */
    private String convertZaverecna(Element zaver) {
        if (!zaver.html().isEmpty()) {
            System.out.println("XAXAX: " + zaver.html());
            return zaver.selectFirst("a").html();
        }

        return "";
    }
}
