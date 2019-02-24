package cz.johnyapps.jecnakvkapse.Tools;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Slouží k formátování textu.
 */
public class TextUtils {
    /**
     * Inicializace
     */
    public TextUtils() {

    }

    /**
     * Převede HTML na Spanned (Hodí se např. pro textové pole, to html neumí a je třeba ho takto formátovat).
     * @param html  HTML
     * @return      Spanned
     */
    public Spanned fromHtml(String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            //noinspection deprecation
            return Html.fromHtml(html);
        }
    }
}
