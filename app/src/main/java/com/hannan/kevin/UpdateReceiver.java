package com.hannan.kevin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by khannan on 8/22/16.
 */
public class UpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Log.d("receiver", "Got message: ");
        //state=3;
        //Toast.makeText(context, "got it", Toast.LENGTH_SHORT).show();
    }
};