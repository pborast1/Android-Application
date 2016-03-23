package com.braceapps.dusky.Servicehandler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.braceapps.dusky.activities.MainTabActivity;
import com.braceapps.dusky.activities.MasterRequestActivity;
import com.braceapps.dusky.activities.OptionActivity;
import com.braceapps.dusky.activities.PreSlaveActivity;
import com.braceapps.dusky.activities.SlaveActivity;
import com.braceapps.dusky.requestlogger.ConnectivityStatus;

/**
 * Created by Paresh on 1/19/2015.
 */
public class InitStatus {

    public static int RI_Stored = 0;
    public static int stateActivity = 1;
    public static int pairing = 2;
    public static int datac = 3;
    public static int datam = 4;

    public static int sendlog = 5;
    public static int network = 6;
    public static int notificationId=7;
    public static int pendingNotification=8;
    public static int pairingRequest=9;
    public static int dataSlave=10;

    public static int srmessage=11;
    public static int srcall=12;
    public static int showNotification=13;


    public static int tncAccepted=14;

    public static int pairId=15;

    public int storeMessageListStatus(Context param_context, String status) {
        int L_status = 0;

        SharedPreferences.Editor editor = param_context.getSharedPreferences("initStatus", 0).edit();
        editor.putString("msg_list", status);
        editor.commit();
        L_status = 1;
        return L_status;
    }

    public int storeCallListStatus(Context param_context, String status) {
        int L_status = 0;

        SharedPreferences.Editor editor = param_context.getSharedPreferences("initStatus", 0).edit();
        editor.putString("call_list", status);
        editor.commit();
        L_status = 1;
        return L_status;
    }

    public String getMessageListStatus(Context param_context) {
        SharedPreferences prefs = param_context.getSharedPreferences("initStatus", 0);
        return (prefs.getString("msg_list", null));
    }

    public String getCallListStatus(Context param_context) {
        SharedPreferences prefs = param_context.getSharedPreferences("initStatus", 0);
        return (prefs.getString("call_list", null));
    }

    public int storePairingStatus(Context param_context, String status) {
        int L_status = 0;

        SharedPreferences.Editor editor = param_context.getSharedPreferences("initStatus", 0).edit();
        editor.putString("pair_Stored", status);
        editor.commit();
        L_status = 1;
        return L_status;
    }

    public int storeCallStatus(Context param_context, String status) {
        int L_status = 0;

        SharedPreferences.Editor editor = param_context.getSharedPreferences("phonecallStatus", 0).edit();
        editor.putString("callStatus", status);
        editor.commit();
        L_status = 1;
        return L_status;
    }

    public int storeCallStatus(Context param_context, String status, String phonenumber) {
        int L_status = 0;

        SharedPreferences.Editor editor = param_context.getSharedPreferences("phonecallStatus", 0).edit();
        editor.putString("callStatus", status);
        editor.putString("phoneNumber", phonenumber);
        editor.commit();
        L_status = 1;
        return L_status;
    }

    public String getstoreCallStatus(Context param_context) {
        SharedPreferences prefs = param_context.getSharedPreferences("phonecallStatus", 0);
        return (prefs.getString("callStatus", null));
    }

