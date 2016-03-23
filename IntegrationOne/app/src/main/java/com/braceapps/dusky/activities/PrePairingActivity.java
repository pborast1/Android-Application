package com.braceapps.dusky.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.crypto.unisys.psh.pubkey.Cryo;
import com.braceapps.dusky.IMEI.InformationRetriever;
import com.braceapps.dusky.R;
import com.braceapps.dusky.Registration.RegistrationDetails.InfoRetriever;
import com.braceapps.dusky.Servicehandler.AppConfig;
import com.braceapps.dusky.Servicehandler.InitStatus;
import com.braceapps.dusky.Servicehandler.ServiceHandler;
import com.braceapps.dusky.Servicehandler.requestPJ.PairPOJO;
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


public class PrePairingActivity extends ActionBarActivity {

    public static Context contextOfApplication;

    private String master_imei = null;
    private String slave_imei = null;
    public EditText textbox;
    public ImageButton acceptBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_pairing);
        contextOfApplication = getApplicationContext();
        textbox = (EditText) findViewById(R.id.masterIMEITV);
        View.OnClickListener pairbtnListner = new View.OnClickListener() {
            public void onClick(View v) {
                master_imei = textbox.getText().toString();
                new ServerPairInActivity().execute();

            }
        };

        acceptBtn = (ImageButton) findViewById(R.id.acceptBtn);

        acceptBtn.setOnClickListener(pairbtnListner);

        ImageButton cancelButton = (ImageButton) findViewById(R.id.cancelButton);

        View.OnClickListener backbtnListner = new View.OnClickListener() {
            public void onClick(View v) {
        Intent intent=new Intent(contextOfApplication,OptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        };
        cancelButton.setOnClickListener(backbtnListner);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_pre_pairing, menu);
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

        return super.onOptionsItemSelected(item);
    }


    private class ServerPairInActivity extends AsyncTask<Void, Void, Void> {

        String jsonStr;
        private ProgressDialog progress = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(PrePairingActivity.this, null,
                    "Please wait...");

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            InfoRetriever objRegInfo = new InfoRetriever();
            InformationRetriever objInfo = new InformationRetriever();
            slave_imei = objInfo.getIMEIValue(contextOfApplication);

            if (!TextUtils.isEmpty(slave_imei) && !TextUtils.isEmpty(master_imei)) {

               /* List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                parameters.add(new BasicNameValuePair("master", master_imei));
                parameters.add(new BasicNameValuePair("slave", slave_imei));
*/
                PairPOJO pairpj=new PairPOJO(master_imei,slave_imei);
                Gson gson = new Gson();
                String json = gson.toJson(pairpj);
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


                // Making a request to url and getting response
                jsonStr = sh.makeServiceCall(AppConfig.url + "/AppPairRequest", ServiceHandler.POST, parameterNew);

               // Log.d("Response: ", "> " + jsonStr);


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (jsonStr != null) {

                if (jsonStr.matches("1")) {
                    InitStatus objinit = new InitStatus();

                    objinit.storetSpecificParameter(contextOfApplication, InitStatus.pairing, "1");
                    objinit.storetSpecificParameter(contextOfApplication, InitStatus.stateActivity, "7");
               /* objinit.storetSpecificParameter(contextOfApplication, InitStatus.pairing, "1");
                objinit.storetSpecificParameter(contextOfApplication, InitStatus.stateActivity, "6");
                objinit.storetSpecificParameter(contextOfApplication, InitStatus.sendlog, "1");*/

                    OptionActivity.finishActivity.finish();
                    objinit.startDesiredActivity(contextOfApplication);
                    Toast.makeText(contextOfApplication, "Pairing request sent", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(contextOfApplication, "Failed to pair your device", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(contextOfApplication, "Failed to pair your device", Toast.LENGTH_LONG).show();
            }
        }

    }

}

