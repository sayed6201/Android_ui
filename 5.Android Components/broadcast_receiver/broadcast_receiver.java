
====================================================================================
NEW SRC CODE.. for OREO,
Rehister your receiver in CODE..

Example: 01
All in one
static and dynamic declearation..
custom action 


old link:  https://www.journaldev.com/10356/android-broadcastreceiver-example-tutorial

new link:  https://www.journaldev.com/23653/android-oreo-implicit-and-explicit-broadcast-receiver


1. Explicit Intents are used to call a particular component that you know of.
2. Implicit Intents are used when you don’t know the exact component to invoke.

1. Implicit Broadcast Receivers aren’t exclusive to your application.
        # Actions such as ACTION_BOOT_COMPLETED or CONNECTIVITY_CHANGE are categorised in implicit broadcast receivers. 
        # This is because when these events happen, all the applications registered with the event will get the information.

2. Explicit Broadcast Receivers are exclusive to your application. 
        # Only your application’s broadcast receiver will get triggered when the custom intent action you define, gets called.

====================================================================================



------------------------------------------------------------------------------------
MAnifest declaration
------------------------------------------------------------------------------------
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="https://schemas.android.com/apk/res/android"
    xmlns:tools="https://schemas.android.com/tools"
    package="com.journaldev.androidoreobroadcastreceiver">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme"
        tools:ignore="GoogleAppIndexingWarning">

//register inly explicit intent here
        <receiver android:name="com.journaldev.androidoreobroadcastreceiver.MyReceiver"
            android:enabled="true">

            <intent-filter>
                <action android:name="com.journaldev.AN_INTENT" />
            </intent-filter>

        </receiver>

        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

    </application>

</manifest>



------------------------------------------------------------------------------------
Activity
------------------------------------------------------------------------------------
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnExplicitBroadcast;

    MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnExplicitBroadcast = findViewById(R.id.btnExplicitBroadcast);
        btnExplicitBroadcast.setOnClickListener(this);
        myReceiver= new MyReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
//register implicit intents here
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");


        registerReceiver(myReceiver, filter);
    }

    public void broadcastIntent() {

        //creating custom broadcast 
        Intent intent = new Intent();
        intent.setAction("com.journaldev.AN_INTENT");
        intent.setComponent(new ComponentName(getPackageName(),"com.journaldev.androidoreobroadcastreceiver.MyReceiver"));
        getApplicationContext().sendBroadcast(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnExplicitBroadcast:
                broadcastIntent();
                break;
        }
    }
}



------------------------------------------------------------------------------------
MyReceiver
------------------------------------------------------------------------------------

public class MyReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action.equals("com.journaldev.AN_INTENT")) {
            Toast.makeText(context, "Explicit Broadcast was triggered", Toast.LENGTH_SHORT).show();
        }

        if (("android.net.conn.CONNECTIVITY_CHANGE").equals(action)) {
            Toast.makeText(context, "Implicit Broadcast was triggered using registerReceiver", Toast.LENGTH_SHORT).show();
        }

    }
}















==================================OLD CODE==================================================

public class MainActivity extends AppCompatActivity {
    ConnectionReceiver receiver;
    IntentFilter intentFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing broadcast receiver..
        receiver = new ConnectionReceiver();
        //creating custom Intent Filter/action.
        intentFilter = new IntentFilter("com.journaldev.broadcastreceiver.SOME_ACTION");

    }

    public void triggerCustomBroadcast(View v){
        //trigerring the broadcast...........
        Intent intent = new Intent("com.journaldev.broadcastreceiver.SOME_ACTION");
        sendBroadcast(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        //registering receiver with custom intent
        //the reveiver is also registered for (android.net.conn.CONNECTIVITY_CHANGE) action in the Manifest
        //one revicer can be used for multiple Broadcaster,
        registerReceiver(receiver, intentFilter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //unregistering receiver in Ondestroy..
        unregisterReceiver(receiver);
    }

}


------------------------------------------------------------------------------------
MAnifest declaration
------------------------------------------------------------------------------------
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.sayed.ahmed.service_br_tester">



    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!--<receiver android:name=".receiver.ConnectionReceiver">-->
            <!--<intent-filter>-->
                <!--<action android:name="android.net.conn.CONNECTIVITY_CHANGE" />-->
            <!--</intent-filter>-->
        <!--</receiver>-->


        <receiver
            android:name=".receiver.ConnectionReceiver"
            android:enabled="true"
            android:exported="false"
            >
            <intent-filter>
                <!--<action android:name="android.net.wifi.STATE_CHANGE" />-->
                <action android:name="android.intent.action.PHONE_STATE"/>
            </intent-filter>
        </receiver>

    </application>

</manifest>


------------------------------------------------------------------------------------
Receiver class
------------------------------------------------------------------------------------
public class ConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("API123",""+intent.getAction());

        if(intent.getAction().equals("com.journaldev.broadcastreceiver.SOME_ACTION"))
            Toast.makeText(context, "SOME_ACTION is received", Toast.LENGTH_LONG).show();

        else {
            //will execute if the action == android.net.conn.CONNECTIVITY_CHANGE
            

            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                try {
                    Toast.makeText(context, "Network is connected", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Network is changed or reconnected", Toast.LENGTH_LONG).show();
            }
        }
    }

}


====================================================================================
OLD..

