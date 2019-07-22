package cz.johnyapps.catoslibrary.Catos.Entity;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.johnyapps.catoslibrary.R;

/**
 * Cato. To draw cato, use {@link cz.johnyapps.catoslibrary.Catos.View.CatoView}
 */
public class Cato {
    private static final String TAG = "Cato";

    public static final int PAW_LEFT_FRONT = 0;
    public static final int PAW_RIGHT_FRONT = 1;
    public static final int PAW_LEFT_BACK = 2;
    public static final int PAW_RIGHT_BACK = 3;
    public static final int TAIL_TOP = 4;
    public static final int TAIL = 5;
    public static final int EAR_LEFT_IN = 6;
    public static final int EAR_RIGHT_IN = 7;
    public static final int EAR_LEFT_OUT = 20;
    public static final int EAR_RIGHT_OUT = 21;
    public static final int HEAD_TOP = 8;
    public static final int HEAD = 9;
    public static final int EYES = 10;
    public static final int MOUTH = 11;
    public static final int COLLAR = 12;
    public static final int BODY = 13;
    public static final int TUMMY = 14;
    public static final int LEG_LEFT_FRONT = 15;
    public static final int LEG_RIGHT_FRONT = 16;
    public static final int LEG_LEFT_BACK = 17;
    public static final int LEG_RIGHT_BACK = 18;
    public static final int BACKGROUND = 19;

    public static final int MOUTH_STYLE_HMM = R.drawable.mouth_0;
    public static final int MOUTH_STYLE_YASS = R.drawable.mouth_1;

    private int color_leftFrontPaw = Color.BLACK;
    private int color_rightFrontPaw = Color.BLACK;
    private int color_leftBackPaw = Color.BLACK;
    private int color_rightBackPaw = Color.BLACK;
    private int color_tailTop = Color.BLACK;
    private int color_tail = Color.argb(255, 128, 51, 0);
    private int color_earLeft_in = Color.argb(255, 128, 51, 0);
    private int color_earRight_in = Color.argb(255, 128, 51, 0);
    private int color_earLeft_out = Color.argb(255, 170, 68, 0);
    private int color_earRight_out = Color.argb(255, 170, 68, 0);
    private int color_headTop = Color.BLACK;
    private int color_head = Color.argb(255, 128, 51, 0);
    private int color_eyes = Color.BLACK;
    private int color_mouth = Color.BLACK;
    private int color_collar = Color.argb(255, 212, 170, 0);
    private int color_body = Color.argb(255, 128, 51, 0);
    private int color_tummy = Color.BLACK;
    private int color_legLeftFront = Color.argb(255, 128, 51, 0);
    private int color_legRightFront = Color.argb(255, 128, 51, 0);
    private int color_legLeftBack = Color.argb(255, 128, 51, 0);
    private int color_legRightBack = Color.argb(255, 128, 51, 0);
    private int color_background = Color.GRAY;

    private int style_mouth = MOUTH_STYLE_HMM;

    public static final List<CatoInfo> INFOS = new ArrayList<>();

    /**
     * Inicializaton
     */
    public Cato() {
        if (INFOS.isEmpty()) {
            INFOS.add(new CatoInfo("Background", BACKGROUND));
            INFOS.add(new CatoInfo("Head Top", HEAD_TOP));
            INFOS.add(new CatoInfo("Face", new int[]{EYES, MOUTH}));
            INFOS.add(new CatoInfo("Ears", new int[]{EAR_LEFT_IN, EAR_RIGHT_IN}));
            INFOS.add(new CatoInfo("Head", HEAD));
            INFOS.add(new CatoInfo("Body", new int[]{BODY, LEG_LEFT_BACK, LEG_LEFT_FRONT, LEG_RIGHT_BACK, LEG_RIGHT_FRONT}));
            INFOS.add(new CatoInfo("Tummy", TUMMY));
            INFOS.add(new CatoInfo("Tail", TAIL));
            INFOS.add(new CatoInfo("Paws", new int[]{PAW_LEFT_BACK, PAW_LEFT_FRONT, PAW_RIGHT_BACK, PAW_RIGHT_FRONT}));
            INFOS.add(new CatoInfo("Tail Top", TAIL_TOP));
            INFOS.add(new CatoInfo("Collar", COLLAR));
        }
    }

