package com.sayed.alarm_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sayed.alarm_app.Adapter.AlarmAdapter;
import com.sayed.alarm_app.Adapter.RigntoneAdapter;
import com.sayed.alarm_app.BroadcastReceivers.AlertReceiver;
import com.sayed.alarm_app.Entity.Alarm;

import java.net.URI;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    RecyclerView recyclerView;
    ArrayList<Alarm> list;
    AlarmAdapter adaptor;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.alarm_recyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        updateRecyclerView();


        FloatingActionButton buttonTimePicker = findViewById(R.id.button_timepicker);
        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        Log.i("ringtones",App.ringtones[0]+"");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.rigntone_id){
            dialogPopUp();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        String shift = c.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
        long id = DbOperations.addAlarm(AppDatabase.getAppDatabase(this), new Alarm(minute + "", hourOfDay + "", shift));
        updateRecyclerView();

//            updateTimeText(c, id);
        startAlarm(c, id);
    }

    private void updateTimeText(Calendar c, long id) {
        String timeText = "Alarm set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime()) + " id: " + id;
//            mTextView.setText(timeText);
    }

    private void updateRecyclerView() {
        list = (ArrayList<Alarm>) DbOperations.getAllAlarms(AppDatabase.getAppDatabase(this));
        adaptor = new AlarmAdapter(getApplicationContext(), list);
        recyclerView.setAdapter(adaptor);
        adaptor.setClickListener(new AlarmAdapter.onClickDeleteListener(){
            @Override
            public void onClickDelete(View v, int position) {
                cancelAlarm(list.get(position).getId());
                updateRecyclerView();
            }
        });
    }

//trigger alert receiver using time+++++++++++++++++++++++++++++++++++++++++++++++++++
    private void startAlarm(Calendar c, long id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) id, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        //only once the task is executed++++++++++++++++++
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        //task is executed INTERVAL_DAY ++++++++++++++++++
        //alarmManager.setRepeating(AlarmManager.RTC, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY ,pendingIntent);
    }

//cancel alert receiver++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private void cancelAlarm(int id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        alarmManager.cancel(pendingIntent);

        DbOperations.deleteAlarmById(AppDatabase.getAppDatabase(MainActivity.this), id);

        Toast.makeText(this, "Alarm deleted", Toast.LENGTH_SHORT).show();
    }

    public void dialogPopUp(){
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.layout_rigntome_dialog);
        ImageView close;
        RecyclerView rintoneRecyclerView = myDialog.findViewById(R.id.ringtone_recyclerView);
        rintoneRecyclerView.setHasFixedSize(true);
        rintoneRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Uri[]  uriList= App.ringtones;
        RigntoneAdapter rigntoneAdapter = new RigntoneAdapter(this, uriList);
        rintoneRecyclerView.setAdapter(rigntoneAdapter);

        rigntoneAdapter.setSelectedRingtone(new RigntoneAdapter.SelectedRingtone() {
            @Override
            public void onRigntoneSelected(int position, Uri uri) {
                Log.i("RING",position+"-"+uri);
                Toast.makeText(MainActivity.this, "Ringtone "+(position+1)+" selected as Alarm tone", Toast.LENGTH_SHORT).show();
                App.preferences.edit().putString( "ringtone", uri.toString() ).apply();
                myDialog.dismiss();
            }
        });

        close = myDialog.findViewById(R.id.close_dialog);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

}

