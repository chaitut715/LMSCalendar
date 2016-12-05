package com.chaitu.lmscalendar.sakai.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.chaitu.lmscalendar.sakai.sync.EventsSyncService;

public class AlarmReceiver extends BroadcastReceiver {

    public static String ACTION_ALARM = "com.alarammanager.alaram";

    @Override
    public void onReceive(Context context, Intent intent) {

        /*Log.i("Alarm Receiver", "Entered");
        Toast.makeText(context, "Entered", Toast.LENGTH_SHORT).show();*/

        Bundle bundle = intent.getExtras();
        String action = bundle.getString(ACTION_ALARM);
        Log.i("Alarm Receiver", "Starting event EventsSyncService");
        Log.i("Alarm Receiver", "If loop");
        Intent inService = new Intent(context, EventsSyncService.class);
        context.startService(inService);

    }

}
