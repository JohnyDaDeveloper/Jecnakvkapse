package cz.johnyapps.catoslibrary.Catos.Entity;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

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

    public static final List<CatoInfo> INFOS = new ArrayList<>();

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

    public void save(Context context) {
        try {
            JSONObject cato = new JSONObject();
            cato.put("background", color_background);
            cato.put("head_top", color_headTop);
            cato.put("face", color_eyes);
            cato.put("ears_in", color_earLeft_in);
            cato.put("ears_out", color_earLeft_out);
            cato.put("head", color_head);
            cato.put("body", color_body);
            cato.put("tummy", color_tummy);
            cato.put("tail", color_tail);
            cato.put("paws", color_leftBackPaw);
            cato.put("tail_top", color_tailTop);
            cato.put("collar", color_collar);

            OutputStreamWriter output = new OutputStreamWriter(context.openFileOutput("cato.json", Context.MODE_PRIVATE));
            output.write(cato.toString());
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void load(Context context) {
        try {
            InputStreamReader input = new InputStreamReader(context.openFileInput("cato.json"));
            BufferedReader bufferedReader = new BufferedReader(input);
            String receiveString;
            StringBuilder stringBuilder = new StringBuilder();

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
            }

            input.close();
            String data = stringBuilder.toString();

            JSONObject cato = new JSONObject(data);

            if (cato.has("background")) {
                color_background = cato.getInt("background");
            }

            if (cato.has("head_top")) {
                color_headTop = cato.getInt("head_top");
            }

            if (cato.has("face")) {
                color_eyes = cato.getInt("face");
                color_mouth = cato.getInt("face");
            }

            if (cato.has("ears_in")) {
                color_earLeft_in = cato.getInt("ears_in");
                color_earRight_in = cato.getInt("ears_in");
            }

            if (cato.has("ears_out")) {
                color_earLeft_out = cato.getInt("ears_out");
                color_earRight_out = cato.getInt("ears_out");
            }

            if (cato.has("head")) {
                color_head = cato.getInt("head");
            }

            if (cato.has("body")) {
                color_body = cato.getInt("body");
                color_legLeftBack = cato.getInt("body");
                color_legLeftFront = cato.getInt("body");
                color_legRightBack = cato.getInt("body");
                color_legRightFront = cato.getInt("body");
            }

            if (cato.has("tummy")) {
                color_tummy = cato.getInt("tummy");
            }

            if (cato.has("tail")) {
                color_tail = cato.getInt("tail");
            }

            if (cato.has("paws")) {
                color_leftFrontPaw = cato.getInt("paws");
                color_leftBackPaw = cato.getInt("paws");
                color_rightFrontPaw = cato.getInt("paws");
                color_rightBackPaw = cato.getInt("paws");
            }

            if (cato.has("tail_top")) {
                color_tailTop = cato.getInt("tail_top");
            }

            if (cato.has("collar")) {
                color_collar = cato.getInt("collar");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
