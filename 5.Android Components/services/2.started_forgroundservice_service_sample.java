
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
froground service example: 1
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

 In Activity class, start service using startForegroundService() instead of startService()
____________________________________________________________________________________________________________

    Intent myService = new Intent(this, MyService.class);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(myService);
    } else {
        startService(myService);
    }


 Now in Service class in onStartCommand() do as following
 ____________________________________________________________________________________________________________

@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    ......
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        Notification.Builder builder = new Notification.Builder(this, ANDROID_CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setAutoCancel(true);

        Notification notification = builder.build();
        startForeground(1, notification);

    } else {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Notification notification = builder.build();

        startForeground(1, notification);
    }
    return START_NOT_STICKY;
}

Note: Using Notification.Builder instead of NotificationCompat.Builder made it work. Only in Notification.Builder you will need to provide Channel ID which is new feature in Android Oreo.

Hope it works!

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
froground service example: 2
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
App.java
1   package com.codinginflow.foregroundserviceexample;
2    
3   import android.app.Application;
4   import android.app.NotificationChannel;
5   import android.app.NotificationManager;
6   import android.os.Build;
7    
8    
9   public class App extends Application {
10      public static final String CHANNEL_ID = "exampleServiceChannel";
11   
12      @Override
13      public void onCreate() {
14          super.onCreate();
15   
16          createNotificationChannel();
17      }
18   
19      private void createNotificationChannel() {
20          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
21              NotificationChannel serviceChannel = new NotificationChannel(
22                      CHANNEL_ID,
23                      "Example Service Channel",
24                      NotificationManager.IMPORTANCE_DEFAULT
25              );
26   
27              NotificationManager manager = getSystemService(NotificationManager.class);
28              manager.createNotificationChannel(serviceChannel);
29          }
30      }
31  }
 
AndroidManifest.xml:
AndroidManifest.xml
1   <?xml version="1.0" encoding="utf-8"?>
2   <manifest xmlns:android="http://schemas.android.com/apk/res/android"
3       package="com.codinginflow.foregroundserviceexample">
4    
5       <application
6           android:name=".App"
7           android:allowBackup="true"
8           android:icon="@mipmap/ic_launcher"
9           android:label="@string/app_name"
10          android:roundIcon="@mipmap/ic_launcher_round"
11          android:supportsRtl="true"
12          android:theme="@style/AppTheme">
13          <activity android:name=".MainActivity">
14              <intent-filter>
15                  <action android:name="android.intent.action.MAIN" />
16   
17                  <category android:name="android.intent.category.LAUNCHER" />
18              </intent-filter>
19          </activity>
20          <service android:name=".ExampleService" />
21      </application>
22   
23  </manifest>
 
activity_main.xml:
activity_main.xml
1   <?xml version="1.0" encoding="utf-8"?>
2   <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
3       xmlns:app="http://schemas.android.com/apk/res-auto"
4       xmlns:tools="http://schemas.android.com/tools"
5       android:layout_width="match_parent"
6       android:layout_height="match_parent"
7       android:gravity="center"
8       android:orientation="vertical"
9       android:padding="16dp"
10      tools:context="com.codinginflow.foregroundserviceexample.MainActivity">
11   
12      <EditText
13          android:id="@+id/edit_text_input"
14          android:layout_width="match_parent"
15          android:layout_height="wrap_content"
16          android:hint="Input" />
17   
18      <Button
19          android:layout_width="match_parent"
20          android:layout_height="wrap_content"
21          android:onClick="startService"
22          android:text="Start Service" />
23   
24      <Button
25          android:layout_width="match_parent"
26          android:layout_height="wrap_content"
27          android:onClick="stopService"
28          android:text="Stop Service" />
29   
30  </LinearLayout>
 
ExampleService.java:
ExampleService.java
1   package com.codinginflow.foregroundserviceexample;
2    
3   import android.app.Notification;
4   import android.app.PendingIntent;
5   import android.app.Service;
6   import android.content.Intent;
7   import android.os.IBinder;
8   import android.support.annotation.Nullable;
9   import android.support.v4.app.NotificationCompat;
10   
11  import static com.codinginflow.foregroundserviceexample.App.CHANNEL_ID;
12   
13   
14  public class ExampleService extends Service {
15   
16      @Override
17      public void onCreate() {
18          super.onCreate();
19      }
20   
21      @Override
22      public int onStartCommand(Intent intent, int flags, int startId) {
23          String input = intent.getStringExtra("inputExtra");
24   
25          Intent notificationIntent = new Intent(this, MainActivity.class);
26          PendingIntent pendingIntent = PendingIntent.getActivity(this,
27                  0, notificationIntent, 0);
28   
29          Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
30                  .setContentTitle("Example Service")
31                  .setContentText(input)
32                  .setSmallIcon(R.drawable.ic_android)
33                  .setContentIntent(pendingIntent)
34                  .build();
35   
36          startForeground(1, notification);
37   
38          //do heavy work on a background thread
39          //stopSelf();
40   
41          return START_NOT_STICKY;
42      }
43   
44      @Override
45      public void onDestroy() {
46          super.onDestroy();
47      }
48   
49      @Nullable
50      @Override
51      public IBinder onBind(Intent intent) {
52          return null;
53      }
54  }
 
