package com.braceapps.dusky.activities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.braceapps.dusky.telephonymessage.services.MessagLogService;


public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();


    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                String senderNum = "", message = "";

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    senderNum = phoneNumber;
                    message = currentMessage.getDisplayMessageBody();
                    currentMessage.getTimestampMillis();


                }


                int duration = Toast.LENGTH_LONG;


               /* Toast toast = Toast.makeText(context, "senderNum:toast " + senderNum + ", message: " + message, duration);
                toast.show();
*/

                Intent service = new Intent(context, MessagLogService.class);
                service.putExtra("title", "msg");
                service.putExtra("contactNumber", senderNum);
                service.putExtra("contactName", "Unknown");
                service.putExtra("msg", message);
                context.startService(service);
            } // end for loop
            // bundle is null


        } catch (Exception e) {
           // Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }




}