package com.braceapps.dusky.Broadcasters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.braceapps.dusky.activities.MainTabActivity;

/**
 * Created by paresh.boraste on 6/27/2015.
 */
public class SlaveUpdate extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        MainTabActivity mainTabActivity = ((MainTabActivity) context.getApplicationContext());
        //MainTabActivity.CallLogFragment.updatelistview();

    }

}