MainActivity.java:
MainActivity.java
1   package com.codinginflow.foregroundserviceexample;
2    
3   import android.content.Intent;
4   import android.support.v4.content.ContextCompat;
5   import android.support.v7.app.AppCompatActivity;
6   import android.os.Bundle;
7   import android.view.View;
8   import android.widget.EditText;
9    
10  public class MainActivity extends AppCompatActivity {
11      private EditText editTextInput;
12   
13      @Override
14      protected void onCreate(Bundle savedInstanceState) {
15          super.onCreate(savedInstanceState);
16          setContentView(R.layout.activity_main);
17   
18          editTextInput = findViewById(R.id.edit_text_input);
19      }
20   
21      public void startService(View v) {
22          String input = editTextInput.getText().toString();
23   
24          Intent serviceIntent = new Intent(this, ExampleService.class);
25          serviceIntent.putExtra("inputExtra", input);
26   
27          ContextCompat.startForegroundService(this, serviceIntent);
28      }
29   
30      public void stopService(View v) {
31          Intent serviceIntent = new Intent(this, ExampleService.class);
32          stopService(serviceIntent);
33      }
34  }




++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Read SMS in background and send to server:
-------------------------------------------

    # you should have used startForegroundService instead of startService
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

____________________________________________________________________________________________________________
MainActivity.java start and stop froground servie
____________________________________________________________________________________________________________
public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Bkash Sms reader running");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }


____________________________________________________________________________________________________________
ForegroundService.java class code
____________________________________________________________________________________________________________
package com.datahost.sms_read.service;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    Timer timer;
    int i =0;
    TextView textview;
    RecyclerView rvSms;
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    ArrayList<SmsData> smsLists, smsListsOfToday;
    String filteringForReceivedMsgText;
    String[] decodedData;
    String amount = "";
    String from = "";
    String date = "";
    String tranxId = "";
    String Token = "123";
    String MY_PREFS_NAME = "MY_APP";
    SmsViewModel noteViewModel;
    AsyncTask<Void, Void, String> mTask, mTask2;
    AlertDialog.Builder builder1, builder11;
    AlertDialog alert11, alert111;
    String formattedDate, formattedDateStart;
    LinearLayout noDataView;
    boolean autoUpload = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Bikash Sms reader Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.sms)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        smsListsOfToday = new ArrayList<>();

        timer = new Timer();
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                        mTask2 = new SmsFetchForTodayAsyncTask().execute();
                        //noteViewModel.callSmsRetriever();
                        Log.i("TODAYS_DATA_TASK_CALLED", "OK"); }
        }, 0, 5000);
//        stopSelf();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        if(timer != null) timer.cancel();
//        Log.i("DESTORY","ondestroy called");
        super.onDestroy();
        timer.cancel();
        timer = null;
        stopForeground(true);
        stopSelf();//Add this. Since stopping a service in started in foreground is different from normal services.
        Log.d("","SERVICE HAS BEEN DESTROYED!!!");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Bikash Sms Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private final class SmsFetchForTodayAsyncTask extends AsyncTask<Void, Void, String> {

        public SmsFetchForTodayAsyncTask(){}

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(Void... params) {
            getSmsOfToday();
            if(smsListsOfToday.size() > 0 ){ postDataTodayDataToServer();}
            return "ok";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("DATA LENGTH: ",smsListsOfToday.size()+"");
        }
    }

