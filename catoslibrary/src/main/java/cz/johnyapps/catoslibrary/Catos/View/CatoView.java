package cz.johnyapps.catoslibrary.Catos.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import cz.johnyapps.catoslibrary.Catos.Entity.Cato;
import cz.johnyapps.catoslibrary.R;

public class CatoView extends View {
    private Cato cato;

    private int left = 0;
    private int top = 0;
    private int right = 0;
    private int bottom = 0;

    public CatoView(Context context) {
        super(context);
        initialize();
    }

    public CatoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CatoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        cato = new Cato();
        invalidate();
    }

    public boolean saveToPNG() {
        Bitmap  bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Catos");
        boolean foldersNotCreated = false;

        if (!folder.exists() && !folder.mkdirs()) {
            foldersNotCreated = true;
            Toast.makeText(getContext(), "Saving failed (#0)", Toast.LENGTH_LONG).show();
        }

        if (!foldersNotCreated) {
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

    public void setCato(Cato cato) {
        this.cato = cato;
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (cato != null) {
            setBackgroundColor(cato.getColor(Cato.BACKGROUND));

            int width = getWidth();
            int height = getHeight();
            double padding = 0.1;

            int heightOffset = width < height ? (int) Math.round((height - width) * (0.5 - padding)) : 0;
            int widthOffset = height < width ? (int) Math.round((width - height) * (0.5 - padding)) : 0;

            left = (int) Math.round(width * padding) + widthOffset;
            top = (int) Math.round(height * padding) + heightOffset;
            right = (int) Math.round(width - width * padding) - widthOffset;
            bottom = (int) Math.round(height - height * padding) - heightOffset;

            drawTop(canvas);
            drawFace(canvas);

            drawCollar(canvas);

            drawlegs(canvas);
            drawPaws(canvas);

            drawTail(canvas);

            drawBody(canvas);

            drawShadows(canvas);
        }
    }

    private void drawDrawable(Canvas canvas, int id, int color) {
        Drawable drawable = getResources().getDrawable(id, getContext().getTheme());
        drawable.setBounds(left, top, right, bottom);

        if (color < 0) {
            drawable.setTint(color);
        }

        drawable.draw(canvas);
    }

    private void drawTop(Canvas canvas) {
        drawDrawable(canvas, R.drawable.ear_left_out, cato.getColor(Cato.EAR_LEFT_OUT));
        drawDrawable(canvas, R.drawable.ear_right_out, cato.getColor(Cato.EAR_RIGHT_OUT));
        drawDrawable(canvas, R.drawable.ear_left_in, cato.getColor(Cato.EAR_LEFT_IN));
        drawDrawable(canvas, R.drawable.ear_right_in, cato.getColor(Cato.EAR_RIGHT_IN));
        drawDrawable(canvas, R.drawable.head, cato.getColor(Cato.HEAD));
        drawDrawable(canvas, R.drawable.head_top, cato.getColor(Cato.HEAD_TOP));
    }

    private void drawFace(Canvas canvas) {
        drawDrawable(canvas, R.drawable.eyes, cato.getColor(Cato.EYES));
        drawDrawable(canvas, R.drawable.mouth, cato.getColor(Cato.MOUTH));
    }

    private void drawCollar(Canvas canvas) {
        drawDrawable(canvas, R.drawable.collar, cato.getColor(Cato.COLLAR));
    }

    private void drawlegs(Canvas canvas) {
        drawDrawable(canvas, R.drawable.leg_1, cato.getColor(Cato.LEG_RIGHT_FRONT));
        drawDrawable(canvas, R.drawable.leg_2, cato.getColor(Cato.LEG_RIGHT_BACK));
        drawDrawable(canvas, R.drawable.leg_3, cato.getColor(Cato.LEG_LEFT_BACK));
        drawDrawable(canvas, R.drawable.leg_4, cato.getColor(Cato.LEG_LEFT_FRONT));
    }

    private void drawPaws(Canvas canvas) {
        drawDrawable(canvas, R.drawable.paw_1, cato.getColor(Cato.PAW_RIGHT_FRONT));
        drawDrawable(canvas, R.drawable.paw_2, cato.getColor(Cato.PAW_RIGHT_BACK));
        drawDrawable(canvas, R.drawable.paw_3, cato.getColor(Cato.PAW_LEFT_BACK));
        drawDrawable(canvas, R.drawable.paw_4, cato.getColor(Cato.PAW_LEFT_FRONT));
    }

    private void drawTail(Canvas canvas) {
        drawDrawable(canvas, R.drawable.tail, cato.getColor(Cato.TAIL));
        drawDrawable(canvas, R.drawable.tail_top, cato.getColor(Cato.TAIL_TOP));
    }

    private void drawBody(Canvas canvas) {
        drawDrawable(canvas, R.drawable.body, cato.getColor(Cato.BODY));
        drawDrawable(canvas, R.drawable.tummy, cato.getColor(Cato.TUMMY));
    }

    private void drawShadows(Canvas canvas) {
        Drawable drawable = getResources().getDrawable(R.drawable.shadows, getContext().getTheme());
        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
    }
}
