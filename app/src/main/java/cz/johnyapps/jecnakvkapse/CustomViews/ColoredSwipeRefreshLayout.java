package cz.johnyapps.jecnakvkapse.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Tools.ColorUtils;

/**
 * SwipeRefreshLayout který má speciální atribut (refreshColor) na nastavení barvy progress kolečka
 */
public class ColoredSwipeRefreshLayout extends SwipeRefreshLayout {
    /**
     * Inicializace
     * @param context   Context
     * @param attrs     Atributy
     */
    public ColoredSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColoredSwipeRefreshLayout);

        try {
            ColorUtils colorUtils = new ColorUtils();
            int refreshColor = typedArray.getColor(R.styleable.ColoredSwipeRefreshLayout_refreshColor, colorUtils.getColorAccent(context));

            setColorSchemeColors(refreshColor);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Inicializace
     * @param context   Context
     */
    public ColoredSwipeRefreshLayout(Context context) {
        super(context);
    }
}
