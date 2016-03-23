package com.braceapps.dusky.activities;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.braceapps.dusky.R;
import com.braceapps.dusky.Servicehandler.InitStatus;


public class Settings extends ActionBarActivity {
    Context contextOfApplication;
    String messagePositive="Tap to stop sending/recieving Messages";
    String callPositive="Tap to stop sending/recieving Call Log";
    String notificationPositive="Tap to stop recieving Notifications";

    String messageNegative="Tap to start sending/recieving Messages";
    String callNegative="Tap to start sending/recieving Call Log";
    String notificationNegative="Tap to start recieving Notifications";

    String colorPositive="#8CC29F";
    String colorNegative="#C75050";
    LinearLayout messageLL;
    LinearLayout callLogLL;
    LinearLayout notificationLL;

    TextView messageTV,callLogTV,notificationTV;
    String srmsg,srcall,shownotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        contextOfApplication = getApplicationContext();



        messageLL=(LinearLayout)findViewById(R.id.MessagesSettingLL);
        callLogLL=(LinearLayout)findViewById(R.id.CallSettingLL);
        notificationLL=(LinearLayout)findViewById(R.id.NotificationLL);

        messageTV=(TextView)findViewById(R.id.MessagesSettingTV);
        callLogTV=(TextView)findViewById(R.id.CallSettingTV);
        notificationTV=(TextView)findViewById(R.id.NotificationTV);

        messageLL.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    InitStatus init = new InitStatus();
                                    String msg = init.getSpecificParameter(contextOfApplication, InitStatus.srmessage);
                                    if(msg.matches("1")) {
                                        init.storetSpecificParameter(contextOfApplication, InitStatus.srmessage, "0");
                                    }else{
                                        init.storetSpecificParameter(contextOfApplication, InitStatus.srmessage, "1");
                                    }
                                    resetview();
                                }
                            });
        callLogLL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InitStatus init = new InitStatus();
                String call = init.getSpecificParameter(contextOfApplication, InitStatus.srcall);
                if(call.matches("1")) {
                    init.storetSpecificParameter(contextOfApplication, InitStatus.srcall, "0");
                }else{
                    init.storetSpecificParameter(contextOfApplication, InitStatus.srcall, "1");
                }
                resetview();
            }
        });
        notificationLL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InitStatus init = new InitStatus();
                String notification = init.getSpecificParameter(contextOfApplication, InitStatus.showNotification);
                if(notification.matches("1")) {
                    init.storetSpecificParameter(contextOfApplication, InitStatus.showNotification, "0");
                }else{
                    init.storetSpecificParameter(contextOfApplication, InitStatus.showNotification, "1");
                }
                resetview();
            }
        });

        resetview();
            }

            private void resetview() {
                InitStatus init = new InitStatus();
                //contextOfApplication = getApplicationContext();

                srmsg = init.getSpecificParameter(contextOfApplication, InitStatus.srmessage);
                srcall = init.getSpecificParameter(contextOfApplication, InitStatus.srcall);
                shownotifications = init.getSpecificParameter(contextOfApplication, InitStatus.showNotification);

                if (srmsg.matches("1")) {
                    messageLL.setBackgroundColor(Color.parseColor(colorPositive));
                    messageTV.setText(messagePositive);
                } else {
                    messageLL.setBackgroundColor(Color.parseColor(colorNegative));
                    messageTV.setText(messageNegative);
                    //init.storetSpecificParameter(contextOfApplication,InitStatus.srmessage,"0");
                }

                if (srcall.matches("1")) {
                    callLogLL.setBackgroundColor(Color.parseColor(colorPositive));
                    callLogTV.setText(callPositive);
                } else {
                    callLogLL.setBackgroundColor(Color.parseColor(colorNegative));
                    callLogTV.setText(callNegative);
                }

                if (shownotifications.matches("1")) {
                    notificationLL.setBackgroundColor(Color.parseColor(colorPositive));
                    notificationTV.setText(notificationPositive);
                } else {
                    notificationLL.setBackgroundColor(Color.parseColor(colorNegative));
                    notificationTV.setText(notificationNegative);
                }


            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
               // getMenuInflater().inflate(R.menu.menu_settings, menu);
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
        }