Broadcast receiver LInk: 

vogella.com/tutorials/AndroidBroadcastReceiver/article.html

https://www.journaldev.com/10356/android-broadcastreceiver-example-tutorial

====================================================================================


====================================================================================
Example: 0
Automatically starting Services from a Receivers

BOOT_COMPLETED is triggered when the app is launched..
====================================================================================
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="de.vogella.android.ownservice.local"
    android:versionCode="1"
    android:versionName="1.0" >

    <uses-sdk android:minSdkVersion="10" />

    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

    <application
        android:icon="@drawable/icon"
        android:label="@string/app_name" >
        <activity
            android:name=".ServiceConsumerActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <receiver android:name="MyScheduleReceiver" >
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>
        <receiver android:name="MyStartServiceReceiver" >
        </receiver>
    </application>

</manifest>


------------------------------------------------------------------------------------
The receive would start the service as demonstrated in the following example code.
------------------------------------------------------------------------------------

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // assumes WordService is a registered service
        Intent intent = new Intent(context, WordService.class);
        context.startService(intent);
    }
}


====================================================================================
Example: 1 
Get phone number while being called,
static registration of broadcast and receiver
====================================================================================
------------------------------------------------------------------------------------
4.3. Implement receiver for the phone event
Create the MyPhoneReceiver class.
------------------------------------------------------------------------------------

package de.vogella.android.receiver.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyPhoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            Log.w("MY_DEBUG_TAG", state);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String phoneNumber = extras
                        .getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.w("MY_DEBUG_TAG", phoneNumber);
            }
        }
    }
}

----------------------------------------------------------------------------------------------------------------------------------------------------------------
4.4. Request permission
Add the android.permission.READ_PHONE_STATE permission to your manifest file which allows you to listen to state changes in your receiver. Also Register your receiver in your manifest file. The resulting manifest should be similar to the following listing.
----------------------------------------------------------------------------------------------------------------------------------------------------------------
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="de.vogella.android.receiver.phone"
    android:versionCode="1"
    android:versionName="1.0" >

     <uses-sdk android:minSdkVersion="15" />

    <uses-permission android:name="android.permission.READ_PHONE_STATE" >
    </uses-permission>

    <application
        android:icon="@drawable/icon"
        android:label="@string/app_name" >
           <activity
            android:name=".MainActivity"
            android:label="@string/title_activity_main" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <receiver android:name="MyPhoneReceiver" >
            <intent-filter>
                <action android:name="android.intent.action.PHONE_STATE" >
                </action>
            </intent-filter>
        </receiver>
    </application>

</manifest>



========================================================================================================================================================================
Example: 2 
-----------
static registration of broadcast and receiver
AlarmManager /AlertManager 
pending intent

Target:
--------
set pending intent and fire using AlarmManager to trigger broadcast receiver
-----
In this chapter we will schedule a receiver via the Android AlertManager/AlarmManager system service.
Once called, it uses the Android vibrator manager and a popup message (Toast) to notify the user.
========================================================================================================================================================================


----------------------------------------------------------------------------------------------------------------------------------------------------------------
5.2. Implement project
Create a new project called de.vogella.android.alarm with the activity called AlarmActivity.

Create the following layout.
----------------------------------------------------------------------------------------------------------------------------------------------------------------
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >

    <EditText
        android:id="@+id/time"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:hint="Number of seconds"
        android:inputType="numberDecimal" >
    </EditText>

    <Button
        android:id="@+id/ok"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:onClick="startAlert"
        android:text="Start Counter" >
    </Button>

</LinearLayout>

----------------------------------------------------------------------------------------------------------------------------------------------------------------
Create the following broadcast receiver class. This class will get the vibrator service.
package de.vogella.android.alarm;
----------------------------------------------------------------------------------------------------------------------------------------------------------------
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Don't panik but your time is up!!!!.",
                Toast.LENGTH_LONG).show();
        // Vibrate the mobile phone
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }

}


----------------------------------------------------------------------------------------------------------------------------------------------------------------
Register this class as a broadcast receiver in AndroidManifest.xml and request authorization to vibrate the phone.
----------------------------------------------------------------------------------------------------------------------------------------------------------------
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="de.vogella.android.alarm"
    android:versionCode="1"
    android:versionName="1.0" >

    <uses-sdk android:minSdkVersion="15" />

    <uses-permission android:name="android.permission.VIBRATE" >
    </uses-permission>

    <application
        android:icon="@drawable/icon"
        android:label="@string/app_name" >
        <activity
            android:name=".AlarmActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <receiver android:name="MyBroadcastReceiver" >
        </receiver>
    </application>

</manifest>

----------------------------------------------------------------------------------------------------------------------------------------------------------------
Change the code of your AlarmActivity class to the following. This activity creates an intent to start the receiver and register this intent with the alarm manager service.
----------------------------------------------------------------------------------------------------------------------------------------------------------------
package de.vogella.android.alarm;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AlarmActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void startAlert(View view) {
        EditText text = (EditText) findViewById(R.id.time);
        int i = Integer.parseInt(text.getText().toString());

        Intent intent = new Intent(this, MyBroadcastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);

        //Alarm manager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i * 1000), pendingIntent);
        Toast.makeText(this, "Alarm set in " + i + " seconds",Toast.LENGTH_LONG).show();
    }

}