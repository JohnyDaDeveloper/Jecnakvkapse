package cz.johnyapps.jecnakvkapse.Tools;

import android.content.Context;
import android.content.res.Resources;
import androidx.core.content.ContextCompat;
import android.util.TypedValue;

import cz.johnyapps.jecnakvkapse.R;

/**
 * Slouží k získávání barev
 */
public class ColorUtils {
    public ColorUtils() {

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

    /**
     * Vrátí colorPrimary barvu
     * @param context   Context
     * @return          Barva
     */
    int getColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return ContextCompat.getColor(context, typedValue.resourceId);
    }
}
