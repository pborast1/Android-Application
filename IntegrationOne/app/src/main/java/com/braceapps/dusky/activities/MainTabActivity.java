package com.braceapps.dusky.activities;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.braceapps.dusky.Registration.RegistrationManager.RegistererManager;
import com.braceapps.dusky.Servicehandler.requestPJ.AppDevicePOJO;
import com.braceapps.dusky.db.dbconfig.DbInit;
import com.braceapps.dusky.db.dbhandler.MessagePOJO;
import com.crypto.unisys.psh.pubkey.Cryo;
import com.braceapps.dusky.IMEI.InformationRetriever;
import com.braceapps.dusky.IMEI.ManagerIMEI;
import com.braceapps.dusky.R;
import com.braceapps.dusky.Registration.RegistrationDetails.InfoRetriever;
import com.braceapps.dusky.Servicehandler.AppConfig;
import com.braceapps.dusky.Servicehandler.InitStatus;
import com.braceapps.dusky.Servicehandler.ServiceHandler;
import com.braceapps.dusky.adaptors.CallLogListAdaptor;
import com.braceapps.dusky.adaptors.MessageListAdaptor;
import com.braceapps.dusky.db.dbhandler.CallLogPOJO;
import com.braceapps.dusky.db.dbhandler.DBManagerCall_logs;
import com.braceapps.dusky.db.dbhandler.DBManagerMessage;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class MainTabActivity extends ActionBarActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public SectionsPagerAdapter mSectionsPagerAdapter;
    public static Context contextOfApplication;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        contextOfApplication = getApplicationContext();

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //  actionBar.setBackgroundDrawable(new ColorDrawable(Color.RED));


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.

        // Create a tab with text corresponding to the page title defined by
        // the adapter. Also specify this Activity object, which implements
        // the TabListener interface, as the callback (listener) for when
        // this tab is selected.
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Messages")
                        .setTabListener(this));
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Call Log")
                        .setTabListener(this));

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
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a CallLogFragment (defined as a static inner class below).
            if (position == 0) {
                return MessageFragment.newInstance(position + 1);
            } else {
                return CallLogFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Messages";
                case 1:
                    return "Call Logs";

            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CallLogFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        static CallLogListAdaptor mAdaptor;
        static ArrayList<CallLogPOJO> list;
        public int oldPosition=-1;
        ListView callListView;
        TextView tv;
        BroadcastReceiver mReceiver;
        View rootView;
        int viewatList=0;
        int viewOffset=0;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static CallLogFragment newInstance(int sectionNumber) {
            CallLogFragment fragment = new CallLogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        public CallLogFragment() {
        }


        private AbsListView.OnScrollListener onScrollListener() {
            return new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    int threshold = 1;
                    int count = callListView.getCount();

                    if (scrollState == SCROLL_STATE_IDLE) {
                         if (callListView.getLastVisiblePosition() >= count - threshold ) {
                          //Toast.makeText(contextOfApplication,"Fucking limit  "+callListView.getCount(),Toast.LENGTH_SHORT).show();
                             int firstVisibleRow = callListView.getFirstVisiblePosition();
                             int lastVisibleRow = callListView.getLastVisiblePosition();
                             viewOffset=lastVisibleRow-firstVisibleRow;
                            viewatList=count;
                            updateCompleteCallLoglistview(30);

                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                     int totalItemCount) {
                }

            };
        }


        @Override
        public void onResume() {
            super.onResume();
            getActivity() .registerReceiver(mMessageReceiver, new IntentFilter("CallLogUpdate"));
            updateCompleteCallLoglistview();
            removeAllNotifications();
        }
        @Override
        public void onPause() {
            super.onPause();
            getActivity().unregisterReceiver(mMessageReceiver);


        }
        private void removeAllNotifications(){
            NotificationManager nMgr = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancelAll();

        }
        private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // Extract data included in the Intent
                String message = intent.getStringExtra("calls");
                updateViewList(message);
                removeAllNotifications();
            }
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.activity_post_pair, container, false);

            updateCompleteCallLoglistview();

