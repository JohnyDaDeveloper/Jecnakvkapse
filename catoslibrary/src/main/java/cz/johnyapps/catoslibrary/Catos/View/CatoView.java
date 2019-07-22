package cz.johnyapps.catoslibrary.Catos.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import cz.johnyapps.catoslibrary.Catos.Entity.Cato;
import cz.johnyapps.catoslibrary.R;

/**
 * View showing cato
 */
public class CatoView extends View {
    private Cato cato;

    private double padding;

    private int left = 0;
    private int top = 0;
    private int right = 0;
    private int bottom = 0;

    /**
     * Inicialization
     * @param context   Context
     */
    public CatoView(Context context) {
        super(context);
        initialize();
    }

    /**
     * Inicialization
     * @param context   Context
     * @param attrs     Attribute set
     */
    public CatoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    /**
     * Inicialization
     * @param context       Contest
     * @param attrs         Attribute set
     * @param defStyleAttr  Default style
     */
    public CatoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    /**
     * Inicialization
     */
    private void initialize() {
        cato = new Cato();
        padding = 0.1;
        invalidate();
    }

    /**
     * Sets padding
     * @param padding   Padding
     */
    public void setPadding(double padding) {
        this.padding = padding;
    }

    /**
     * Saves cato as png to gallery (into Pictures/Catos)
     * Fail #0 - Folder couldn't be created
     * Fail #1 - Saving failed, see log for more info
     * @return  True - saved, False - saving failed
     */
    public boolean saveToPNG() {
        Bitmap  bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Catos");
        boolean foldersCreated = true;

        if (!folder.exists() && !folder.mkdirs()) {
            foldersCreated = false;
            Toast.makeText(getContext(), "Saving failed (#0)", Toast.LENGTH_LONG).show();
        }

        if (foldersCreated) {
            int count = 0;
            String filename = "cato_" + count;
            boolean reset;
            File[] files = folder.listFiles();

            do {
                reset = false;

                for (File file : files) {
                    if (file.getName().equals(filename + ".png")) {
                        count++;
                        filename = "cato_" + count;
                        reset = true;
                        break;
                    }
                }
            } while (reset);

            File file = new File(folder, filename + ".png");

            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                Toast.makeText(getContext(), "Saved!", Toast.LENGTH_LONG).show();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Saving failed (#1)", Toast.LENGTH_LONG).show();
            }
        }

        return false;
    }

    /**
     * Exports Cato as JSON file
     */
    public void export() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/Catos");
        File[] files = folder.listFiles();
        boolean foldersCreated = true;

        if (!folder.exists() && !folder.mkdirs()) {
            foldersCreated = false;
            Toast.makeText(getContext(), "Saving failed (#0)", Toast.LENGTH_LONG).show();
        }

