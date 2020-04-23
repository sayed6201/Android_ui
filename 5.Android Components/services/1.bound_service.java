Some important NOTE on Service:

	1. Binding to a service doesn't start it, service must be started/unbounded before binding to it
	2. unbound service runs unless it is stopped, if the activty is destroyed it still runs
	3. activty is bound to a service(unbound service), to listen the changes
	4. activty can be bound to service if the service is not started yet, but binding won't start the service
	5. you can not stop a service which is boound to an activty/service/content provider..
	6. component that can bound to a service: content provider, 
	7. service always runs on UI thread



++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Started service
Bound service 
Example....
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
manifest.xml
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.sayed.ahmed.service_br_tester">
    
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
        

        <service android:name=".service.MyService"/>

    </application>

</manifest>





++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
MyService.java
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

public class MyService extends Service {

    //random number is stored is in this variable
    private int mRandomNumber;
    //when random number is on it is set to true
    private boolean mIsRandomGeneratorOn;

    //Min value of random number to be generated
    private final int MIN=0;
    //maximum number of Random number to be generated
    private final int MAX=100;

    //now the class can receive onBinde request from external agent/Activity
    //it will help to get instance of service from activty
    public class MyServiceBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        }
    }

    //object of Myservice class been created, it will be used to connect with Activity/service etc
    private IBinder mBinder=new MyServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("MyService","In OnBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i("MyService","In OnReBind");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i("MyService","Service Started");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
        Log.i("MyService","Service Destroyed");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MyService","In onStartCommend, thread id: "+Thread.currentThread().getId());
        mIsRandomGeneratorOn =true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();
        return START_STICKY;
    }

    private void startRandomNumberGenerator(){
        while (mIsRandomGeneratorOn){
            try{
                Thread.sleep(1000);
                if(mIsRandomGeneratorOn){
                    mRandomNumber =new Random().nextInt(MAX)+MIN;
                    Log.i("MyService","Thread id: "+Thread.currentThread().getId()+", Random Number: "+ mRandomNumber);
                }
            }catch (InterruptedException e){
                Log.i("MyService","Thread Interrupted");
            }

        }
    }

    private void stopRandomNumberGenerator(){
        mIsRandomGeneratorOn =false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("MyService","In onUnbind");
        return super.onUnbind(intent);
    }

    public int getRandomNumber(){
        return mRandomNumber;
    }
}





++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
MainActivity
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
package com.sayed.ahmed.service_br_tester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sayed.ahmed.service_br_tester.receiver.ConnectionReceiver;
import com.sayed.ahmed.service_br_tester.service.MyService;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonStart, buttonStop, buttonBind,buttonUnBind,buttonGetRandomNumber;
    private TextView textViewthreadCount;

    private MyService myService;
    private boolean isServiceBound;
    private ServiceConnection serviceConnection;

    private  Intent serviceIntent;

    boolean mStopLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("MainActivity", "MainActivity thread id: " + Thread.currentThread().getId());

        buttonStart = (Button) findViewById(R.id.buttonStartService);
        buttonStop = (Button) findViewById(R.id.buttonStopService);
        buttonBind=(Button)findViewById(R.id.buttonBindService);
        buttonUnBind=(Button)findViewById(R.id.buttonUnBindService);
        buttonGetRandomNumber=(Button)findViewById(R.id.buttonGetRandomNumber);

        textViewthreadCount = (TextView) findViewById(R.id.getRandomNumber_tv);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonBind.setOnClickListener(this);
        buttonUnBind.setOnClickListener(this);
        buttonGetRandomNumber.setOnClickListener(this);

        //service intent is created
        serviceIntent = new Intent(getApplicationContext(),MyService.class);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            //satrts service
            case R.id.buttonStartService:
                mStopLoop = true;
                startService(serviceIntent);
                break;

            //stops service
            case R.id.buttonStopService:
                /*mStopLoop = false;*/
                /* myAsyncTask.cancel(true);*/
                stopService(serviceIntent);
                break;

            //bind service
            case R.id.buttonBindService:
                bindService();
                break;

            //unbind service
            case R.id.buttonUnBindService:
                unbindService();
                break;

            //get's random number generated in service
            case R.id.buttonGetRandomNumber:
                setRandomNumber();
                break;
        }
    }

    private void bindService(){
        //checks weather the service is binded
        if(serviceConnection==null){

            //getting unscatnce of service
            serviceConnection=new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    MyService.MyServiceBinder myServiceBinder=(MyService.MyServiceBinder)iBinder;
                    myService=myServiceBinder.getService();
                    isServiceBound=true;
                }

                //when service is disconnected this method is called
                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceBound=false;
                }
            };
        }

        //service can be bound to activity once only..
        bindService(serviceIntent,serviceConnection, Context.BIND_AUTO_CREATE);

    }

    //when unbindbutton is clicked this method executes
    private void unbindService(){
        if(isServiceBound){
            //unbided through this method
            unbindService(serviceConnection);
            isServiceBound=false;
        }
    }

    //derieves the random number generated in service class and set in the textview
    private void setRandomNumber(){
        if(isServiceBound){
            textViewthreadCount.setText("Random number: "+myService.getRandomNumber());
        }else{
            textViewthreadCount.setText("Service not bound");
        }
    }

}
