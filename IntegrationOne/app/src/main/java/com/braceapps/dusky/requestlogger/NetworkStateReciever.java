package com.braceapps.dusky.requestlogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.braceapps.dusky.Servicehandler.InitStatus;

/**
 * Created by Paresh on 3/12/2015.
 */
public class NetworkStateReciever extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        InitStatus objinit = new InitStatus();

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            objinit.storetSpecificParameter(context, InitStatus.network, "1");
           // Toast.makeText(context, "connected", Toast.LENGTH_LONG).show();
            new ExecuteLoggedRequest().executeAllLoggedRequest(context);
        } else {
            objinit.storetSpecificParameter(context, InitStatus.network, "0");
            //Toast.makeText(context, "not connected", Toast.LENGTH_LONG).show();
        }
    }
}

