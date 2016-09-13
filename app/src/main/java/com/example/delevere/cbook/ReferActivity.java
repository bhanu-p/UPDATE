package com.example.delevere.cbook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReferActivity  extends BroadcastReceiver {



    public void onReceive(Context context, Intent intent) {
        String referrer = intent.getStringExtra("referrer");
        Log.w("TEST", "Referrer is: " + referrer);

    }
}
