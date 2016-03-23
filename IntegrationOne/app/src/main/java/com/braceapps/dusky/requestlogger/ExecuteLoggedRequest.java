package com.braceapps.dusky.requestlogger;

import android.content.Context;
import android.content.Intent;

import com.braceapps.dusky.Servicehandler.InitStatus;
import com.braceapps.dusky.db.dbhandler.DBManagerLogger;
import com.braceapps.dusky.db.dbhandler.RequestLoggerPOJO;
import com.braceapps.dusky.telephonymessage.services.MessagLogService;

import java.util.ArrayList;

/**
 * Created by Paresh on 3/14/2015.
 */
public class ExecuteLoggedRequest {
    public void executeAllLoggedRequest(Context mContext) {
        DBManagerLogger loggerManager = new DBManagerLogger(mContext);
        ArrayList<RequestLoggerPOJO> list = new ArrayList<RequestLoggerPOJO>();
        list = loggerManager.getAllRequests();
        if (list != null) {
            InitStatus initobj = new InitStatus();
            for (int i = 0; i < list.size(); i++) {
                RequestLoggerPOJO pj = list.get(i);
                if (initobj.getSpecificParameter(mContext, InitStatus.network).matches("1")) {
                    if (pj.gettitle().matches("Recieved Call") || pj.gettitle().matches("Missed Call") || pj.gettitle().matches("Dialed Call")) {
                        Intent service = new Intent(mContext, MessagLogService.class);
                        service.putExtra("title", pj.gettitle());
                        service.putExtra("msg", "NA");
                        service.putExtra("contactNumber", pj.getContactNumber());
                        service.putExtra("contactName", pj.getContactNumber());
                        mContext.startService(service);
                    } else if (pj.gettitle().matches("msg")) {
                        Intent service = new Intent(mContext, MessagLogService.class);
                        service.putExtra("title", "msg");
                        service.putExtra("contactNumber", pj.getContactNumber());
                        service.putExtra("contactName", "Unknown");
                        service.putExtra("msg", pj.getMessage());
                        mContext.startService(service);
                    }

                    loggerManager.deleteSingleEntry(pj.getid());
                }
            }

        }
    }
}