        if (foldersCreated) {
            String original = "cato";
            String name = original;
            int i = 1;

            for (File file : files) {
                if (file.getName().equals(name + ".json")) {
                    name = original + "_" + i;
                    i++;
                } else {
                    break;
                }
            }

            File file = new File(folder.getPath(), name + ".json");

            cato.saveToFile(getContext(), file);
        }
    }

    /**
     * Sets {@link Cato} to this view
     * @param cato  {@link Cato}
     */
    public void setCato(Cato cato) {
        this.cato = cato;
        invalidate();
    }

    /**
     * Draws {@link Cato} on this view
     * @param canvas    Canvas
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (cato != null) {
            setBackgroundColor(cato.getColor(Cato.BACKGROUND));

            int width = getWidth();
            int height = getHeight();

            int heightOffset = width < height ? (int) Math.round((height - width) * (0.5 - padding)) : 0;
            int widthOffset = height < width ? (int) Math.round((width - height) * (0.5 - padding)) : 0;

            left = (int) Math.round(width * padding) + widthOffset;
            top = (int) Math.round(height * padding) + heightOffset;
            right = (int) Math.round(width - width * padding) - widthOffset;
            bottom = (int) Math.round(height - height * padding) - heightOffset;

            //Uncomment this to draw testing background for cato
            //drawTestBackground(canvas);

            drawTop(canvas);
            drawFace(canvas);

            drawCollar(canvas);

            drawLegs(canvas);
            drawPaws(canvas);

            drawTail(canvas);

            drawBody(canvas);

            drawShadows(canvas);
        }
    }

    /**
     * Draws colored drawable
     * @param canvas    Canvas
     * @param id        Id of drawable
     * @param color     Color
     */
    private void drawDrawable(Canvas canvas, int id, int color) {
        Drawable drawable = getResources().getDrawable(id, getContext().getTheme());
        //drawable.setBounds(left, top, right, bottom);

        if (color < 0) {
            drawable.setTint(color);
        }

        Bitmap bitmap = getBitmap((VectorDrawable) drawable, canvas);
        Paint paint = new Paint(color);

        //drawable.draw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    /**
     * Draws drawable
     * @param canvas    Canvas
     * @param id        Id of drawable
     */
    private void drawDrawable(Canvas canvas, int id) {
        Drawable drawable = getResources().getDrawable(id, getContext().getTheme());
        drawable.setBounds(left, top, right, bottom);

        drawable.draw(canvas);
    }

    /**
     * Returns bitmat of drawable
     * @param vectorDrawable    Drawable
     * @param parent            Canvas
     * @return                  Bitmap
     */
    private Bitmap getBitmap(VectorDrawable vectorDrawable, Canvas parent) {
        Bitmap bitmap = Bitmap.createBitmap(parent.getWidth(), parent.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.setBounds(left, top, right, bottom);
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    /**
     * Draws parts of cato above the collar (Except face)
     * @param canvas    Canvas
     */
    private void drawTop(Canvas canvas) {
        drawDrawable(canvas, R.drawable.ear_left_out, cato.getColor(Cato.EAR_LEFT_OUT));
        drawDrawable(canvas, R.drawable.ear_right_out, cato.getColor(Cato.EAR_RIGHT_OUT));
        drawDrawable(canvas, R.drawable.ear_left_in, cato.getColor(Cato.EAR_LEFT_IN));
        drawDrawable(canvas, R.drawable.ear_right_in, cato.getColor(Cato.EAR_RIGHT_IN));
        drawDrawable(canvas, R.drawable.head, cato.getColor(Cato.HEAD));
        drawDrawable(canvas, R.drawable.head_top, cato.getColor(Cato.HEAD_TOP));
    }

    /**
     * Draws face of the cato
     * @param canvas    Canvas
     */
    private void drawFace(Canvas canvas) {
        drawDrawable(canvas, R.drawable.eyes, cato.getColor(Cato.EYES));
        drawDrawable(canvas, cato.getStyle(Cato.MOUTH), cato.getColor(Cato.MOUTH));
    }

    /**
     * Draws collar of cato
     * @param canvas    Canvas
     */
    private void drawCollar(Canvas canvas) {
        drawDrawable(canvas, R.drawable.collar, cato.getColor(Cato.COLLAR));
    }

    /**
     * Draes legs of cato
     * @param canvas    Canvas
     */
    private void drawLegs(Canvas canvas) {
        drawDrawable(canvas, R.drawable.leg_1, cato.getColor(Cato.LEG_RIGHT_FRONT));
        drawDrawable(canvas, R.drawable.leg_2, cato.getColor(Cato.LEG_RIGHT_BACK));
        drawDrawable(canvas, R.drawable.leg_3, cato.getColor(Cato.LEG_LEFT_BACK));
        drawDrawable(canvas, R.drawable.leg_4, cato.getColor(Cato.LEG_LEFT_FRONT));
    }

    /**
     * Draws paws of cato
     * @param canvas    Canvas
     */
    private void drawPaws(Canvas canvas) {
        drawDrawable(canvas, R.drawable.paw_1, cato.getColor(Cato.PAW_RIGHT_FRONT));
        drawDrawable(canvas, R.drawable.paw_2, cato.getColor(Cato.PAW_RIGHT_BACK));
        drawDrawable(canvas, R.drawable.paw_3, cato.getColor(Cato.PAW_LEFT_BACK));
        drawDrawable(canvas, R.drawable.paw_4, cato.getColor(Cato.PAW_LEFT_FRONT));
    }

    /**
     * Draws tail of cato
     * @param canvas    Canvas
     */
    private void drawTail(Canvas canvas) {
        drawDrawable(canvas, R.drawable.tail, cato.getColor(Cato.TAIL));
        drawDrawable(canvas, R.drawable.tail_top, cato.getColor(Cato.TAIL_TOP));
    }

    /**
     * Draws body of cato
     * @param canvas    Canvas
     */
    private void drawBody(Canvas canvas) {
        drawDrawable(canvas, R.drawable.body, cato.getColor(Cato.BODY));
        drawDrawable(canvas, R.drawable.tummy, cato.getColor(Cato.TUMMY));
    }

    /**
     * Draws shadows of cato
     * @param canvas    Canvas
     */
    private void drawShadows(Canvas canvas) {
        drawDrawable(canvas, R.drawable.shadows);
    }

    /**
     * Draws a test background
     * @param canvas    Canvas
     */
    private void drawTestBackground(Canvas canvas) {
        drawDrawable(canvas, R.drawable.test_bck, Color.RED);
    }
}
