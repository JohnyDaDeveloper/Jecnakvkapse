package cz.johnyapps.jecnakvkapse.Tools;

import android.content.Context;
import android.os.AsyncTask;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Stará se o cache aplikace
 */
public class CacheManager {
    private static final String TAG = "CacheManager: ";
    private static final long MAX_SIZE = 5242880L; // 5MB

    private Context context;

    /**
     * Inicializace
     * @param context   Context
     */
    public CacheManager(Context context) {
        this.context = context;
    }

    /**
     * Vymaže cahce přes {@link ClearAll}
     */
    public void clearAll() {
        File[] files = context.getCacheDir().listFiles();

        ClearAll clearAll = new ClearAll();
        clearAll.execute(files);
    }

    /**
     * Zapíše data do cache přes {@link WriteData}
     * @param bytes     Data
     * @param fileName  Soubor který má data obsahovat
     * @throws IOException IOException
     */
    public void writeToCache(byte[] bytes, String fileName) throws IOException {
        File cacheDir = context.getCacheDir();
        RawFile rawFile = new RawFile(bytes, fileName, cacheDir);

        WriteData writeData = new WriteData();
        writeData.execute(rawFile);
    }

    /**
     * Smaže všechny soubory, které se jí pošlou
     */
    static class ClearAll extends AsyncTask<File[], File[], Void> {
        @Override
        protected Void doInBackground(File[]... params) {
            ArrayList<File> files = new ArrayList<>();

            for (File[] param : params) {
                files.addAll(Arrays.asList(param));
            }

            for (File file : files) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }

            return null;
        }
    }

    /**
     * Zapíše data do cache <b>NETESTOVÁNO</b>
     */
    static class WriteData extends AsyncTask<RawFile, RawFile, Void> {
        @Override
        protected Void doInBackground(RawFile... params) {
            for (RawFile rawFile  : params) {
                File[] filesInDir = rawFile.getDir().listFiles();
                long size = 0;

                for (File file : filesInDir) {
                    if (file.isFile()) {
                        size += file.length();
                    }
                }

                while (size > MAX_SIZE) {
                    for (File file : filesInDir) {
                        if (file.isFile()) {
                            size -= file.length();
                            //noinspection ResultOfMethodCallIgnored
                            file.delete();
                        }
                    }
                }

                try {
                    File file = new File(rawFile.getDir(), rawFile.getFileName());
                    FileOutputStream os = new FileOutputStream(file);

                    try {
                        os.write(rawFile.getBytes());
                    } finally {
                        os.flush();
                        os.close();
                    }
                } catch (IOException e) {
                    Crashlytics.log(TAG + "IOException occurred while writing " + rawFile.getFileName());
                }
            }

            return null;
        }
    }

    class RawFile {
        private byte[] bytes;
        private String fileName;
        private File dir;

        RawFile(byte[] bytes, String fileName, File dir) {
            this.bytes = bytes;
            this.fileName = fileName;
            this.dir = dir;
        }

        byte[] getBytes() {
            return bytes;
        }

        File getDir() {
            return dir;
        }

        String getFileName() {
            return fileName;
        }
    }
}
