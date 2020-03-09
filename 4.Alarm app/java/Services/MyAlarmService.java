package com.sayed.alarm_app.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

import com.sayed.alarm_app.App;
import com.sayed.alarm_app.R;
import com.sayed.alarm_app.SnoozeActivity;
import com.sayed.alarm_app.StopAlarmActivity;

import static com.sayed.alarm_app.App.CHANNEL_ID;

public class MyAlarmService extends Service {

    private Ringtone ringtone;
    MediaPlayer mp;

    @Override
    public void onCreate() {
        Toast.makeText(this, "MyAlarmService.onCreate()", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("AlarmService", "Alarm Starting to ring");

        long id = intent.getLongExtra("id", 0);

        Intent stopService = new Intent(this, StopAlarmActivity.class);
        stopService.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) id, stopService, 0);

        Intent snoozeAction = new Intent(this, SnoozeActivity.class);
        snoozeAction.putExtra("id", id);
        PendingIntent snoozePendingIntent = PendingIntent.getActivity(this, (int)id, snoozeAction, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alarm Service running")
                .setContentText("Wakie Wakie")
                .setSmallIcon(R.drawable.ic_access_alarm_black_24dp)
                .addAction(R.drawable.ic_close_24dp, "Stop", pendingIntent)
                .addAction(R.drawable.ic_snooze_black_24dp, "Snooze", snoozePendingIntent)
                .build();

        startForeground((int) id, notification);

        String uriStr = App.preferences.getString( "ringtone", null );
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        if(uriStr != null){
            ringtoneUri = Uri.parse(uriStr);
        }
        mp = MediaPlayer.create(getBaseContext(), ringtoneUri);
        mp.setVolume(100, 100);
        mp.setLooping(true);
        mp.start();

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        assert vibrator != null;
        vibrator.vibrate(1000);

        Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();

        return Service.START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
//        this.ringtone.stop();
        this.mp.stop();
        Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
    }

}