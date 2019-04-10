package cz.johnyapps.jecnakvkapse.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import cz.johnyapps.jecnakvkapse.R;
import cz.johnyapps.jecnakvkapse.Tools.TextUtils;

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
            TextUtils textUtils = new TextUtils();
            int refreshColor = typedArray.getColor(R.styleable.ColoredSwipeRefreshLayout_refreshColor, textUtils.getColorAccent(context));

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