/*
            callListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    tv=(TextView)view.findViewById(R.id.messageTV);

                    TextView tvReverts=(TextView)view.findViewById(R.id.Callback);
                    tvReverts
                }

                });*/

            return rootView;
        }


        public void updateCompleteCallLoglistview(){
            DbInit dbInit = new DbInit(getActivity().getApplicationContext());
            DBManagerCall_logs dbm = new DBManagerCall_logs(getActivity().getApplicationContext());

            callListView= (ListView) rootView.findViewById(R.id.messageLV);
            View empty = rootView.findViewById(R.id.empty);
            empty.setVisibility(View.INVISIBLE);
            InitStatus objinit = new InitStatus();
            callListView.setOnScrollListener(onScrollListener());  //also a new line
            if (objinit.getSpecificParameter(contextOfApplication, InitStatus.datac).matches("1")) {
                /*list = dbm.getFullCallLog();  //this is changed to make an protable change to offset read on scroll
                Collections.reverse(list);*/
                list=dbm.getCallLogFromAndTo(15);
              //  Toast.makeText(contextOfApplication,list.size(),Toast.LENGTH_LONG).show();
               //
                mAdaptor = new CallLogListAdaptor(getActivity(), list);

                mAdaptor.notifyDataSetChanged();
                callListView.setAdapter(mAdaptor);
              //  callListView.setSelection(viewatList-viewOffset);

                callListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                       /* Object o = parent.getItemAtPosition(position);
                        LinearLayout linearLay=(LinearLayout)view.findViewById(R.id.linearLayoutHideUnHide);
                        linearLay.setVisibility(View.VISIBLE);
                        CallLogPOJO log=(CallLogPOJO)o;//As you are using Default String Adapter
                        Toast.makeText(getActivity(),log.getContactName(),Toast.LENGTH_SHORT).show();*/
                        if(oldPosition!=-1){
                            if(oldPosition!=position) {
                                CallLogPOJO log = (CallLogPOJO) parent.getItemAtPosition(oldPosition);
                                log.setLvUnorHide(false);
                                oldPosition=position;
                            }

                        }else{
                            oldPosition=position;
                        }

                        CallLogPOJO log=(CallLogPOJO) parent.getItemAtPosition(position);
                        if(!log.isLvUnorHide())
                            log.setLvUnorHide(true);
                        else
                            log.setLvUnorHide(false);
                        mAdaptor.notifyDataSetChanged();
                    }
                });
            } else {

                callListView.setEmptyView(empty);
            }
        }
        public void updateCompleteCallLoglistview(int size){
            DbInit dbInit = new DbInit(getActivity().getApplicationContext());
            DBManagerCall_logs dbm = new DBManagerCall_logs(getActivity().getApplicationContext());

            callListView= (ListView) rootView.findViewById(R.id.messageLV);
            View empty = rootView.findViewById(R.id.empty);
            empty.setVisibility(View.INVISIBLE);
            InitStatus objinit = new InitStatus();


            if (objinit.getSpecificParameter(contextOfApplication, InitStatus.datac).matches("1")) {
                /*list = dbm.getFullCallLog();  //this is changed to make an protable change to offset read on scroll
                Collections.reverse(list);*/
                list=dbm.getCallLogFromAndTo(callListView.getCount()+size);
                //  Toast.makeText(contextOfApplication,list.size(),Toast.LENGTH_LONG).show();
                //
                mAdaptor = new CallLogListAdaptor(getActivity(), list);

                mAdaptor.notifyDataSetChanged();
                callListView.setAdapter(mAdaptor);
               // callListView.setOnScrollListener(onScrollListener());  //also a new line
                callListView.setSelection(viewatList-viewOffset);
                callListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                       /* Object o = parent.getItemAtPosition(position);
                        LinearLayout linearLay=(LinearLayout)view.findViewById(R.id.linearLayoutHideUnHide);
                        linearLay.setVisibility(View.VISIBLE);
                        CallLogPOJO log=(CallLogPOJO)o;//As you are using Default String Adapter
                        Toast.makeText(getActivity(),log.getContactName(),Toast.LENGTH_SHORT).show();*/
                        if (oldPosition != -1) {
                            if (oldPosition != position) {
                                CallLogPOJO log = (CallLogPOJO) parent.getItemAtPosition(oldPosition);
                                log.setLvUnorHide(false);
                                oldPosition = position;
                            }

                        } else {
                            oldPosition = position;
                        }

                        CallLogPOJO log = (CallLogPOJO) parent.getItemAtPosition(position);
                        if (!log.isLvUnorHide())
                            log.setLvUnorHide(true);
                        else
                            log.setLvUnorHide(false);
                        mAdaptor.notifyDataSetChanged();
                    }
                });
            } else {

                callListView.setEmptyView(empty);
            }
        }
        public void updateViewList(String params){
            String[] arr=params.split(",");
            ArrayList<CallLogPOJO> arrayList=new ArrayList<CallLogPOJO>();
           /* arrayList.add(new CallLogPOJO(arr[0],arr[1],arr[2],arr[3]));
            arrayList.addAll(list);
            list=arrayList;*/
            if(list!=null) {
                list.add(0, new CallLogPOJO(arr[0], arr[1], arr[2], arr[3]));
            mAdaptor.notifyDataSetChanged();
            }else{
                updateCompleteCallLoglistview();
            }
            }



    }


    public static class MessageFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        int viewOffset=0;
        int viewatList=0;
        static MessageListAdaptor mAdaptor;
        static ArrayList<MessagePOJO> list;
        int oldPosition;
        View rootView;
        ListView messageListLV;

        public static MessageFragment newInstance(int sectionNumber) {
            MessageFragment fragment = new MessageFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public MessageFragment() {
        }
        @Override
        public void onResume() {
            super.onResume();
            getActivity().registerReceiver(mMessageReceiver, new IntentFilter("MessageLogUpdate"));
            updateMessageLogListView();
            removeAllNotifications();
        }
        @Override
        public void onPause() {
            super.onPause();
            getActivity().unregisterReceiver(mMessageReceiver);


        }
        private void removeAllNotifications(){
            NotificationManager nMgr = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancelAll();

        }
        private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // Extract data included in the Intent
                String message = intent.getStringExtra("messages");
                updateViewList(message);
                removeAllNotifications();
            }
        };
        public void updateViewList(String params){
            String[] arr=params.split(",");
            ArrayList<CallLogPOJO> arrayList=new ArrayList<CallLogPOJO>();

            if(list!=null) {
                list.add(0, new MessagePOJO(arr[0], arr[1], arr[2], arr[3]));
                mAdaptor.notifyDataSetChanged();
            }else{
                updateMessageLogListView();
            }
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

             rootView = inflater.inflate(R.layout.activity_post_pair, container, false);

                updateMessageLogListView();

            return rootView;
        }
        private AbsListView.OnScrollListener onScrollListener() {
            return new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    int threshold = 1;
                    int count = messageListLV.getCount();

                    if (scrollState == SCROLL_STATE_IDLE) {
                        if (messageListLV.getLastVisiblePosition() >= count - threshold) {
                            //Toast.makeText(contextOfApplication,"Fucking limit  "+callListView.getCount(),Toast.LENGTH_SHORT).show();
                            int firstVisibleRow = messageListLV.getFirstVisiblePosition();
                            int lastVisibleRow = messageListLV.getLastVisiblePosition();
                            viewOffset=lastVisibleRow-firstVisibleRow;
                            viewatList = count;
                            updateMessageLogListView(30);

                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                     int totalItemCount) {
                }
            };
            }
        private void updateMessageLogListView() {
            DbInit dbInit = new DbInit(getActivity().getApplicationContext());
            DBManagerMessage dbm = new DBManagerMessage(getActivity().getApplicationContext());

             messageListLV = (ListView) rootView.findViewById(R.id.messageLV);
            View empty = rootView.findViewById(R.id.empty);
            empty.setVisibility(View.INVISIBLE);

            InitStatus objinit = new InitStatus();
            //  objinit.storePairingStatus(contextOfApplication,"0");
            if (objinit.getSpecificParameter(contextOfApplication, InitStatus.datam).matches("1")) {

                // list = dbm.getAllMessages();  Commenting this part as change of dynamic loading
                list=dbm.getAllMessagesFromAndTo(15);
                //Collections.reverse(list);
                mAdaptor = new MessageListAdaptor(getActivity(), list);
                mAdaptor.notifyDataSetChanged();
                messageListLV.setAdapter(mAdaptor);
                messageListLV.setOnScrollListener(onScrollListener());
                oldPosition=-1;
                messageListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if (oldPosition != -1) {
                            if (oldPosition != position) {
                                MessagePOJO log = (MessagePOJO) parent.getItemAtPosition(oldPosition);
                                log.setLvUnorHide(false);
                                oldPosition = position;
                            }

                        } else {
                            oldPosition = position;
                        }

                        MessagePOJO log = (MessagePOJO) parent.getItemAtPosition(position);
                        if (!log.isLvUnorHide())
                            log.setLvUnorHide(true);
                        else
                            log.setLvUnorHide(false);
                        mAdaptor.notifyDataSetChanged();
                    }
                });



            } else {

                messageListLV.setEmptyView(empty);
            }
        }

        private void updateMessageLogListView(int to) {
            DbInit dbInit = new DbInit(getActivity().getApplicationContext());
            DBManagerMessage dbm = new DBManagerMessage(getActivity().getApplicationContext());

            messageListLV = (ListView) rootView.findViewById(R.id.messageLV);
            View empty = rootView.findViewById(R.id.empty);
            empty.setVisibility(View.INVISIBLE);

            InitStatus objinit = new InitStatus();
            //  objinit.storePairingStatus(contextOfApplication,"0");
            if (objinit.getSpecificParameter(contextOfApplication, InitStatus.datam).matches("1")) {

                // list = dbm.getAllMessages();  Commenting this part as change of dynamic loading
                list=dbm.getAllMessagesFromAndTo(messageListLV.getCount()+to);
                //Collections.reverse(list);
                mAdaptor = new MessageListAdaptor(getActivity(), list);
                mAdaptor.notifyDataSetChanged();
                messageListLV.setAdapter(mAdaptor);
              //  messageListLV.setOnScrollListener(onScrollListener());
                messageListLV.setSelection(viewatList-viewOffset);
                oldPosition=-1;
                messageListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if (oldPosition != -1) {
                            if (oldPosition != position) {
                                MessagePOJO log = (MessagePOJO) parent.getItemAtPosition(oldPosition);
                                log.setLvUnorHide(false);
                                oldPosition = position;
                            }

                        } else {
                            oldPosition = position;
                        }

                        MessagePOJO log = (MessagePOJO) parent.getItemAtPosition(position);
                        if (!log.isLvUnorHide())
                            log.setLvUnorHide(true);
                        else
                            log.setLvUnorHide(false);
                        mAdaptor.notifyDataSetChanged();
                    }
                });



            } else {

                messageListLV.setEmptyView(empty);
            }
        }

    }




    private class UnparingBackground extends AsyncTask<Void, Void, Void> {
        String L_imei;
        String jsonStr;
        private ProgressDialog progress = null;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progress = ProgressDialog.show(MainTabActivity.this, null,
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

            //    Log.d("Response: ", "> " + jsonStr);

                if (jsonStr.matches("2")) {



                } else {
                    Log.e("Servicehandler", "Couldn't get any data from the url");
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
                Toast.makeText(contextOfApplication, "Unpairing Successfull!!!!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(contextOfApplication, "Unable to connect to server. Please try later", Toast.LENGTH_LONG).show();
            }

            /**
             * Updating parsed JSON data into ListView
             * */

        }

    }


}
