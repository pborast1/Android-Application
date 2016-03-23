package com.braceapps.dusky.telephonymessage.OutGoingMessage;

import android.telephony.SmsManager;

public class OutGoingSms {
    public void sendSms(String sender, String msg) {
        SmsManager sms = SmsManager.getDefault();


        sms.sendTextMessage(sender, null, msg, null, null);

    }

}

