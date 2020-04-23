
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



//=======================================MRE-API-CLIENT=========================================
    public class APIServiceProvider {

    private static APIServiceProvider apiServiceProvider;

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    APIInterface apiInterface;

    private APIServiceProvider(String baseUrl,
                               long readTimeout,
                               long connectTimeout,
                               HttpLoggingInterceptor.Level logLevel){

        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(logLevel);

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(RestAPIs.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        apiInterface = retrofit.create(APIInterface.class);
    }

    public static APIServiceProvider getApiServiceProvider(String baseUrl,
                                                           long readTimeout,
                                                           long connectTimeout,
                                                           HttpLoggingInterceptor.Level logLevel){
        if(apiServiceProvider==null){
            apiServiceProvider=new APIServiceProvider(baseUrl,readTimeout,connectTimeout,logLevel);
        }
        return  apiServiceProvider;
    }

    public Call<Author> getRegisterAuthorApi(Author author){
        return apiInterface.registerAuthor(author);
    }

    public Call<LoginToken> loginAuthor(Author author){
        return apiInterface.loginAuthor(author);
    }

    public Call<ResponseBody> logoutAuthor(Author author){
        return apiInterface.logoutAuthor(author);
    }

    public Call<ToDoItem> addToDoItem(ToDoItem toDoItem){
        return apiInterface.addToDoItem(AppConfig.getSessionTokenValue(),toDoItem);
    }

    public Call<List<ToDoItem>> getToDoList(){
        return apiInterface.getToDoList(AppConfig.getSavedSuccessfulAuthor().getAuthorEmailId(),AppConfig.getSessionTokenValue());
    }

    public Call<ResponseBody> deleteToDo(ToDoItem toDoItem){
        return apiInterface.deleteToDo(AppConfig.getSessionTokenValue(),toDoItem);
    }

    public Call<ToDoItem> modifyToDoItem(ModifyToDoPayloadBean modifyToDoPayloadBean){
        return apiInterface.modifyToDoItem(AppConfig.getSessionTokenValue(),modifyToDoPayloadBean);
    }

}



//App config class
//specify it in manifest file
public class AppConfig extends Application {

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

    public static API_ENDPOINTS selectedEndPoint;
    public boolean isEmulator;

    static Context context;

    private static APIServiceProvider apiServiceProvider;

    public static enum API_ENDPOINTS{
        localhost, remote
    }


    @Override
    public void onCreate() {
        super.onCreate();

//shared pred intialization
        sharedPreferences = getSharedPreferences("appprefrences.xml",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //shared pred intialization
        isEmulator = Util.isEmulator();
        //context intialization
        context = getApplicationContext();

//retrofit 2 client intialization
        apiServiceProvider = APIServiceProvider.getApiServiceProvider(
                RestAPIs.getBaseUrl(),
                5000,
                5000,
                HttpLoggingInterceptor.Level.BODY);

    }

    public static void saveUserName(String username){
        editor.putString("username",username);
        editor.commit();
    }

    public static void saveSuccessfulLoginUser(String jsonString){
        editor.putString("user",jsonString);
        editor.commit();
    }

    public static Author getSavedSuccessfulAuthor(){
        Author author = new GsonBuilder().create().fromJson(sharedPreferences.getString("user",null), Author.class);
        return author;
    }

    public static void saveSessionTokenValue(String token){
        editor.putString("token",token);
        editor.commit();
    }

    public static String getSessionTokenValue(){
        return sharedPreferences.getString("token",null);
    }

    public static  String getSavedUserName(){
        return sharedPreferences.getString("username",null);
    }

    public static void savePassword(String password){
        editor.putString("password",password);
        editor.commit();
    }

    public static String getSavedPassword(){
        return sharedPreferences.getString("password",null);
    }


    public static String getSessionId(){
        return sharedPreferences.getString("sessionId",null);
    }

    public static void saveSessionId(String sessionId){
        editor.putString("sessionId",sessionId);
        editor.commit();
    }

    public static void setServerEndPointPreference(boolean endPoint){
        editor.putBoolean("endpoint",endPoint);
        editor.commit();
    }

    public static boolean getSeverEndPointPreference(){
        return sharedPreferences.getBoolean("endpoint", true);
    }

    public static Context getContext(){
        return context;
    }

    public static APIServiceProvider getApiServiceProvider(){
        return apiServiceProvider;
    }

}