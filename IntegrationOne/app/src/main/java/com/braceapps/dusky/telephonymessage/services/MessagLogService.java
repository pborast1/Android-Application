package com.braceapps.dusky.telephonymessage.services;

/**
 * Created by Paresh on 1/25/2015.
 *
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.braceapps.dusky.db.dbhandler.DBManagerLogger;
import com.braceapps.dusky.R;
import com.braceapps.dusky.Servicehandler.InitStatus;
import com.braceapps.dusky.activities.SlaveActivity;
import com.braceapps.dusky.contacts.ContactInfo;
import com.braceapps.dusky.telephonymessage.IncomingMessage.MessageForward;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MessagLogService extends IntentService {

    public static  int NOTIFICATION_ID = 1;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p/>
     * param  Used to name the worker thread, important only for debugging.
     */
    public MessagLogService() {
        super("MessagLogService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String title = "";
        String msg = "";
        String contactNumber = "";
        String contactName = "";
        if (bundle != null) {
            title = (String) bundle.get("title");
            msg = (String) bundle.get("msg");
            contactNumber = (String) bundle.get("contactNumber");
            ContactInfo cinfo = new ContactInfo();
            contactName = cinfo.getContactName(this, contactNumber);
            if (contactName == null)
                contactName = "NotinContactList";
        }
        String currentDateandTime = new SimpleDateFormat("MM/dd HH:mm").format(new Date());
        InitStatus initobj = new InitStatus();
        String srmsg = initobj.getSpecificParameter(this, InitStatus.srmessage);
        String srcall = initobj.getSpecificParameter(this, InitStatus.srcall);

        if (title.matches("msg") && srmsg.matches("1") || title.contains("Call") && srcall.matches("1")) {

            if (initobj.getSpecificParameter(this, InitStatus.network).matches("1")) {
                if (initobj.getSpecificParameter(this, InitStatus.dataSlave).matches("0")) {
                    initobj.storetSpecificParameter(this, InitStatus.dataSlave, "1");
                }

                //here is the function which does the sending job
                new DBManagerLogger(this).addToSlaveLog(contactNumber, msg, currentDateandTime, "0", contactName, "NA", title);
                new MessageForward().sendMessageToMaster(title, msg, contactNumber, contactName, this);


                if(initobj.getSpecificParameter(this, InitStatus.showNotification).matches("1")){
                    if(initobj.getSpecificParameter(this, InitStatus.stateActivity).matches("6")){
                        NotificationManager mNotificationManager;
                        NotificationCompat.Builder builder;
                        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                                new Intent(this, SlaveActivity.class), 0);

                        if(contactName.matches("NotinContactList")){
                            contactName=contactNumber;
                        }
                        NotificationCompat.Builder mBuilder=null;
                        if (!msg.matches("NA")) {
                            mBuilder = new NotificationCompat.Builder(
                                    this).setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle("Brace")
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                                    .setContentText("Log sent " + "Text Message" + " " + msg)
                                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

                        } else {
                            mBuilder = new NotificationCompat.Builder(
                                    this).setSmallIcon(R.drawable.ic_launcher)
                                    .setContentTitle("Brace")
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(contactName))
                                    .setContentText("Log sent " + title + " " + contactName + " " + contactNumber)
                                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

                        }
                        mBuilder.setAutoCancel(true);
                        mBuilder.setContentIntent(contentIntent);

                        mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());
                        //  new GCMNotificationIntentHandlerSlave().sendNotification(title, msg, contactNumber, contactName);
                    }
                }
            } else {
                new DBManagerLogger(this).addRequesttoLocal(contactNumber, msg, currentDateandTime, "0", contactName, "NA", title);
                //  Toast.makeText(this, "add in logger", Toast.LENGTH_LONG).show();
            }
        }
    }
}
