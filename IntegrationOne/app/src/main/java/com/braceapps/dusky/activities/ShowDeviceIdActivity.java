package com.braceapps.dusky.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.braceapps.dusky.IMEI.ManagerIMEI;
import com.braceapps.dusky.R;


public class ShowDeviceIdActivity extends ActionBarActivity {
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_device_id);
        TextView IMEITv = (TextView) findViewById(R.id.imeiTV);

        contextOfApplication = this;
        ManagerIMEI objIMEI = new ManagerIMEI();
        String L_imei = objIMEI.getStoredIMEI(this);
        IMEITv.setText(L_imei);
        ImageButton acceptBtn = (ImageButton) findViewById(R.id.cancelRequestPreSlaveButton);


        View.OnClickListener backbtnListner = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(contextOfApplication,OptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        };
        acceptBtn.setOnClickListener(backbtnListner);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_show_device_id, menu);
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
