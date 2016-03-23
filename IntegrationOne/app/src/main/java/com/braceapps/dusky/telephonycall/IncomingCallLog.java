package com.braceapps.dusky.telephonycall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.braceapps.dusky.Servicehandler.InitStatus;
import com.braceapps.dusky.telephonymessage.services.MessagLogService;


/**
 * Created by Paresh on 1/27/2015.
 */
public class IncomingCallLog extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        InitStatus objInit = new InitStatus();
        long startTime = 0;
        if (null == bundle)
            return;
        InitStatus objinit = new InitStatus();
        String m_sendLog=objinit.getSpecificParameter(context, InitStatus.sendlog);
        if(m_sendLog!=null)
        if(!m_sendLog.matches("1"))
            return;


        String phoneState = bundle.getString(TelephonyManager.EXTRA_STATE);

        if (phoneState.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
            String phonenumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            //Toast.makeText(context, phonenumber, Toast.LENGTH_LONG).show();
            objInit.storeCallStatus(context, "1", phonenumber);
            startTime = System.currentTimeMillis();

        }
        if (phoneState.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
            String oldState = new InitStatus().getstoreCallStatus(context);
            if (!TextUtils.isEmpty(oldState)) {
                if (oldState.matches("1")) {
                    String phonenumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    if (!TextUtils.isEmpty(phonenumber)) {
                       // Toast.makeText(context, "missed Call" + phonenumber, Toast.LENGTH_LONG).show();
                        objInit.storeCallStatus(context, "-1");
                        Intent service = new Intent(context, MessagLogService.class);
                        service.putExtra("title", "Missed Call");
                        service.putExtra("msg", "NA");
                        service.putExtra("contactNumber", phonenumber);
                        //service.putExtra("contactName", phonenumber);
                        context.startService(service);
                        //objInit.storeCallStatus(context, "-1");

                    }
                } else  {
                    //Toast.makeText(context, "Call Duration" + (System.currentTimeMillis() - startTime) / 2, Toast.LENGTH_LONG).show();
                    objInit.storeCallStatus(context, "-1");
                }
            }
        }
        if (phoneState.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            String oldState = new InitStatus().getstoreCallStatus(context);
            if (!TextUtils.isEmpty(oldState)) {
                if (oldState.matches("1")) {
                    String phonenumber = objInit.getstoreCallPhoneNumeber(context);
                   // Toast.makeText(context, "Recieved Call" + phonenumber, Toast.LENGTH_LONG).show();

                    objInit.storeCallStatus(context, "-1");
                    Intent service = new Intent(context, MessagLogService.class);
                    service.putExtra("title", "Recieved Call");
                    service.putExtra("msg", "NA");
                    service.putExtra("contactNumber", phonenumber);
                    service.putExtra("contactName", phonenumber);
                    context.startService(service);


                }else {
                //Toast.makeText(context, "Call Duration" + (System.currentTimeMillis() - startTime) / 2, Toast.LENGTH_LONG).show();
                    objInit.storeCallStatus(context, "-1");
            }


            }
        }

    }

}
