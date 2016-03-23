package com.braceapps.dusky.activities;

import android.app.NotificationManager;
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
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.braceapps.dusky.IMEI.ManagerIMEI;
import com.braceapps.dusky.Registration.RegistrationDetails.InfoRetriever;
import com.braceapps.dusky.Registration.RegistrationManager.RegistererManager;
import com.braceapps.dusky.Servicehandler.InitStatus;
import com.braceapps.dusky.Servicehandler.ServiceHandler;
import com.braceapps.dusky.Servicehandler.requestPJ.AppDevicePOJO;
import com.braceapps.dusky.adaptors.SlaveLogAdaptor;
import com.braceapps.dusky.db.dbconfig.DbInit;
import com.braceapps.dusky.db.dbhandler.DBManagerLogger;
import com.braceapps.dusky.db.dbhandler.RequestLoggerPOJO;
import com.crypto.unisys.psh.pubkey.Cryo;
import com.braceapps.dusky.IMEI.InformationRetriever;
import com.braceapps.dusky.R;
import com.braceapps.dusky.Servicehandler.AppConfig;
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


public class SlaveActivity extends ActionBarActivity {

    public static Context contextOfApplication;
    ArrayList<RequestLoggerPOJO> list;
    int viewatList=0;
    int viewOffset=0;
    ListView slaveListView;
    int oldPosition;
    SlaveLogAdaptor mAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slave);
        contextOfApplication = getApplicationContext();
        InitStatus initstat=new InitStatus();
        removeAllNotifications();

      /*  String value=initstat.getSpecificParameter(contextOfApplication,InitStatus.pairing);
        TextView paringStat=(TextView)findViewById(R.id.paringstatus);

        if(value.matches("1")){
        paringStat.setText("Paring request sent.Waiting for response");
        }else{
            paringStat.setText("Paring request accepted.Sending log...");
        }*/
        removeAllNotifications();
        updateSlaveLogList();


    }
    private void removeAllNotifications(){
        NotificationManager nMgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();

    }
    private void updateSlaveLogList() {
        DbInit dbInit = new DbInit(contextOfApplication);
        DBManagerLogger dbm = new DBManagerLogger(contextOfApplication);

        slaveListView = (ListView) findViewById(R.id.slaveLV);
        View empty =findViewById(R.id.empty);
        empty.setVisibility(View.INVISIBLE);

        InitStatus objinit = new InitStatus();
        //  objinit.storePairingStatus(contextOfApplication,"0");
        if (objinit.getSpecificParameter(contextOfApplication, InitStatus.dataSlave).matches("1")) {

           /* list = dbm.getCompleteSlaveLog();
            Collections.reverse(list);*/
            list=dbm.getCompleteSlaveLog(15);
            mAdaptor = new SlaveLogAdaptor(this, list);
            slaveListView.setAdapter(mAdaptor);
            mAdaptor.notifyDataSetChanged();
            slaveListView.setOnScrollListener(onScrollListener());


            //onclick expand not working here....

            /*oldPosition=-1;
            slaveListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (oldPosition != -1) {
                        if (oldPosition != position) {
                            RequestLoggerPOJO log = list.get(oldPosition);
                            log.setLvUnorHide(false);
                            oldPosition = position;
                        }

                    } else {
                        oldPosition = position;
                    }

                    RequestLoggerPOJO log = list.get(oldPosition);
                    if (!log.isLvUnorHide())
                        log.setLvUnorHide(true);
                    else
                        log.setLvUnorHide(false);
                    mAdaptor.notifyDataSetChanged();
                }
            });

*/

        } else {

            slaveListView.setEmptyView(empty);
        }
    }
    private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count =  slaveListView.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (slaveListView.getLastVisiblePosition() >= count - threshold) {
                        int firstVisibleRow = slaveListView.getFirstVisiblePosition();
                        int lastVisibleRow = slaveListView.getLastVisiblePosition();
                        viewOffset=lastVisibleRow-firstVisibleRow;
                       // Toast.makeText(contextOfApplication,String.valueOf(viewOffset),Toast.LENGTH_SHORT).show();
                        viewatList = count;
                       /* ProgressDialog progress = null;
                        progress = ProgressDialog.show(SlaveActivity.this, null,
                                "Loading Data...");*/
                        updateSlaveLogList(30);
                       //progress.dismiss();

                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }
        };
    }
    private void updateSlaveLogList(int to) {
        DbInit dbInit = new DbInit(contextOfApplication);
        DBManagerLogger dbm = new DBManagerLogger(contextOfApplication);

         slaveListView = (ListView) findViewById(R.id.slaveLV);
        View empty =findViewById(R.id.empty);
        empty.setVisibility(View.INVISIBLE);

        InitStatus objinit = new InitStatus();
        //  objinit.storePairingStatus(contextOfApplication,"0");
        if (objinit.getSpecificParameter(contextOfApplication, InitStatus.dataSlave).matches("1")) {

            /*list = dbm.getCompleteSlaveLog();
            Collections.reverse(list);*/
            list=dbm.getCompleteSlaveLog(slaveListView.getCount()+to);
            mAdaptor = new SlaveLogAdaptor(this, list);

            slaveListView.setAdapter(mAdaptor);
            mAdaptor.notifyDataSetChanged();
            slaveListView.setOnScrollListener(onScrollListener());
            slaveListView.setSelection(viewatList-viewOffset);
            //onclick expand not working here....

            /*oldPosition=-1;
            slaveListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (oldPosition != -1) {
                        if (oldPosition != position) {
                            RequestLoggerPOJO log = list.get(oldPosition);
                            log.setLvUnorHide(false);
                            oldPosition = position;
                        }

                    } else {
                        oldPosition = position;
                    }

                    RequestLoggerPOJO log = list.get(oldPosition);
                    if (!log.isLvUnorHide())
                        log.setLvUnorHide(true);
                    else
                        log.setLvUnorHide(false);
                    mAdaptor.notifyDataSetChanged();
                }
            });

*/

        } else {

            slaveListView.setEmptyView(empty);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_tab, menu);
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

        if (id == R.id.unpair) {
            new UnparingBackground().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSlaveLogList();
        removeAllNotifications();
    }

    private class UnparingBackground extends AsyncTask<Void, Void, Void> {
        String L_imei;
        String jsonStr;
        private ProgressDialog progress = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            progress = ProgressDialog.show(SlaveActivity.this, null,
                    "Please wait...");

            //   L_imei="someval";


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();


            ManagerIMEI objIMEI = new ManagerIMEI();
            L_imei = objIMEI.getStoredIMEI(contextOfApplication);


            if (!TextUtils.isEmpty(L_imei)) {
                AppDevicePOJO appDevice=new AppDevicePOJO(L_imei);
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


                // Making a request to url and getting response
                jsonStr = sh.makeServiceCall(AppConfig.url + "/AppUnpairRequest", ServiceHandler.POST, parameterNew);

              //  Log.d("Response: ", "> " + jsonStr);

                if (jsonStr.matches("2")) {


                } else {
                 //   Log.e("Servicehandler", "Couldn't get any data from the url");
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            progress.dismiss();
            RegistererManager objManager = new RegistererManager();
            objManager.registerAndStoreRegId(contextOfApplication);
            InfoRetriever objInfo = new InfoRetriever();
            InformationRetriever objIMEI = new InformationRetriever();

          //  Toast.makeText(contextOfApplication, jsonStr, Toast.LENGTH_LONG).show();
            if (jsonStr.matches("1") || jsonStr.matches(("2"))) {
                InitStatus objinit = new InitStatus();
                objinit.storetSpecificParameter(contextOfApplication, InitStatus.pairing, "0");
                objinit.storetSpecificParameter(contextOfApplication, InitStatus.stateActivity, "2");
                objinit.startDesiredActivity(contextOfApplication);
                Toast.makeText(contextOfApplication, "Unpairing Successful!!!!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(contextOfApplication, "Unable to unpair.Please try again later", Toast.LENGTH_LONG).show();
            }

        }
    }
}