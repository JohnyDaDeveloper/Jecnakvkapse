package cz.johnyapps.jecnakvkapse.Tools;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;

import cz.johnyapps.jecnakvkapse.R;

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

    /**
     * Vrátí colorAccent barvu
     * @param context   Context
     * @return          Barva
     */
    public int getColorAccent(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        return ContextCompat.getColor(context, typedValue.resourceId);
    }
}
