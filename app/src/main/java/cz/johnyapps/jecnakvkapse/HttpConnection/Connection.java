package cz.johnyapps.jecnakvkapse.HttpConnection;

import android.support.v7.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import cz.johnyapps.jecnakvkapse.Singletons.User;

/**
 * Přečte obsah požadované stránky
 * @see Request
 */
public class Connection extends AsyncTask<Request, Request, String> {
    private static final String TAG = "Connection";

    private AlertDialog dialog = null;
    private User user;

    /**
     * Inicializace
     * @param dialog    Dialog zobrazený během stahování
     */
    protected Connection(AlertDialog dialog) {
        this.dialog = dialog;
        this.user = User.getUser();
    }

    /**
     * Inicializace
     */
    protected Connection() {
        this.user = User.getUser();
    }

    /**
     * Zobrazí dialog před začátkem čtení
     */
    @Override
    protected void onPreExecute() {
        if (dialog != null) dialog.show();

        super.onPreExecute();
    }

    /**
     * V pozadí se připojí ke zdroji, přečte jeho obsah a výsledek pošle dále ke zpracování {@link #onPostExecute(String)}
     * @param request   Dotaz
     * @return          Výsledek čtení
     * @see Request
     */
    @Override
    protected String doInBackground(final Request... request) {
        Request r = request[0];

        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        String ERROR_REASON;

        try {
            URL url = new URL("https://www.spsejecna.cz/" + r.getDotaz());

            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(r.getMethod());
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(15000);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);
            if (r.getMethod().equals("POST")) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setRequestProperty("Content-Length", Integer.toString(r.getData().getBytes(Charset.forName("UTF-8")).length));
            }
            if (user.getSessionId() != null) {
                connection.setRequestProperty("Cookie", user.getSessionId());
            }
            connection.connect();

            if (r.getMethod().equals("POST")) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write(r.getData());
                writer.close();
            }

            processHeaders(connection.getHeaderFields().entrySet());

            InputStream stream;

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                stream = connection.getInputStream();
            } else {
                stream = connection.getErrorStream();
            }

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder text = new StringBuilder();
            String line;

            boolean first = true;

            while ((line = reader.readLine()) != null) {
                text.append(line);

                if (!first) {
                    text.append(System.getProperty("line.separator"));
                } else {
                    first = false;
                }
            }

            return text.toString();
        } catch (SocketTimeoutException e) {
            ERROR_REASON = "TIMEOUT";
        } catch (IOException e) {
            ERROR_REASON = "ERROR";
        } finally {
            if (connection != null) connection.disconnect();

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return ERROR_REASON;
    }

    /**
     * Zpracuje hlavičku
     * @param headers   Hlavička
     */
    private void processHeaders(Set<Map.Entry<String, List<String>>> headers) {
        for (Map.Entry<String, List<String>> entries : headers) {
            if (entries.getKey() != null) {
                if (entries.getKey().equals("Set-Cookie")) {
                    processCookies(entries);
                }
            }
        }
    }

    /**
     * Zpracuje cookies
     * @param entries   Vstupy
     */
    private void processCookies(Map.Entry<String, List<String>> entries) {
        for (String value : entries.getValue()) {
            String[] cookies = value.split(";");

            for (String cookie : cookies) {
                String[] data = value.split("=");

                if (data[0].equals("JSESSIONID")) {
                    user.setSessionId(cookie + ";");
                    break;
                }
            }
        }
    }

    /**
     * Schová dialog po dokončení stahování a pošle data k dalšímu zpracování {@link #nextAction(String)}
     * @param result    Výsledek čtení
     */
    @Override
    protected void onPostExecute(String result) {
        if (dialog != null) dialog.dismiss();
        Log.i(TAG, result);

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
