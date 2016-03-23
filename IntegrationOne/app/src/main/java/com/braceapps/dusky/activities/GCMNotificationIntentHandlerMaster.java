package com.braceapps.dusky.activities;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.braceapps.dusky.MainActivity;
import com.braceapps.dusky.R;
import com.braceapps.dusky.Servicehandler.InitStatus;
import com.braceapps.dusky.db.dbhandler.DBManagerCall_logs;
import com.braceapps.dusky.db.dbhandler.DBManagerMessage;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Paresh on 1/12/2015.
 */
public class GCMNotificationIntentHandlerMaster extends IntentService {

    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMNotificationIntentHandlerMaster() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString(), "", "", "");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        + extras.toString(), "", "", "");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                    if(extras.get("title")!=null) {
                        sendNotification(extras.get("title").toString(), extras.get("msg").toString(), extras.get("contactNumber").toString(), extras.get("contactName").toString());
                    }

            }
        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    public void sendNotification(String title, String msg, String contactNumber, String contactName) {
       // Log.d(TAG, "Preparing to send notification...: " + msg);
        InitStatus objinit = new InitStatus();
        String srmsg = objinit.getSpecificParameter(this, InitStatus.srmessage);
        String srcall = objinit.getSpecificParameter(this, InitStatus.srcall);
        Boolean defaultCondition=title.matches("msg") && srmsg.matches("1") || title.contains("Call") && srcall.matches("1");
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = null;

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        String notification = objinit.getSpecificParameter(this, InitStatus.showNotification);

        if (defaultCondition) {

            if(contactName.matches("NotinContactList")){
                contactName=contactNumber;
            }
            if (notification.matches("1")) {

                if (!msg.matches("NA") ) {

                        mBuilder = new NotificationCompat.Builder(
                                this).setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle("Brace")
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                                .setContentText(title + "\n " + msg)
                                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
                    mBuilder.setAutoCancel(true);

                    mBuilder.setContentIntent(contentIntent);
                    mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());


                } else {

                        mBuilder = new NotificationCompat.Builder(
                                this).setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle("Brace")
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(contactName))
                                .setContentText(title + "\n" + contactName + "\n" + contactNumber)
                                .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI);
                    mBuilder.setAutoCancel(true);

                    mBuilder.setContentIntent(contentIntent);
                    mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());


                }
            }
        }else {
            Boolean otherRequestConditions=title.matches("Pairing Request") || title.matches("Request Denied") || title.matches("Paired") || title.matches("UnPaired");

            if(otherRequestConditions ) {
                mBuilder = new NotificationCompat.Builder(
                        this).setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Brace")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(contactName))
                        .setContentText(title)
                        .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI);
                mBuilder.setAutoCancel(true);

                mBuilder.setContentIntent(contentIntent);
                mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());

            }
        }
      /*  if (notification.matches("1") && flag==1) {
            mBuilder.setAutoCancel(true);

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());
        }*/



                String currentDateandTime = new SimpleDateFormat("MM/dd HH:mm").format(new Date());

                if (title.matches("Pairing Request")) {
                    objinit.storetSpecificParameter(getApplicationContext(), InitStatus.pairing, "1");
                    objinit.storetSpecificParameter(getApplicationContext(), InitStatus.stateActivity, "8");
                    objinit.storetSpecificParameter(getApplicationContext(), InitStatus.pairId, contactNumber);

                    //do something here
                }
                if (title.matches("Request Denied")) {
                    objinit.storetSpecificParameter(getApplicationContext(), InitStatus.pairing, "0");
                    objinit.storetSpecificParameter(getApplicationContext(), InitStatus.stateActivity, "2");
                    //do something here
                }
                if (title.matches("Paired")) {
                    objinit.storetSpecificParameter(getApplicationContext(), InitStatus.pairing, "2");
                    if (objinit.getSpecificParameter(getApplicationContext(), InitStatus.stateActivity).matches("7")) {
                        objinit.storetSpecificParameter(getApplicationContext(), InitStatus.sendlog, "1");
                        //objinit.storetSpecificParameter(getApplicationContext(), InitStatus.dataSlave, "1");
                        objinit.storetSpecificParameter(getApplicationContext(), InitStatus.stateActivity, "6");
                    } else {
                        objinit.storetSpecificParameter(getApplicationContext(), InitStatus.stateActivity, "3");
                    }
                }
                if (title.matches("UnPaired")) {
                    objinit.storetSpecificParameter(getApplicationContext(), InitStatus.pairing, "0");
                    objinit.storetSpecificParameter(getApplicationContext(), InitStatus.sendlog, "0");
                    objinit.storetSpecificParameter(getApplicationContext(), InitStatus.stateActivity, "2");
                }
                if(defaultCondition) {
                    if (title.matches("msg")) {
                        objinit.storetSpecificParameter(getApplicationContext(), InitStatus.datam, "1");
                        new DBManagerMessage(this).addMessagetoLocal(contactNumber, msg, currentDateandTime, "0", contactName);
                        updateMymsgActivity(getApplicationContext(), contactNumber + "," + msg + "," + currentDateandTime + "," + contactName);
                    }
                    if (title.matches("Missed Call") || title.matches("Recieved Call") || title.matches("Dialed Call")) {
                        objinit.storetSpecificParameter(getApplicationContext(), InitStatus.datac, "1");
                        new DBManagerCall_logs(this).addCalltoLocal(contactNumber, currentDateandTime, title, contactName);
                        updateMyActivity(getApplicationContext(), contactNumber + "," + contactName + "," + title + "," + currentDateandTime);

                    }
                }


        }

    static void updateMyActivity(Context context, String message) {

        Intent intent = new Intent("CallLogUpdate");

        //put whatever data you want to send, if any
        intent.putExtra("calls", message);

        //send broadcast
        context.sendBroadcast(intent);
    }
    static void updateMymsgActivity(Context context, String message) {
        Intent intent = new Intent("MessageLogUpdate");

        //put data to send use ...soon will replaced by event bus
        intent.putExtra("messages", message);

        //sending broadcast
        context.sendBroadcast(intent);
    }

}


