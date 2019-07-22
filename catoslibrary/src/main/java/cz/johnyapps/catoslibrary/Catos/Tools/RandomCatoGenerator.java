package cz.johnyapps.catoslibrary.Catos.Tools;

import android.graphics.Color;

import java.util.Random;

import cz.johnyapps.catoslibrary.Catos.Entity.Cato;

/**
 * Generates a random {@link Cato}
 */
public class RandomCatoGenerator {
    private Random random;
    private Cato cato;

    private boolean lightBody = false;

    /**
     * Inicializaton
     */
    public RandomCatoGenerator() {
        init();
    }

    /**
     * Inicializaton
     */
    private void init() {
        cato = new Cato();
        random = new Random();
    }

    /**
     * Generates {@link Cato}
     * @return  {@link Cato}
     * @see #background()
     * @see #body()
     * @see #face()
     * @see #paws()
     * @see #collar()
     */
    public Cato generate() {
        background();
        body();
        face();
        paws();
        collar();

        return cato;
    }

    /**
     * Generates background
     */
    private void background() {
        int color = generateColor();

        cato.setColor(Cato.BACKGROUND, color);
    }

    /**
     * Generates body
     */
    private void body() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        int color = Color.argb(255, red, green, blue);

        cato.setColor(Cato.HEAD, color);
        cato.setColor(Cato.BODY, color);
        cato.setColor(Cato.LEG_RIGHT_FRONT, color);
        cato.setColor(Cato.LEG_RIGHT_BACK, color);
        cato.setColor(Cato.LEG_LEFT_FRONT, color);
        cato.setColor(Cato.LEG_LEFT_BACK, color);
        cato.setColor(Cato.TAIL, color);
        cato.setColor(Cato.EAR_RIGHT_IN, color);
        cato.setColor(Cato.EAR_LEFT_IN, color);

        double luminance = (0.2126 * red + 0.7152 * green + 0.0722 * blue) / 255;
        if (luminance > 0.2) {
            lightBody = true;
        }
    }

    /**
     * Generates face
     */
    private void face() {
        if (lightBody) {
            int color = Color.BLACK;

            cato.setColor(Cato.EYES, color);
            cato.setColor(Cato.MOUTH, color);
        } else {
            int color = Color.argb(255, 200, 200, 200);

            cato.setColor(Cato.EYES, color);
            cato.setColor(Cato.MOUTH, color);
        }
    }

    /**
     * Generate paws
     */
    private void paws() {
        int variant = random.nextInt(9);

        switch (variant) {
            case 0: {
                int color = generateColor();

                cato.setColor(Cato.PAW_RIGHT_FRONT, color);
                cato.setColor(Cato.PAW_RIGHT_BACK, color);
                cato.setColor(Cato.PAW_LEFT_FRONT,color);
                cato.setColor(Cato.PAW_LEFT_BACK, color);
                cato.setColor(Cato.TAIL_TOP, color);
                head_top(color);
                break;
            }

            case 1: {
                int color1 = generateColor();
                int color2 = generateColor();

                cato.setColor(Cato.PAW_RIGHT_FRONT, color1);
                cato.setColor(Cato.PAW_RIGHT_BACK, color1);
                cato.setColor(Cato.PAW_LEFT_FRONT,color2);
                cato.setColor(Cato.PAW_LEFT_BACK, color2);
                cato.setColor(Cato.TAIL_TOP, color1);
                head_top(color2);
                break;
            }

            case 2: {
                int color1 = generateColor();
                int color2 = generateColor();

                cato.setColor(Cato.PAW_RIGHT_FRONT, color1);
                cato.setColor(Cato.PAW_RIGHT_BACK, color2);
                cato.setColor(Cato.PAW_LEFT_FRONT,color1);
                cato.setColor(Cato.PAW_LEFT_BACK, color1);
                cato.setColor(Cato.TAIL_TOP, color1);
                head_top(color2);
                break;
            }

            case 3: {
                int color1 = generateColor();
                int color2 = generateColor();

                cato.setColor(Cato.PAW_RIGHT_FRONT, color1);
                cato.setColor(Cato.PAW_RIGHT_BACK, color2);
                cato.setColor(Cato.PAW_LEFT_FRONT,color2);
                cato.setColor(Cato.PAW_LEFT_BACK, color1);
                cato.setColor(Cato.TAIL_TOP, color1);
                head_top(color2);
                break;
            }

            case 4: {
                int color1 = Color.WHITE;
                int color2 = Color.BLACK;

                cato.setColor(Cato.PAW_RIGHT_FRONT, color1);
                cato.setColor(Cato.PAW_RIGHT_BACK, color1);
                cato.setColor(Cato.PAW_LEFT_FRONT,color2);
                cato.setColor(Cato.PAW_LEFT_BACK, color2);
                cato.setColor(Cato.TAIL_TOP, color1);
                head_top(color2);
                break;
            }

            case 5: {
                int color1 = Color.WHITE;
                int color2 = Color.BLACK;

                cato.setColor(Cato.PAW_RIGHT_FRONT, color1);
                cato.setColor(Cato.PAW_RIGHT_BACK, color2);
                cato.setColor(Cato.PAW_LEFT_FRONT,color1);
                cato.setColor(Cato.PAW_LEFT_BACK, color2);
                cato.setColor(Cato.TAIL_TOP, color1);
                head_top(color2);
                break;
            }

            case 6: {
                int color1 = Color.WHITE;
                int color2 = Color.BLACK;

                cato.setColor(Cato.PAW_RIGHT_FRONT, color1);
                cato.setColor(Cato.PAW_RIGHT_BACK, color2);
                cato.setColor(Cato.PAW_LEFT_FRONT,color2);
                cato.setColor(Cato.PAW_LEFT_BACK, color1);
                cato.setColor(Cato.TAIL_TOP, color1);
                head_top(color2);
                break;
            }

            case 7: {
                int color = Color.WHITE;

                cato.setColor(Cato.PAW_RIGHT_FRONT, color);
                cato.setColor(Cato.PAW_RIGHT_BACK, color);
                cato.setColor(Cato.PAW_LEFT_FRONT,color);
                cato.setColor(Cato.PAW_LEFT_BACK, color);
                cato.setColor(Cato.TAIL_TOP, color);
                head_top(color);
                break;
            }

            case 8: {
                int color = Color.BLACK;

                cato.setColor(Cato.PAW_RIGHT_FRONT, color);
                cato.setColor(Cato.PAW_RIGHT_BACK, color);
                cato.setColor(Cato.PAW_LEFT_FRONT,color);
                cato.setColor(Cato.PAW_LEFT_BACK, color);
                cato.setColor(Cato.TAIL_TOP, color);
                head_top(color);
                break;
            }
        }
    }

    /**
     * Generates top of the head
     * @param color Color
     */
    private void head_top(int color) {
        boolean visible = random.nextBoolean();

        if (visible) {

            cato.setColor(Cato.HEAD_TOP, color);
        } else {
            color = cato.getColor(Cato.HEAD);
            cato.setColor(Cato.HEAD_TOP, color);
        }
    }

    /**
     * Generates collar
     */
    private void collar() {
        int color = generateColor();
        cato.setColor(Cato.COLLAR, color);
    }

    /**
     * Generates random color
     * @return  Color
     */
    private int generateColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        return Color.argb(255, red, green, blue);
    }
}
