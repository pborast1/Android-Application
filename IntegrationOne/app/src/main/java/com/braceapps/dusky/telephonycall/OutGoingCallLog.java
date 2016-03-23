package com.braceapps.dusky.telephonycall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.braceapps.dusky.Servicehandler.InitStatus;
import com.braceapps.dusky.telephonymessage.services.MessagLogService;

/**
 * Created by Paresh on 1/27/2015.
 */
public class OutGoingCallLog extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String phonenumber = "";
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
// If it is to call (outgoing)
            phonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            InitStatus objinit = new InitStatus();
            String m_sendLog=objinit.getSpecificParameter(context, InitStatus.sendlog);

            if(!m_sendLog.matches("1"))
                return;
            else{
                Intent service = new Intent(context, MessagLogService.class);
                service.putExtra("title", "Dialed Call");
                service.putExtra("msg", "NA");
                service.putExtra("contactNumber", phonenumber);
                service.putExtra("contactName", phonenumber);
                context.startService(service);

            }
            /*Toast.makeText(context, phonenumber, Toast.LENGTH_LONG).show();*/
        }
    }

}