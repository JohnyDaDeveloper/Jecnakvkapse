package cz.johnyapps.jecnakvkapse.Tools;

import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

@SuppressWarnings("unused")
public class Logger {
    private static final String TAG = "Logger";

    public static final Logger instance = new Logger();
    private boolean crashlyticsEnabled = false;
    private boolean initialized = false;

    public Logger() {

    }

    public void initialize(boolean enableCrashlytics) {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(enableCrashlytics);
        this.crashlyticsEnabled = enableCrashlytics;

        initialized = true;
        Logger.i(TAG, "Crashlytics " + (enableCrashlytics ? "enabled" : "disabled"));
    }

    public void enableCrashlytics() {
        crashlyticsEnabled = true;
        Logger.i(TAG, "enableCrashlytics");
    }

    public void disableCrashlytics() {
        crashlyticsEnabled = false;
        Logger.i(TAG, "disableCrashlytics");
    }

    public boolean isCrashlyticsEnabled() {
        return crashlyticsEnabled;
    }

    public static Logger getInstance() {
        return instance;
    }

    private void checkIfInitialized() {
        if (!initialized) {
            Logger.w(TAG, "Logger is not initialized! It may not work correctly");
        }
    }

    public static void v(String tag, String message) {
        Logger.getInstance().checkIfInitialized();

        if (Logger.getInstance().isCrashlyticsEnabled()) {
            FirebaseCrashlytics.getInstance().log(tag + ": " + message);
        }

        Log.v(tag, message);
    }

    public static void d(String tag, String message) {
        Logger.getInstance().checkIfInitialized();
        Log.d(tag, message);
    }

    public static void i(String tag, String message) {
        Logger.getInstance().checkIfInitialized();

        if (Logger.getInstance().isCrashlyticsEnabled()) {
            FirebaseCrashlytics.getInstance().log(tag + ": " + message);
        }

        Log.i(tag, message);
    }

    public static void w(String tag, String message) {
        Logger.getInstance().checkIfInitialized();

        if (Logger.getInstance().isCrashlyticsEnabled()) {
            FirebaseCrashlytics.getInstance().log(tag + ": " + message);
        }

        Log.w(tag, message);
    }

    public static void w(String tag, String message, Exception exception) {
        Logger.getInstance().checkIfInitialized();

        if (Logger.getInstance().isCrashlyticsEnabled()) {
            FirebaseCrashlytics.getInstance().log(tag + ": " + message);
            FirebaseCrashlytics.getInstance().recordException(exception);
        }

        Log.w(tag, message, exception);
    }

    public static void e(String tag, String message) {
        Logger.getInstance().checkIfInitialized();

        if (Logger.getInstance().isCrashlyticsEnabled()) {
            FirebaseCrashlytics.getInstance().log(tag + ": " + message);
        }

        Log.e(tag, message);
    }

    public static void e(String tag, String message, Exception exception) {
        Logger.getInstance().checkIfInitialized();

        if (Logger.getInstance().isCrashlyticsEnabled()) {
            FirebaseCrashlytics.getInstance().log(tag + ": " + message);
            FirebaseCrashlytics.getInstance().recordException(exception);
        }

        Log.e(tag, message, exception);
    }
}
