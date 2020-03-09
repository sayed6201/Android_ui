package com.sayed.alarm_app.BroadcastReceivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import com.sayed.alarm_app.Services.MyAlarmService;


public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        long id = intent.getLongExtra("id",0);

        String action = intent.getStringExtra("action");

//        Intent serviceIntent = new Intent(context, MyAlarmService.class);
//        context.startService(serviceIntent.putExtra("id",id));

        if(action == null){
            Intent serviceIntent = new Intent(context, MyAlarmService.class);
            serviceIntent.putExtra("id", id);
            ContextCompat.startForegroundService(context, serviceIntent);
        }else if(action.equals("stop")){
            Intent serviceIntent = new Intent(context, MyAlarmService.class);
            context.stopService(serviceIntent);
        }

    }

//    public void stopService(View v) {
//        Intent serviceIntent = new Intent(co, MyAlarmService.class);
//        stopService(serviceIntent);
//    }
}