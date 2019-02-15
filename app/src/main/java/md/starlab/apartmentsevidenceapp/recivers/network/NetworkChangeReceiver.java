package md.starlab.apartmentsevidenceapp.recivers.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import md.starlab.apartmentsevidenceapp.ApartmentsEvidenceApp;
import md.starlab.apartmentsevidenceapp.utils.NetworkUtil;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkChangeReceiver";
    public static NetWorkChangedListener sListener;

    public static void setListener(NetWorkChangedListener listener) {
        sListener = listener;
    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) ApartmentsEvidenceApp.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = NetworkUtil.getConnectivityStatus(context);
        Log.e(TAG, "action: " + intent.getAction());
        Log.d(TAG, "Network status: " + status);
        if (null != sListener) {
            sListener.onNetworkAvailable(NetworkUtil.TYPE_NOT_CONNECTED != status);
        }
    }

    public interface NetWorkChangedListener {
        void onNetworkAvailable(boolean available);
    }
}