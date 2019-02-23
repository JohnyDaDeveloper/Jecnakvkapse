package cz.johnyapps.jecnakvkapse.HttpConnection;

import java.util.ArrayList;

/**
 * Slouží k uchování údajú o dotazu
 */
public class Request {
    private String dotaz;
    private String method;

    private String path;

    private StringBuilder data;

    /**
     * Inicializace
     * @param dotaz     Adresa dotazu
     * @param method    Metoda (POST, GET...)
     */
    public Request(String dotaz, String method) {
        this.dotaz = dotaz;
        this.method = method.toUpperCase();

        this.data = new StringBuilder();
    }

    /**
     * Inicializace
     * @param dotaz     Adresa dotazu
     * @param method    Metoda (POST, GET...)
     * @param path      Cesta k souboru
     */
    public Request(String dotaz, String method, String path) {
        this.dotaz = dotaz;
        this.method = method.toUpperCase();

        this.path = path;

        this.data = new StringBuilder();
    }

    /**
     * Vrátí dotaz
     * @return String dotaz
     */
    String getDotaz() {
        return dotaz;
    }

    /**
     * Vrátí metodu (GET, POST...)
     * @return  String metoda
     */
    String getMethod() {
        return method;
    }

    /**
     * Formátuje GET data pro pozdější pridání za dotaz
     * @param data  Data
     */
    public void putData(ArrayList<Data> data) {
        for (int i = 0; i < data.size(); i++) {
            String d = data.get(i).getData();
            if (i == 0) {
                this.data.append(d);
            } else {
                this.data.append("&").append(d);
            }
        }
    }

    /**
     * Vrátí formátovaná data
     * @return  String data
     */
    String getData() {
        return data.toString();
    }

    /**
     * Vrátí cestu k souboru
     * @return  String cesta
     */
    public String getPath() {
        return path;
    }
}
