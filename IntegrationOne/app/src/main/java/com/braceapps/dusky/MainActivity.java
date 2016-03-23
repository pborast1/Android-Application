package com.braceapps.dusky;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


import com.braceapps.dusky.Registration.RegistrationManager.RegistererManager;
import com.braceapps.dusky.Servicehandler.requestPJ.AppDevicePOJO;
import com.crypto.unisys.psh.pubkey.Cryo;
import com.braceapps.dusky.IMEI.InformationRetriever;
import com.braceapps.dusky.IMEI.ManagerIMEI;
import com.braceapps.dusky.R;
import com.braceapps.dusky.Registration.RegistrationDetails.InfoRetriever;
import com.braceapps.dusky.Servicehandler.AppConfig;
import com.braceapps.dusky.Servicehandler.InitStatus;
import com.braceapps.dusky.Servicehandler.ServiceHandler;
import com.braceapps.dusky.Servicehandler.requestPJ.AppRegPOJO;
import com.google.gson.Gson;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class MainActivity extends ActionBarActivity {
    public static Context contextOfApplication;

    String appState=null;
    String regId;
    private ProgressDialog progress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contextOfApplication = getApplicationContext();
        InitStatus init = new InitStatus();
        String tempVal = init.getSpecificParameter(contextOfApplication, InitStatus.stateActivity);

        if (tempVal != null && !tempVal.matches("")) {
            //progress.dismiss();
            init.startDesiredActivity(contextOfApplication);
        } else {
            progress = ProgressDialog.show(MainActivity.this, null,
                    "Please wait...");

            init.initAllParamaters(contextOfApplication);
            ManagerIMEI objIMEI = new ManagerIMEI();
            int L_status_im = objIMEI.storeIMEI(contextOfApplication);
            if (init.getSpecificParameter(contextOfApplication, InitStatus.network).matches("1")) {
                new AppStateRetrieval().execute();
            } else {
                init.storetSpecificParameter(contextOfApplication, InitStatus.stateActivity, "");
                progress.dismiss();
                Toast.makeText(contextOfApplication, "Unable to connect to server.Please try later", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class RegisterApp extends AsyncTask<Void, Void, Void> {
        String L_imei;
        String L_RegId;
        String jsonStr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            //   L_imei="someval";


        }

        @Override
        protected Void doInBackground(Void... arg0) {


            ManagerIMEI objIMEI = new ManagerIMEI();
            int L_status_im = objIMEI.storeIMEI(contextOfApplication); //no exception managed here
            RegistererManager objManager = new RegistererManager();
            int L_status = objManager.registerAndStoreRegId(contextOfApplication);  //no exception managed here

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            RegistererManager objManager = new RegistererManager();
            objManager.registerAndStoreRegId(contextOfApplication);
            InfoRetriever objInfo = new InfoRetriever();
            InformationRetriever objIMEI = new InformationRetriever();
            if (!TextUtils.isEmpty(objInfo.getStoredRegId(contextOfApplication)) && !TextUtils.isEmpty(objIMEI.getIMEIValue(contextOfApplication))) {
                //Toast.makeText(contextOfApplication, objInfo.getStoredRegId(contextOfApplication), Toast.LENGTH_LONG).show();

                new ServerRegistrationInActivity().execute();
            } else {
                Toast.makeText(contextOfApplication, "Unable to fectch info from server.Please try again later", Toast.LENGTH_LONG).show();
            }

        }
    }

    private class ServerRegistrationInActivity extends AsyncTask<Void, Void, Void> {
        String L_imei;
        String L_RegId;
        String jsonStr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            //   L_imei="someval";


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();


            InfoRetriever objRegInfo = new InfoRetriever();
            L_RegId = objRegInfo.getStoredRegId(contextOfApplication);


            ManagerIMEI objIMEI = new ManagerIMEI();
            L_imei = objIMEI.getStoredIMEI(contextOfApplication);


            if (!TextUtils.isEmpty(L_imei) && !TextUtils.isEmpty(L_RegId)) {
                AppRegPOJO reg = new AppRegPOJO(L_imei, L_RegId);
                Gson gson = new Gson();
                String json = gson.toJson(reg);
                String encry = null;

                try {
                    encry = new Cryo().Encrypt(json);
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
                jsonStr = sh.makeServiceCall(AppConfig.url + "/AppRegRequest", ServiceHandler.POST, parameterNew);

                Log.d("Response: ", "> " + jsonStr);


            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            if (jsonStr != null && !jsonStr.contains("error")) {


                InfoRetriever objInfo = new InfoRetriever();
                InformationRetriever objIMEI = new InformationRetriever();
                //Toast.makeText(contextOfApplication, jsonStr, Toast.LENGTH_LONG).show();
                if (jsonStr.matches("1")) {
                    InitStatus objinit = new InitStatus();
                    objinit.storetSpecificParameter(contextOfApplication, InitStatus.RI_Stored, "1");
                    objinit.storetSpecificParameter(contextOfApplication, InitStatus.stateActivity, "2");
                    progress.dismiss();
                    objinit.startDesiredActivity(contextOfApplication);
                    finish();
                } else {
                    progress.dismiss();
                    Toast.makeText(contextOfApplication, "Unable to connect to server.Please try later", Toast.LENGTH_LONG).show();
                }

            } else {
                progress.dismiss();
                Log.e("Servicehandler", "Couldn't get any data from the url");
            }

        }
    }

    private class AppStateRetrieval extends AsyncTask<Void, Void, Void> {
        String L_imei;
        String L_RegId;
        String jsonStr;
        ManagerIMEI objIMEI;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objIMEI = new ManagerIMEI();
            objIMEI.storeIMEI(contextOfApplication);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();


            L_imei = objIMEI.getStoredIMEI(contextOfApplication);

            if (!TextUtils.isEmpty(L_imei)) {
                AppDevicePOJO appDevice = new AppDevicePOJO(L_imei);
                Gson gson = new Gson();
                String json = gson.toJson(appDevice);
                String encry = null;

                try {
                    encry = new Cryo().Encrypt(json);
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
                appState = null;
                // Making a request to url and getting response
                jsonStr = sh.makeServiceCall(AppConfig.url + "/AppStateRequest", ServiceHandler.POST, parameterNew);

                Log.d("Response: ", "> " + jsonStr);

                if (jsonStr != null && jsonStr.length() <= 2 && !jsonStr.contains("error")) {

                    appState = jsonStr;

                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            InitStatus objinit = new InitStatus();
            if (appState != null) {
                if (appState.matches("-1")) {
                    new RegisterApp().execute();
                } else {
                    progress.dismiss();
                    objinit.resetToLastStage(contextOfApplication, appState);
//                        Toast.makeText(contextOfApplication, "Unable to fectch info from server\n please try later", Toast.LENGTH_LONG).show();
                }
            } else {
                //need to call specific activity and set all the params
                objinit.storetSpecificParameter(contextOfApplication, InitStatus.stateActivity, "");
                progress.dismiss();

            }
        }

    }
}

