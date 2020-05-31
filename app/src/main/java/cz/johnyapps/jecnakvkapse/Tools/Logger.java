package cz.johnyapps.jecnakvkapse.Tools;

import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class Logger {
    public static final Logger instance = new Logger();
    private boolean crashlyticsEnabled = false;

    public Logger() {

    }

    public void enableCrashlytics() {
        crashlyticsEnabled = true;
    }

    public void disableCrashlytics() {
        crashlyticsEnabled = false;
    }

    public boolean isCrashlyticsEnabled() {
        return crashlyticsEnabled;
    }

    public static Logger getInstance() {
        return instance;
    }

    public static void v(String tag, String message) {
        if (Logger.getInstance().isCrashlyticsEnabled()) {
            FirebaseCrashlytics.getInstance().log(tag + ": " + message);
        } else {
            Log.v(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (Logger.getInstance().isCrashlyticsEnabled()) {
            FirebaseCrashlytics.getInstance().log(tag + ": " + message);
        } else {
            Log.i(tag, message);
        }
    }

    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

    public static void w(String tag, String message) {
        if (Logger.getInstance().isCrashlyticsEnabled()) {
            FirebaseCrashlytics.getInstance().log(tag + ": " + message);
        } else {
            Log.w(tag, message);
        }
    }

    public static void w(String tag, String message, Exception exception) {
        if (Logger.getInstance().isCrashlyticsEnabled()) {
            FirebaseCrashlytics.getInstance().log(tag + ": " + message);
            FirebaseCrashlytics.getInstance().recordException(exception);
        } else {
            Log.w(tag, message, exception);
        }
    }

    public static void e(String tag, String message) {
        if (Logger.getInstance().isCrashlyticsEnabled()) {
            FirebaseCrashlytics.getInstance().log(tag + ": " + message);
        } else {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Exception exception) {
        if (Logger.getInstance().isCrashlyticsEnabled()) {
            FirebaseCrashlytics.getInstance().log(tag + ": " + message);
            FirebaseCrashlytics.getInstance().recordException(exception);
        } else {
            Log.e(tag, message, exception);
        }
    }
}
