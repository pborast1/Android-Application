package com.braceapps.dusky.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braceapps.dusky.Servicehandler.requestPJ.AppDevicePOJO;
import com.crypto.unisys.psh.pubkey.Cryo;
import com.braceapps.dusky.IMEI.InformationRetriever;
import com.braceapps.dusky.R;
import com.braceapps.dusky.Registration.RegistrationDetails.InfoRetriever;
import com.braceapps.dusky.Servicehandler.AppConfig;
import com.braceapps.dusky.Servicehandler.InitStatus;
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


public class PreSlaveActivity extends ActionBarActivity {
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextOfApplication = getApplicationContext();
        setContentView(R.layout.activity_pre_slave);
        Button cancelRequest =(Button)findViewById(R.id.cancelRequestPreSlaveButton);
        cancelRequest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new SlaveDecline().execute();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_pre_slave, menu);
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
            return true;
        }
        if(id==R.id.action_declineRequest){
            new SlaveDecline().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class SlaveDecline extends AsyncTask<Void, Void, Void> {

        String jsonStr;
        private ProgressDialog progress = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progress = ProgressDialog.show(PreSlaveActivity.this, null,
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
               // Log.d("Response: ", "> " + jsonStr);

                if (jsonStr != null) {


                } else {
                    //Log.e("Servicehandler", "Couldn't get any data from the url");
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
                Toast.makeText(contextOfApplication, "Failed to deny the pairing request", Toast.LENGTH_LONG).show();
            }
        }

    }

}
