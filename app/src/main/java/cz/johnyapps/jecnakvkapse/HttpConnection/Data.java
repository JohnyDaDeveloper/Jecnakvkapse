package cz.johnyapps.jecnakvkapse.HttpConnection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Slouží k uchovávání dat která mají klíč a hodnotu
 */
public class Data {
    private String key;
    private String value;

    /**
     * Inicializace
     * @param key   Klíč
     * @param value Hodnota
     */
    public Data(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Vrátí data v URLEncoded stringu
     * @return  Data ve Stringu (např. pro GET dotaz)
     */
    String getData() {
        try {
            return URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
