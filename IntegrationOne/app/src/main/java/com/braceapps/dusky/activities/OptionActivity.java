package com.braceapps.dusky.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.braceapps.dusky.R;
import com.braceapps.dusky.Servicehandler.InitStatus;
import com.braceapps.dusky.uicomponents.uiadaptor.OptionCardAdaptor;
import com.braceapps.dusky.uicomponents.uipojo.OptionCardPOJO;

import java.util.ArrayList;
import java.util.List;


public class OptionActivity extends ActionBarActivity {
    public static Activity finishActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        finishActivity=this;
        InitStatus init=new InitStatus();
        if(init.getSpecificParameter(this,InitStatus.tncAccepted).matches("0")){
            Intent intent=new Intent(this,InfoActivity.class);
            startActivity(intent);
            finish();
        }

        RecyclerView mRecyclerView = null;
        CardView mCardView = (CardView) findViewById(R.id.card_view);
        RecyclerView.LayoutManager mLayoutManager;
        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        List<OptionCardPOJO> list = new ArrayList<OptionCardPOJO>();

        list.add(new OptionCardPOJO("Connect another Device", "Here's the app which will connect your devices any where in the world." + System.getProperty("line.separator") + "Select this option to send call log and messages on other device." + System.getProperty("line.separator") + "You can simply customize the list of items you'd like to share in the setting menu", "Get Started"));
        list.add(new OptionCardPOJO("Show device ID", "Let other device connect your device." + System.getProperty("line.separator") + "Select this option to see your device ID so that other device can connect you simply by entering the ID.", "Show me device ID"));

        RecyclerView.Adapter mAdapter;
        mAdapter = new OptionCardAdaptor(list, this);


        mRecyclerView.setAdapter(mAdapter);
       /* ContactInfo cinfo=new ContactInfo();

       Toast.makeText(this, cinfo.getContactName(this,"+91-8983-061589"), Toast.LENGTH_LONG).show();*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_option, menu);
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
