package cz.johnyapps.jecnakvkapse.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Slouží k rozpoznání připojení k internetu
 */
public class NetworkState {
    /**
     * Inicializace
     */
    public NetworkState() {

    }

    /**
     * Vrátí stav připojení
     * @param context   Context
     * @return          True - připojen; False - nepřipojen
     */
    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * Slouží k rozpoznání připojení k internetu real-time pomocí {@link ConnectivityReceiverListener}
     */
    public static class Receiver extends BroadcastReceiver {
        private ConnectivityReceiverListener connectivityReceiverListener;

        /**
         * Inicializace
         */
        public Receiver() {
            super();
        }

        /**
         * Zachytí změnu internetového připojení
         * @param context   Context
         * @param intent    Intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            boolean connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            if (connectivityReceiverListener != null) {
                connectivityReceiverListener.onNetworkConnectionChanged(connected);
            }
        }

        /**
         * Listener pro změny připojení
         */
        public interface ConnectivityReceiverListener {
            void onNetworkConnectionChanged(boolean connected);
        }

        /**
         * Nastaví listener
         * @param listener  Listener
         */
        public void setConnectivityReceiverListener(ConnectivityReceiverListener listener) {
            this.connectivityReceiverListener = listener;
        }
    }
}

