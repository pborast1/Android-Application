package com.braceapps.dusky.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.braceapps.dusky.Registration.RegistrationDetails.InfoRetriever;
import com.braceapps.dusky.Servicehandler.InitStatus;
import com.braceapps.dusky.Servicehandler.requestPJ.AppDevicePOJO;
import com.crypto.unisys.psh.pubkey.Cryo;
import com.braceapps.dusky.IMEI.InformationRetriever;
import com.braceapps.dusky.R;
import com.braceapps.dusky.Servicehandler.AppConfig;
import com.braceapps.dusky.Servicehandler.ServiceHandler;
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


public class MasterRequestActivity extends ActionBarActivity {
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_request);
        contextOfApplication = getApplicationContext();
        TextView slaveId=(TextView)findViewById(R.id.imeiTV);
        InitStatus init=new InitStatus();
        String slave=init.getSpecificParameter(this,InitStatus.pairId);
        if(slave!=null || slave!="") {
            slaveId.setText(slave);
        }else{
            slaveId.setText("Device Id misssing");
        }

        ImageButton accept=(ImageButton)findViewById(R.id.cancelRequestPreSlaveButton);
        ImageButton decline=(ImageButton)findViewById(R.id.declineMaster);

        decline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new MasterDecline().execute();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new MasterAccept().execute();
            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_master_request, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MasterAccept extends AsyncTask<Void, Void, Void> {

        String jsonStr;
        private ProgressDialog progress = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progress = ProgressDialog.show(MasterRequestActivity.this, null,
                    "Please wait...");

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            InfoRetriever objRegInfo = new InfoRetriever();
            InformationRetriever objInfo = new InformationRetriever();
            String imei = objInfo.getIMEIValue(contextOfApplication);

            if (!TextUtils.isEmpty(imei) ) {

                AppDevicePOJO appDevice=new AppDevicePOJO(imei);
                Gson gson = new Gson();
                String json = gson.toJson(appDevice);
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

                jsonStr = sh.makeServiceCall(AppConfig.url + "/AppConfirmPairRequest", ServiceHandler.POST, parameterNew);
              //  Log.d("Response: ", "> " + jsonStr);

                if (jsonStr != null) {


                } else {
                    Log.e("Servicehandler", "Couldn't get any data from the url");
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (jsonStr.matches("2")) {
                InitStatus objinit = new InitStatus();


                //objinit.storetSpecificParameter(contextOfApplication, InitStatus.pairing, "1");
                objinit.storetSpecificParameter(contextOfApplication, InitStatus.stateActivity, "3");
                //objinit.storetSpecificParameter(contextOfApplication, InitStatus.sendlog, "1");

                Toast.makeText(contextOfApplication, "Request Accepted", Toast.LENGTH_LONG).show();
                objinit.startDesiredActivity(contextOfApplication);

                finish();
            } else {
                Toast.makeText(contextOfApplication, "Failed to cancel the request.", Toast.LENGTH_LONG).show();
            }
        }

    }

    private class MasterDecline extends AsyncTask<Void, Void, Void> {

        String jsonStr;
        private ProgressDialog progress = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progress = ProgressDialog.show(MasterRequestActivity.this, null,
                    "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            InfoRetriever objRegInfo = new InfoRetriever();
            InformationRetriever objInfo = new InformationRetriever();
            String imei = objInfo.getIMEIValue(contextOfApplication);

            if (!TextUtils.isEmpty(imei) ) {

                AppDevicePOJO appDevice=new AppDevicePOJO(imei);
                Gson gson = new Gson();
                String json = gson.toJson(appDevice);
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

                jsonStr = sh.makeServiceCall(AppConfig.url + "/AppDenyPairRequest", ServiceHandler.POST, parameterNew);
              //  Log.d("Response: ", "> " + jsonStr);

                if (jsonStr != null) {


                } else {
                   // Log.e("Servicehandler", "Couldn't get any data from the url");
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (jsonStr.matches("2")) {
                InitStatus objinit = new InitStatus();


                objinit.storetSpecificParameter(contextOfApplication, InitStatus.pairing, "0");
                objinit.storetSpecificParameter(contextOfApplication, InitStatus.stateActivity, "2");
                objinit.storetSpecificParameter(contextOfApplication, InitStatus.sendlog, "0");


                objinit.startDesiredActivity(contextOfApplication);
                Toast.makeText(contextOfApplication, "Request Denied", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(contextOfApplication, "Failed to pair your device", Toast.LENGTH_LONG).show();
            }
        }

    }

}



