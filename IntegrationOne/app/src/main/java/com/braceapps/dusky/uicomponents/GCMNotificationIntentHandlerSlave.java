package com.braceapps.dusky.uicomponents;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.braceapps.dusky.activities.SlaveActivity;
import com.braceapps.dusky.R;

/**
 * Created by paresh.boraste on 8/4/2015.
 */
public class GCMNotificationIntentHandlerSlave extends IntentService {
    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMNotificationIntentHandlerSlave() {
        super("GCMNotificationIntentHandlerSlave");
    }

    public static final String TAG = "GCMNotificationIntentHandlerSlave";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        sendNotification(extras.get("title").toString(), extras.get("msg").toString(), extras.get("contactNumber").toString(), extras.get("contactName").toString());

    }

    public void sendNotification(String title, String msg, String contactNumber, String contactName) {
        // Log.d(TAG, "Preparing to send notification...: " + msg);


        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SlaveActivity.class), 0);


        NotificationCompat.Builder mBuilder=null;

        if (!msg.matches("NA")) {
            mBuilder = new NotificationCompat.Builder(
                    this).setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Fravas")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(title + "\n " + msg);

        } else {
            mBuilder = new NotificationCompat.Builder(
                    this).setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Fravas")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(contactName))
                    .setContentText(title + "\n" + contactName + "\n" + contactNumber);

        }

        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);

        mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());

    }
    }