//postDataTodayDataToServer date++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void postDataTodayDataToServer() {
        // Post request
        String tag_json_obj = "json_obj_req";

        JSONArray jsArray = new JSONArray();
        final JSONObject mainBody = new JSONObject();
        try {
            for (int i = 0; i < smsListsOfToday.size(); i++) {
                final JSONObject SmsObject = new JSONObject();
                SmsObject.put("mobile_no", smsListsOfToday.get(i).getFrom());
                SmsObject.put("amount", smsListsOfToday.get(i).getAmount());
                SmsObject.put("trxn_id", smsListsOfToday.get(i).getTrXId());
                jsArray.put(SmsObject);
            }

            mainBody.put("data", jsArray);
            Log.i("MY_DATA", mainBody.toString());
            Log.i("MY_ARRAY", jsArray.toString());


            RequestQueue queue = Volley.newRequestQueue(this);

            JsonArrayRequest request_json = new JsonArrayRequest(Request.Method.POST, BIKASH_URL, jsArray,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(final JSONArray response) {

                            Log.i("VOLLEY", response.toString());
                            Log.i("SENT", "ok");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.e("Error: ", volleyError.getMessage());
                    Log.e("VOLLEY", volleyError.toString());
                    Log.i("SENT_FAILED", "ERR");
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    // Add headers
                    return headers;
                }

                //Important part to convert response to JSON Array Again
                @Override
                protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                    String responseString;
                    JSONArray array = new JSONArray();
                    if (response != null) {

                        try {
                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject obj = new JSONObject(responseString);
                            (array).put(obj);
                        } catch (Exception ex) {
                        }
                    }
                    //return array;
                    return Response.success(array, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            queue.add(request_json);

        } catch (JSONException e) {
            e.printStackTrace();
//            dialog2[0].dismiss();
        }
    }

//gets SMS of todays date++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void getSmsOfToday() {
        smsListsOfToday.clear();

        Log.i("FETCHING_DATA","OK");

        //-------------------get todays date----------------------------------
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy'T'hh:mm:ss");
        SimpleDateFormat dfStart = new SimpleDateFormat("dd-MM-yyyy'T'00:00:00");

        formattedDate = df.format(c);
        formattedDateStart = dfStart.format(c);
        Log.d("STARTING_DATE", formattedDateStart);
        Log.d("CURRENT_DATE", formattedDate);

        Date start = null, end = null;

        try {
            start = df.parse(formattedDateStart);
            end = df.parse(formattedDateStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("starting-data:", start.getTime() + "");

        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(SMS_INBOX, null, "address = ? AND date>= ?", new String[]{SENDER, start.getTime() + ""}, null);
        String msgData = "";

        while (cursor.moveToNext()) {
//            "total size: "+cursor.getColumnCount()+ " - "+

            msgData = cursor.getString(cursor.getColumnIndex("body"));
            decodedData = msgData.split(" ");

            if(decodedData.length <= 1) continue;

            filteringForReceivedMsgText = decodedData[0] + decodedData[1] + decodedData[2];

            amount = "";
            from = "";
            tranxId = "";
            date = "";

            //filtering only cash in data
            if (filteringForReceivedMsgText.equals("Youhavereceived")) {

                //from mobile
                if (decodedData[3].equals("Tk")) {

                    amount = decodedData[4];
                    from = decodedData[6];
                    tranxId = decodedData[Arrays.asList(decodedData).indexOf("TrxID") + 1];
                    date = decodedData[Arrays.asList(decodedData).indexOf("at") + 1] + " " + decodedData[Arrays.asList(decodedData).indexOf("at") + 2];

                    SmsData data = new SmsData(msgData, tranxId, amount, from, date);
                    smsListsOfToday.add(data);

                    Log.i("DATA_1", decodedData.toString());
                }

                if (decodedData[3].equals("payment")) {

                    amount = decodedData[5];
                    from = decodedData[7];
                    tranxId = decodedData[Arrays.asList(decodedData).indexOf("TrxID") + 1];
                    date = decodedData[Arrays.asList(decodedData).indexOf("at") + 1] + " " + decodedData[Arrays.asList(decodedData).indexOf("at") + 2];

                    SmsData data = new SmsData(msgData, tranxId, amount, from, date);
                    smsListsOfToday.add(data);

                    Log.i("DATA_1", decodedData.toString());

                }

                if (decodedData[3].equals("deposit")) {

                    amount = decodedData[8];
                    from = "Bank";
                    tranxId = decodedData[Arrays.asList(decodedData).indexOf("TrxID") + 1];
                    date = decodedData[Arrays.asList(decodedData).indexOf("at") + 1] + " " + decodedData[Arrays.asList(decodedData).indexOf("at") + 2];

                    SmsData data = new SmsData(msgData, tranxId, amount, from, date);
                    smsListsOfToday.add(data);
                }

            }


        }

        cursor.close();
    }
}