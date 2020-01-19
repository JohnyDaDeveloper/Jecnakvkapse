package cz.johnyapps.jecnakvkapse.Suplarch;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.util.List;

import cz.johnyapps.jecnakvkapse.Dialogs.DialogLoading;
import cz.johnyapps.jecnakvkapse.HttpConnection.DownloadFile;
import cz.johnyapps.jecnakvkapse.HttpConnection.Request;
import cz.johnyapps.jecnakvkapse.HttpConnection.ResultErrorProcess;
import cz.johnyapps.jecnakvkapse.Suplarch.SuplarchLinky.SuplarchLink;
import cz.johnyapps.jecnakvkapse.Tools.GenericFileProvider;

/**
 * Stáhne soubor se suplováním a pošle intent pro jeho otevření aplikací, která umí zpracovávat application/vnd.ms-excel.
 * @see SuplarchLink
 * @see DownloadFile
 */
public class StahniSuplarch {
    private static final String TAG = "StahniSuplarch";

    private Context context;
    private SuplarchLink link;

    /**
     * Inicializace
     * @param context   Context
     */
    public StahniSuplarch(Context context) {
        this.context = context;
    }

    /**
     * Stáhne suplarch
     * @param link  {@link SuplarchLink}
     */
    public void stahni(SuplarchLink link) {
        this.link = link;

        DialogLoading dialog = new DialogLoading(context);
        Request request = new Request(link.getLink(), "GET", new File(context.getCacheDir(), link.getDocName()));

        @SuppressLint("StaticFieldLeak") DownloadFile download = new DownloadFile(dialog.get(link.getDocName())) {
            @Override
            public void nextAction(String result) {
                super.nextAction(result);

                ResultErrorProcess process = new ResultErrorProcess(context);

                if (process.process(result)) {
                    onResult();
                }
            }
        };
        download.execute(request);
    }

    /**
     * Spustí se při dokončení stahování
     */
    private void onResult() {
        if (link != null) {
            openSuplarch(link);
        }
    }

    /**
     * Otevře suplování v aplikace schopné zpracovat application/vnd.ms-excel
     * @param link  {@link SuplarchLink}
     * @see GenericFileProvider
     */
    private void openSuplarch(SuplarchLink link) {
        Crashlytics.log(Log.INFO, TAG, "Getting downloads directory");
        File file = new File(context.getCacheDir(), link.getDocName());

        Crashlytics.log(Log.INFO, TAG, "Getting file provider");
        Uri uri = GenericFileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);

        Crashlytics.log(Log.INFO, TAG, "Creating intent");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Crashlytics.log(Log.INFO, TAG, "Granting permissions");
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        Crashlytics.log(Log.INFO, TAG, "Opening");
        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Crashlytics.log(Log.INFO, TAG, "Opening failed! No app found.");
            Toast.makeText(context, "Nenalezena žádná app pro otevření suplarchu (.xls)", Toast.LENGTH_SHORT).show();
        }
    }
}
