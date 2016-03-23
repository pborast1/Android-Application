package com.braceapps.dusky.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Paresh on 1/12/2015.
 */
public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

            ComponentName comp = new ComponentName(context.getPackageName(),
                    GCMNotificationIntentHandlerMaster.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));
            //setResultCode(Activity.RESULT_OK);   // done to handle un-ordered boradcasts

    }
}