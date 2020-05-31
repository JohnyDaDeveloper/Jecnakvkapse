package cz.johnyapps.jecnakvkapse.Tools;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Ukládá data jako je např. rozvrh do paměti zařízení.
 */
public class OfflineMode {
    private static final String TAG = "OfflineMode";
    private Context context;

    /**
     * Inicializace
     * @param context   Context
     */
    public OfflineMode(Context context) {
        this.context = context;
    }

    /**
     * Zapíše data do souboru
     * @param html  HTML
     * @param what  Název (Přepíše se na LowerCase)
     * @param datum Datum zápisu
     */
    public void write(String html, String what, String datum) {
        try {
            JSONObject object = new JSONObject();
            object.put("html", html);
            object.put("datum", datum);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(what.toLowerCase() + ".json", Context.MODE_PRIVATE));
            outputStreamWriter.write(object.toString());
            outputStreamWriter.close();

            Logger.i(TAG, what.toUpperCase() + " write succesfull");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e(TAG, what.toUpperCase() + " write: IOException");
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e(TAG, what.toUpperCase() + " write: JSONException");
        }
    }

    /**
     * Přečte data ze souboru
     * @param what  Název souboru (Předělá se automaticky do LowerCase)
     * @return      Vrátí data ze souboru
     */
    public String[] read(String what) {
        try {
            InputStream inputStream = context.openFileInput( what.toLowerCase() + ".json");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();

                JSONObject object = new JSONObject(stringBuilder.toString());

                String[] output = new String[2];
                output[0] = object.getString("html");
                output[1] = object.getString("datum");

                Logger.i(TAG, what.toUpperCase() + " read succesfull");
                return output;
            } else {
                Logger.e(TAG, what.toUpperCase() + " read: Null input stream");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Logger.e(TAG, what.toUpperCase() + " read: File not found");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.e(TAG, what.toUpperCase() + " read: IOException");
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e(TAG, what.toUpperCase() + " read: JSONException");
        }

        String[] output = new String[1];
        output[0] = "ERROR";

        return output;
    }
}
