package com.sayed.alarm_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.sayed.alarm_app.BroadcastReceivers.AlertReceiver;
import com.sayed.alarm_app.Services.MyAlarmService;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;


public class SnoozeActivity extends AppCompatActivity {

    int snoozeTime = 2;
    Button snoozeBtn;
    NumberPicker np;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze);
        final TextView displaySnooze = findViewById(R.id.snooze_display_tv);
        snoozeBtn = findViewById(R.id.snooze_btn);
        np = findViewById(R.id.numberPicker);

        np.setMinValue(0);
        np.setMaxValue(20);

        final long id = getIntent().getLongExtra("id", 0);
        displaySnooze.setText("Snooze for 0 minutes");

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                displaySnooze.setText("Snooze for " + numberPicker.getValue() + " minutes");

                snoozeTime = numberPicker.getValue();

                snoozeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.i("snooze_time", "" + snoozeTime);

                        Intent intent = new Intent(SnoozeActivity.this, AlertReceiver.class);
                        intent.putExtra("id", id);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(SnoozeActivity.this, (int) id, intent, 0);

                        long currentTimeMillis = System.currentTimeMillis();
                        long nextUpdateTimeMillis = currentTimeMillis + snoozeTime * DateUtils.MINUTE_IN_MILLIS;

                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, nextUpdateTimeMillis,pendingIntent );

                        Toast.makeText(SnoozeActivity.this, "snoozed for " + snoozeTime+" minutes", Toast.LENGTH_SHORT).show();
                        Intent serviceIntent = new Intent(SnoozeActivity.this, MyAlarmService.class);
                        stopService(serviceIntent);
                        finish();
                    }
                });

            }
        });


    }

}