    public String getstoreCallPhoneNumeber(Context param_context) {
        SharedPreferences prefs = param_context.getSharedPreferences("phonecallStatus", 0);
        return (prefs.getString("phoneNumber", null));
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void resetNotifications(Context param_context) {

        SharedPreferences.Editor editor = param_context.getSharedPreferences("initStatus", 0).edit();

        editor.putString("notification","0");
        editor.putString("pendingNotification","0");
        editor.commit();
    }
    public void initAllParamaters(Context param_context) {

        SharedPreferences.Editor editor = param_context.getSharedPreferences("initStatus", 0).edit();
        editor.putString("RI_Stored", "0");
        editor.putString("stateActivity", "0");
        editor.putString("pairing", "0");
        editor.putString("datac", "0");
        editor.putString("datam", "0");
        editor.putString("sendlog", "0");
        editor.putString("notification","0");
        editor.putString("dataSlave", "0");

        editor.putString("srmessage", "1");
        editor.putString("srcall","1");
        editor.putString("showNotification", "1");
        editor.putString("tncAccepted","0");
        ConnectivityStatus cs=new ConnectivityStatus();
        Boolean result=cs.getStatus(param_context);
        if(result)
        editor.putString("network","1");
        else
            editor.putString("network","0");
        editor.putString("pendingNotification","0");
        editor.commit();

    }

    public String getSpecificParameter(Context param_context, int mField) {
        String tempretrun = null;
        SharedPreferences prefs = param_context.getSharedPreferences("initStatus", 0);
        switch (mField) {
            case 0:
                tempretrun = prefs.getString("RI_Stored", null);
                break;
            case 1:
                tempretrun = prefs.getString("stateActivity", null);
                break;
            case 2:
                tempretrun = prefs.getString("pairing", null);
                break;
            case 3:
                tempretrun = prefs.getString("datac", null);
                break;
            case 4:
                tempretrun = prefs.getString("datam", null);
                break;
            case 5:
                tempretrun = prefs.getString("sendlog", null);
                break;
            case 6:
                tempretrun = prefs.getString("network", null);
                break;
            case 7:
                tempretrun = prefs.getString("notification", null);
                break;
            case 8:
                tempretrun = prefs.getString("pendingNotification", null);
                break;
            case 10:
                tempretrun = prefs.getString("dataSlave", null);
                break;

            case 11:
                tempretrun = prefs.getString("srmessage", null);
                break;
            case 12:
                tempretrun = prefs.getString("srcall", null);
                break;
            case 13:
                tempretrun = prefs.getString("showNotification", null);
                break;
            case 14:
                tempretrun = prefs.getString("tncAccepted", null);
                break;
            case 15:
                tempretrun = prefs.getString("pairId", null);
                break;



        }
        return tempretrun;
    }
    public void resetToLastStage(Context param_context,String state){
        switch(state){
            case "2":
                storetSpecificParameter(param_context, InitStatus.RI_Stored, "1");
                storetSpecificParameter(param_context, InitStatus.stateActivity, "2");
                break;
            case "3":
                storetSpecificParameter(param_context, InitStatus.pairing, "2");
                storetSpecificParameter(param_context, InitStatus.stateActivity, "3");
                break;
            case "6":
                storetSpecificParameter(param_context, InitStatus.sendlog, "1");
                storetSpecificParameter(param_context, InitStatus.pairing, "2");
               // storetSpecificParameter(param_context, InitStatus.dataSlave, "1");
                storetSpecificParameter(param_context, InitStatus.stateActivity, "6");
                break;
            case "7":
                storetSpecificParameter(param_context, InitStatus.pairing, "1");
                storetSpecificParameter(param_context, InitStatus.stateActivity, "7");
                break;
            case "8":
                storetSpecificParameter(param_context, InitStatus.pairing, "1");
                storetSpecificParameter(param_context, InitStatus.stateActivity, "8");
                break;
        }
        startDesiredActivity(param_context);
    }

    public void startDesiredActivity(Context param_context) {

        String state = getSpecificParameter(param_context, stateActivity);
        Intent intent = null;
        switch (state) {
            case "0":storetSpecificParameter(param_context, InitStatus.stateActivity, ""); break;  //exception case has to be handled her not a right way though has to be handleed in a standard way.

            case "2":
                intent = new Intent(param_context, OptionActivity.class);

                break;
            case "3":
                intent = new Intent(param_context, MainTabActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            case "6":
                intent = new Intent(param_context, SlaveActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            case "7":
                intent = new Intent(param_context, PreSlaveActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;
            case "8":
                intent = new Intent(param_context, MasterRequestActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                break;

        }
        if(intent!=null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


            param_context.startActivity(intent);
        }

    }

    public void storetSpecificParameter(Context param_context, int mField, String value) {

        SharedPreferences.Editor editor = param_context.getSharedPreferences("initStatus", 0).edit();

        switch (mField) {
            case 0:
                editor.putString("RI_Stored", value);
                break;
            case 1:
                editor.putString("stateActivity", value);
                break;
            case 2:
                editor.putString("pairing", value);
                break;
            case 3:
                editor.putString("datac", value);
                break;
            case 4:
                editor.putString("datam", value);
                break;
            case 5:
                editor.putString("sendlog", value);
                break;
            case 6:
                editor.putString("network", value);
                break;
            case 7:
                editor.putString("notification", value);
                break;
            case 8:
                editor.putString("pendingNotification", value);
                break;
            case 10:
                editor.putString("dataSlave",value);
                break;
            case 11:
                editor.putString("srmessage",value);
                break;
            case 12:
                editor.putString("srcall",value);
                break;
            case 13:
                editor.putString("showNotification",value);
                break;
            case 14:
                editor.putString("tncAccepted",value);
                break;
            case 15:
                editor.putString("pairId",value);
                break;

        }
        editor.commit();
    }


}
