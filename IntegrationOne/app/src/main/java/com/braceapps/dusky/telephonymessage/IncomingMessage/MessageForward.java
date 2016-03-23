package com.braceapps.dusky.telephonymessage.IncomingMessage;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.braceapps.dusky.Servicehandler.AppConfig;
import com.braceapps.dusky.Servicehandler.ServiceHandler;
import com.braceapps.dusky.telephonymessage.services.RequestPOJO;
import com.crypto.unisys.psh.pubkey.Cryo;
import com.braceapps.dusky.IMEI.InformationRetriever;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * Created by Paresh on 1/22/2015.
 */
public class MessageForward {



    public String sendMessageToMaster(String title, String message, String contactNumber, String contactName, Context contextOfApplication) {
        int L_status = 0;
        InformationRetriever objInfo = new InformationRetriever();
        String slave_imei = objInfo.getIMEIValue(contextOfApplication);

        ServiceHandler sh = new ServiceHandler();
        String jsonStr = null;
        if (!TextUtils.isEmpty(slave_imei)) {


            RequestPOJO pj=new RequestPOJO(slave_imei,title,message,contactNumber,contactName);
            Gson gson = new Gson();
            String json = gson.toJson(pj);
            String encry=null;

            try {
                encry=new Cryo().Encrypt(json);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }

            List<NameValuePair> parameterNew = new ArrayList<NameValuePair>();

            //remove capital R from Request
            parameterNew.add(new BasicNameValuePair("Request", encry));
            jsonStr = sh.makeServiceCall(AppConfig.url + "/NotifyMaster", ServiceHandler.POST, parameterNew);

            Log.d("Server mResponse: ", "> " + jsonStr);


        }

        return jsonStr;

    }
}
