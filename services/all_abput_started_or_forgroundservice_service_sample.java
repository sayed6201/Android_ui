++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
Read SMS in background and send to server
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