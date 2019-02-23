package cz.johnyapps.jecnakvkapse.HttpConnection;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

import cz.johnyapps.jecnakvkapse.Singletons.User;

/**
 * Slouží ke stažení souboru
 * @see Request
 */
public class DownloadFile extends AsyncTask<Request, Request, String> {
    private static final String TAG = "Connection";
    private AlertDialog dialog;
    private User user;

    /**
     * Inicializace
     * @param dialog    Dialog zobrazený během stahování
     */
    protected DownloadFile(@Nullable AlertDialog dialog) {
        this.dialog = dialog;
        user = User.getUser();
    }

    /**
     * Zobrazí dialog před začátkem čtení
     */
    @Override
    protected void onPreExecute() {
        if (dialog != null) {
            dialog.show();
        }

        super.onPreExecute();
    }

    /**
     * V pozadí se připojí ke zdroji, přečte jeho obsah a výsledek pošle dále ke zpracování {@link #onPostExecute(String)}
     * @param r Dotaz
     * @return  Výsledek čtení
     * @see Request
     */
    @Override
    public String doInBackground(final Request... r) {
        Request request = r[0];

        HttpsURLConnection connection = null;

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        try {
            URL url = new URL(request.getDotaz());

            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(request.getMethod());
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(15000);
            connection.setDoInput(true);
            if (request.getMethod().equals("POST")) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setRequestProperty("Content-Length", Integer.toString(request.getData().getBytes(Charset.forName("UTF-8")).length));
            }
            if (user.getSessionId() != null) {
                connection.setRequestProperty("Cookie", user.getSessionId());
            }
            connection.connect();

            Log.i(TAG, user.getSessionId() + " - " + request.getDotaz());

            if (request.getMethod().equals("POST")) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write(request.getData());
                writer.close();
            }

            InputStream input = connection.getInputStream();
            File file = new File(request.getPath());
            FileOutputStream output = new FileOutputStream(file);

            int bytesRead;
            byte[] buffer = new byte[4096];
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            return "SUCCESS";
        } catch (FileNotFoundException e) {
            return "FILE_NOT_FOUND";
        } catch (IOException e) {
            e.printStackTrace();

            if (e.getMessage().equals("timeout")) {
                return "TIMEOUT";
            }
        } finally {
            if (connection != null) connection.disconnect();
        }

        return "ERROR";
    }

    /**
     * Schová dialog po dokončení stahování a pošle data k dalšímu zpracování {@link #nextAction(String)}
     * @param result    Výsledek čtení
     */
    @Override
    protected void onPostExecute(String result) {
        if (dialog != null) {
            dialog.dismiss();
        }

        super.onPostExecute(result);

        nextAction(result);
    }

    /**
     * Další zpracování dat
     * @param result    Výsledek čtení
     * @see ResultErrorProcess
     */
    public void nextAction(String result) {

    }
}
