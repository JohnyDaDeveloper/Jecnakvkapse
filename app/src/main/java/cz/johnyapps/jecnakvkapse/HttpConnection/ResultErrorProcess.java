package cz.johnyapps.jecnakvkapse.HttpConnection;

import android.content.Context;
import android.util.Log;

import cz.johnyapps.jecnakvkapse.Dialogs.DialogError;
import cz.johnyapps.jecnakvkapse.Singletons.User;

/**
 * Zprocesuje výsledek čtení a vykoná příslušnou akci
 * @see Connection
 * @see DownloadFile
 */
public class ResultErrorProcess {
    private static final String TAG = "ResultErrorProcess";
    private Context context;

    public ResultErrorProcess(Context context) {
        this.context = context;
    }

    /**
     * Zpracuje výsledek čtení a vykoná příslušnou akci
     * @param result    Výsledek čtení
     * @return          True - výsledek je v pořádku, False - došlo k chybě
     */
    public boolean process(String result) {
        DialogError dialogError = new DialogError(context);

        switch (result) {
            case "TIMEOUT": {
                Log.w(TAG, "Connection Timeout");

                dialogError.get("Vypršel čas spojení").show();
                return false;
            }

            case "ERROR": {
                Log.w(TAG, "Connection Error");

                dialogError.get("Chyba připojení").show();
                return false;
            }

            case "FILE_NOT_FOUND": {
                Log.w(TAG, "File not found");

                dialogError.get("Soubor nenalezen").show();
                return false;
            }

            case "": {
                Log.w(TAG, "Login Error");

                //prefs.edit().remove("pass").apply();
                User.getUser().setLogged(false);

                dialogError.get("Chyba přihlášení: Neplatné jméno nebo heslo").show();
                return false;
            }

            default: {
                break;
            }
        }

        /*try {
            Document doc = new Document(result);
            Element title = doc.selectFirst("title");

            if (title.html().contains("nezdařilo")) {
                Log.w(TAG, "Chyba přihlášení");

                prefs.edit().remove("pass").apply();

                dialogError.get("Chyba přihlášení. Neplatné jméno nebo heslo.").show();
                return false;
            }
        } catch (Exception e) {
            Log.w(TAG, "Špatný vstup");

            dialogError.get("Chyba připojení").show();
            return false;
        }*/

        return true;
    }
}