    /**
     * Saves cato to file "cato.json"
     * @param context   Context
     * @see #save(Context, FileOutputStream)
     */
    public void save(Context context) {
        try {
            FileOutputStream output = context.openFileOutput("cato.json", Context.MODE_PRIVATE);
            save(context, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves cato to given file
     * @param context   Context
     * @param file      File
     * @see #save(Context, FileOutputStream)
     */
    public void saveToFile(Context context, File file) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            save(context, stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes cato with given {@link FileOutputStream}
     * @param context   Context
     * @param output    Output stream
     */
    private void save(Context context, FileOutputStream output) {
        try {
            JSONObject cato = new JSONObject();
            JSONObject colors = new JSONObject();
            JSONObject styles = new JSONObject();

            colors.put("background", color_background);
            colors.put("head_top", color_headTop);
            colors.put("face", color_eyes);
            colors.put("ears_in", color_earLeft_in);
            colors.put("ears_out", color_earLeft_out);
            colors.put("head", color_head);
            colors.put("body", color_body);
            colors.put("tummy", color_tummy);
            colors.put("tail", color_tail);
            colors.put("paws", color_leftBackPaw);
            colors.put("tail_top", color_tailTop);
            colors.put("collar", color_collar);

            styles.put("mouth_0", style_mouth);

            cato.put("colors", colors);
            cato.put("styles", styles);

            char[] chars = cato.toString().toCharArray();

            for (char c : chars) {
                output.write((int) c);
            }

            output.close();

            Toast.makeText(context, "Saved!", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads cato from "cato.json"
     * @param context   Context
     * @see #load(FileInputStream)
     */
    public void load(Context context) {
        try {
            File file = new File("cato.json");
            FileInputStream fileInput = context.openFileInput(file.getPath());
            load(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads cato from given file
     * @param file  File
     * @see #load(FileInputStream)
     */
    public void loadFromFile(File file) {
        try {
            FileInputStream fileInput = new FileInputStream(file);
            load(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads file from raw resources
     * @param context   Context
     * @param file      File name (without suffix)
     * @see #load(InputStreamReader)
     */
    public void loadFromRawResources(Context context, String file) {
        try {
            int id = context.getResources().getIdentifier(file, "raw", context.getPackageName());
            InputStream inputStream = context.getResources().openRawResource(id);
            InputStreamReader input = new InputStreamReader(inputStream);

            load(input);

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads cato with given {@link FileInputStream}
     * @param fileInput    Input stream
     * @see #load(InputStreamReader)
     */
    private void load(FileInputStream fileInput) {
        try {
            InputStreamReader input = new InputStreamReader(fileInput);

            load(input);

            fileInput.close();
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads cato from file using given {@link InputStreamReader}
     * @param input Input stream
     */
    private void load(InputStreamReader input) {
        try {
            BufferedReader bufferedReader = new BufferedReader(input);
            String receiveString;
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
            }

            input.close();
            String data = stringBuilder.toString();

            JSONObject cato = new JSONObject(data);
            JSONObject colors = cato.getJSONObject("colors");
            JSONObject styles = cato.getJSONObject("styles");

            if (colors.has("background")) {
                color_background = colors.getInt("background");
            }

            if (colors.has("head_top")) {
                color_headTop = colors.getInt("head_top");
            }

            if (colors.has("face")) {
                color_eyes = colors.getInt("face");
                color_mouth = colors.getInt("face");
            }

            if (colors.has("ears_in")) {
                color_earLeft_in = colors.getInt("ears_in");
                color_earRight_in = colors.getInt("ears_in");
            }

            if (colors.has("ears_out")) {
                color_earLeft_out = colors.getInt("ears_out");
                color_earRight_out = colors.getInt("ears_out");
            }

            if (colors.has("head")) {
                color_head = colors.getInt("head");
            }

            if (colors.has("body")) {
                color_body = colors.getInt("body");
                color_legLeftBack = colors.getInt("body");
                color_legLeftFront = colors.getInt("body");
                color_legRightBack = colors.getInt("body");
                color_legRightFront = colors.getInt("body");
            }

            if (colors.has("tummy")) {
                color_tummy = colors.getInt("tummy");
            }

            if (colors.has("tail")) {
                color_tail = colors.getInt("tail");
            }

            if (colors.has("paws")) {
                color_leftFrontPaw = colors.getInt("paws");
                color_leftBackPaw = colors.getInt("paws");
                color_rightFrontPaw = colors.getInt("paws");
                color_rightBackPaw = colors.getInt("paws");
            }

            if (colors.has("tail_top")) {
                color_tailTop = colors.getInt("tail_top");
            }

            if (colors.has("collar")) {
                color_collar = colors.getInt("collar");
            }

            if (styles.has("mouth")) {
                style_mouth = styles.getInt("mouth");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculate color of outer ears
     * @param color Color of inner ears
     * @return      Color
     */
    private int calculateEarOut(int color) {
        int red;
        int green;
        int blue;

        if (Color.red(color) > 200) {
            red = Color.red(color) - 40 > 0 ? Color.red(color) - 40 : 0;
        } else {
            red = Color.red(color) + 40 < 256 ? Color.red(color) + 40 : 0;
        }

        if (Color.green(color) > 200) {
            green = Color.green(color) - 40 > 0 ? Color.green(color) - 40 : 0;
        } else {
            green = Color.green(color) + 40 < 256 ? Color.green(color) + 40 : 0;
        }

        if (Color.blue(color) > 200) {
            blue = Color.blue(color) - 40 > 0 ? Color.blue(color) - 40 : 0;
        } else {
            blue = Color.blue(color) + 40 < 256 ? Color.blue(color) + 40 : 0;
        }

        return Color.argb(255, red, green, blue);
    }

    /**
     * Calculate color of inner ears
     * @param color Color of outer ears
     * @return      Color of inner ears
     */
    private int calculateEarIn(int color) {
        int red;
        int green;
        int blue;

        if (Color.red(color) > 200) {
            red = Color.red(color) - 40 > 0 ? Color.red(color) + 40 : 0;
        } else {
            red = Color.red(color) + 40 < 256 ? Color.red(color) - 40 : 0;
        }

        if (Color.green(color) > 200) {
            green = Color.green(color) - 40 > 0 ? Color.green(color) + 40 : 0;
        } else {
            green = Color.green(color) + 40 < 256 ? Color.green(color) - 40 : 0;
        }

        if (Color.blue(color) > 200) {
            blue = Color.blue(color) - 40 > 0 ? Color.blue(color) + 40 : 0;
        } else {
            blue = Color.blue(color) + 40 < 256 ? Color.blue(color) - 40 : 0;
        }

        return Color.argb(255, red, green, blue);
    }

    /**
     * Return color of body part
     * @param id    Id of body part
     * @return      Color
     */
    public int getColor(int id) {
        switch (id) {
            case PAW_LEFT_FRONT: {
                return color_leftFrontPaw;
            }

            case PAW_RIGHT_FRONT: {
                return color_rightFrontPaw;
            }

            case PAW_LEFT_BACK: {
                return color_leftBackPaw;
            }

            case PAW_RIGHT_BACK: {
                return color_rightBackPaw;
            }

            case TAIL_TOP: {
                return color_tailTop;
            }

            case TAIL: {
                return color_tail;
            }

            case EAR_LEFT_IN: {
                return color_earLeft_in;
            }

            case EAR_RIGHT_IN: {
                return color_earRight_in;
            }

            case EAR_LEFT_OUT: {
                return color_earLeft_out;
            }

            case EAR_RIGHT_OUT: {
                return color_earRight_out;
            }

            case HEAD_TOP: {
                return color_headTop;
            }

            case HEAD: {
                return color_head;
            }

            case EYES: {
                return color_eyes;
            }

            case MOUTH: {
                return color_mouth;
            }

            case COLLAR: {
                return color_collar;
            }

            case BODY: {
                return color_body;
            }

            case TUMMY: {
                return color_tummy;
            }

            case LEG_LEFT_FRONT: {
                return color_legLeftFront;
            }

            case LEG_RIGHT_FRONT: {
                return color_legRightFront;
            }

            case LEG_LEFT_BACK: {
                return color_legLeftBack;
            }

            case LEG_RIGHT_BACK: {
                return color_legRightBack;
            }

            case BACKGROUND: {
                return color_background;
            }

            default: {
                Log.w(TAG, "Invalid color");
                return Color.BLACK;
            }
        }
    }

    /**
     * Sets color to body part
     * @param id    Id of body part
     * @param color Color
     */
    public void setColor(int id, int color) {
        switch (id) {
            case PAW_LEFT_FRONT: {
                color_leftFrontPaw = color;
                break;
            }

            case PAW_RIGHT_FRONT: {
                color_rightFrontPaw = color;
                break;
            }

            case PAW_LEFT_BACK: {
                color_leftBackPaw = color;
                break;
            }

            case PAW_RIGHT_BACK: {
                color_rightBackPaw = color;
                break;
            }

            case TAIL_TOP: {
                color_tailTop = color;
                break;
            }

            case TAIL: {
                color_tail = color;
                break;
            }

            case EAR_LEFT_IN: {
                color_earLeft_in = color;
                color_earLeft_out = calculateEarOut(color_earLeft_in);
                break;
            }

            case EAR_RIGHT_IN: {
                color_earRight_in = color;
                color_earRight_out = calculateEarOut(color_earRight_in);
                break;
            }

            case EAR_LEFT_OUT: {
                color_earLeft_out = color;
                color_earLeft_in = calculateEarIn(color_earLeft_out);
                break;
            }

            case EAR_RIGHT_OUT: {
                color_earRight_out = color;
                color_earRight_in = calculateEarIn(color_earRight_out);
                break;
            }

            case HEAD_TOP: {
                color_headTop = color;
                break;
            }

            case HEAD: {
                color_head = color;
                break;
            }

            case EYES: {
                color_eyes = color;
                break;
            }

            case MOUTH: {
                color_mouth = color;
                break;
            }

            case COLLAR: {
                color_collar = color;
                break;
            }

            case BODY: {
                color_body = color;
                break;
            }

            case TUMMY: {
                color_tummy = color;
                break;
            }

            case LEG_LEFT_FRONT: {
                color_legLeftFront = color;
                break;
            }

            case LEG_RIGHT_FRONT: {
                color_legRightFront = color;
                break;
            }

            case LEG_LEFT_BACK: {
                color_legLeftBack = color;
                break;
            }

            case LEG_RIGHT_BACK: {
                color_legRightBack = color;
                break;
            }

            case BACKGROUND: {
                color_background = color;
                break;
            }

            default: {
                Log.w(TAG, "Invalid id");
                break;
            }
        }
    }

    /**
     * Returns style of body part
     * @param id    Id of body part
     * @return      Style
     */
    public int getStyle(int id) {
        switch (id) {
            case MOUTH: {
                return style_mouth;
            }

            default: {
                Log.w(TAG, "Invalid id");
                return 0;
            }
        }
    }

    /**
     * Sets style to body part
     * @param id    Id of body part
     * @param style Style
     */
    public void setStyle(int id, int style) {
        switch (id) {
            case MOUTH: {
                style_mouth = style;
                break;
            }

            default: {
                Log.w(TAG, "Invalid id");
                break;
            }
        }
    }

    /**
     * Saves info about cato
     */
    public class CatoInfo {
        public String name;
        public int[] ids;

        public CatoInfo(String name, int id) {
            this.name = name;
            this.ids = new int[1];
            this.ids[0] = id;
        }

        public CatoInfo(String name, int[] ids) {
            this.name = name;
            this.ids = ids;
        }
    }
}
