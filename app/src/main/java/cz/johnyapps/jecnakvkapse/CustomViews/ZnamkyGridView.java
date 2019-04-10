package cz.johnyapps.jecnakvkapse.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * GridView pro známky
 * @see cz.johnyapps.jecnakvkapse.Adapters.MarksGridAdapter
 */
public class ZnamkyGridView extends GridView {
    /**
     * Inicializace
     * @param context   Context
     * @param attrs     Atributy
     * @param defStyle  Základní styl
     */
    public ZnamkyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * Inicializace
     * @param context   Context
     * @param attrs     Atributy
     */
    public ZnamkyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Inicializace
     * @param context   Context
     */
    public ZnamkyGridView(Context context) {
        super(context);
    }

    /**
     * Měření velikosti
     * @param widthMeasureSpec  widthMeasureSpec
     * @param heightMeasureSpec heightMeasureSpec
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
