
/*
======================================================================================================
App.java , class loads while the app starts
======================================================================================================
*/ 
package com.sayed.alarm_app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;

import android.net.Uri;
import android.os.Build;

import java.lang.reflect.Array;

import java.util.HashMap;
import java.util.Map;


public class App extends Application {
    public static final String CHANNEL_ID = "alarm_App";

    public static Uri[] ringtones;
    public static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
        ringtones = getAllRigntomes();

        preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public Uri[] getAllRigntomes() {
        RingtoneManager ringtoneMgr = new RingtoneManager(this);
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneMgr.getCursor();
        int alarmsCount = alarmsCursor.getCount();
        if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {
            return null;
        }
        Uri[] alarms = new Uri[alarmsCount];
        while(!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
            int currentPosition = alarmsCursor.getPosition();
            alarms[currentPosition] = ringtoneMgr.getRingtoneUri(currentPosition);
        }
        alarmsCursor.close();
        return alarms;
    }

}

/*
======================================================================================================
Manifest.xml
======================================================================================================
*/ 

<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.sayed.alarm_app">

    <uses-permission android:name="android.permission.VIBRATE" />

    <application
        android:name=".App"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".RingtoneActivity">

        </activity>
        <activity
            android:name=".SnoozeActivity"
            android:noHistory="true"></activity>
        <activity
            android:name=".StopAlarmActivity"
            android:noHistory="true" />
        <activity
            android:name=".MainActivity"
            android:noHistory="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <receiver android:name=".BroadcastReceivers.AlertReceiver" />

        <service android:name=".Services.MyAlarmService" />
    </application>

</manifest>