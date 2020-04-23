


===================================================================================
SmsRepository ---- 
===================================================================================

package com.datahost.sms_read.repository;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.datahost.sms_read.Database.SmsDatabase;
import com.datahost.sms_read.MainActivity;
import com.datahost.sms_read.config.AppController;
import com.datahost.sms_read.dao.SmsDao;
import com.datahost.sms_read.model.SmsData;
import com.datahost.sms_read.model.UploadedData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.datahost.sms_read.config.MyStrings.MY_PREFS_NAME;
import static com.datahost.sms_read.config.MyStrings.URL;

//communicates with NoteDao
public class SmsRepository {
    private SmsDao smsDao;

    //make sure live datas are not null,,,,------------
    private LiveData<List<UploadedData>> allNotes;
    private MutableLiveData<String> token;
    private MutableLiveData<List<SmsData>> smsLists;

    Application application;
    private static SmsRepository instance;


    public SmsRepository(Application application) {
//        SmsDatabase database = SmsDatabase.getInstance(application);
        this.application = application;
//        smsDao = database.smsDao();
//        allNotes = smsDao.getAllNotes();
        smsLists = (MutableLiveData<List<SmsData>>) getAllSms();
        token = (MutableLiveData<String>) getToken();
    }


    public static SmsRepository getInstance(Application application) {
        if(instance == null) {
            synchronized (SmsRepository.class) {
                if(instance == null) {
                    instance = new SmsRepository(application);
                }
            }
        }
        return instance;
    }

    public void insert(UploadedData data) {
        new InsertNoteAsyncTask(smsDao).execute(data);
    }

    public void update(UploadedData data) {
        new UpdateNoteAsyncTask(smsDao).execute(data);
    }

    public void delete(UploadedData data) {
        new DeleteNoteAsyncTask(smsDao).execute(data);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(smsDao).execute();
    }

    public LiveData<List<UploadedData>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<SmsData>> getAllSms() {
        if (smsLists == null) {
            smsLists = new MutableLiveData<List<SmsData>>();;
        }
        return smsLists;
    }

    public LiveData<String> getToken(){
        if (token == null) {
            token = new MutableLiveData<String>();;
        }
        SharedPreferences prefs = application.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String mytoken = prefs.getString("token", "No token set");
        token.postValue(mytoken);
        return token;
    }

    public void setToken(String mytoken){
        SharedPreferences.Editor editor = application.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("token", mytoken);
        editor.apply();
        token.postValue(mytoken);
    }

    public void startSmsRetriever(){
        new SmsAsyncTask(application).execute();
    }


    //all operations ---------------
    private static class InsertNoteAsyncTask extends AsyncTask<UploadedData, Void, Void> {
        private SmsDao noteDao;

        private InsertNoteAsyncTask(SmsDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(UploadedData... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<UploadedData, Void, Void> {
        private SmsDao noteDao;

        private UpdateNoteAsyncTask(SmsDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(UploadedData... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<UploadedData, Void, Void> {
        private SmsDao noteDao;

        private DeleteNoteAsyncTask(SmsDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(UploadedData... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private SmsDao noteDao;

        private DeleteAllNotesAsyncTask(SmsDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

    private class SmsAsyncTask extends AsyncTask<Void, Void, String> {

//        private ProgressDialog dialog;
        private String paramOne;
        private int paramTwo;
        private Application application;

        public SmsAsyncTask(Application activity) {
//            dialog = new ProgressDialog(activity);
            this.application = activity;
//            this.paramOne = paramOne; // "Hello"
//            this.paramTwo = paramTwo; // 123
        }

        @Override
        protected void onPreExecute() {
//            if (dialog != null) {
//                dialog.setMessage("Fetching bikash SMS data...");
//                dialog.setIndeterminate(true);
//                dialog.show();
//            }
        }

        @Override
        protected String doInBackground(Void... params) {
//            if(smsLists!=null) smsLists.;
            final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
            Cursor cursor = application.getContentResolver().query(SMS_INBOX, null, "address = ?",
                    new String[]{"+8801777333677"}, "date desc");
            List<SmsData> smsStringLists = new ArrayList<>();
            String msgData = "";
            String[] decodedData;
            String filteringForReceivedMsgText = "";
            String amount = "";
            String from = "";
            String tranxId = "";
            String date = "";


            while (cursor.moveToNext()) {
//            "total size: "+cursor.getColumnCount()+ " - "+
                msgData = cursor.getString(cursor.getColumnIndex("body"));

                decodedData = msgData.split(" ");

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
                        smsStringLists.add(data);

                        Log.i("DATA_1", decodedData.toString());
                    }

                    if (decodedData[3].equals("deposit")) {

                        amount = decodedData[8];
                        from = "Bank";
                        tranxId = decodedData[Arrays.asList(decodedData).indexOf("TrxID") + 1];
                        date = decodedData[Arrays.asList(decodedData).indexOf("at") + 1] + " " + decodedData[Arrays.asList(decodedData).indexOf("at") + 2];

                        SmsData data = new SmsData(msgData, tranxId, amount, from, date);
                        smsStringLists.add(data);
                    }

                }
            }
            smsLists.postValue(smsStringLists);
            return "ok";
        }

        @Override
        protected void onPostExecute(String result) {
//            if (dialog != null) {
//                dialog.dismiss();
//                dialog.hide();
//            }
//            TextView txt = (TextView) findViewById(R.id.output);
//            txt.setText("Executed"); // txt.setText(result);
            // You might want to change "executed" for the returned string
            // passed into onPostExecute(), but that is up to you
        }
    }

    public void postDataToServer() {
        // Post request
        String tag_json_obj = "json_obj_req";
        //"http://103.86.197.83/api.ecure.oauth/
        String url = URL;

        List<SmsData> listsOfSms = new ArrayList<>();
        listsOfSms = smsLists.getValue();
        final List<JSONObject> listsOfData = new ArrayList<>();
        final JSONObject SmsObject = new JSONObject();
        final JSONObject mainBody = new JSONObject();
        try {
            for (int i = 0; i < listsOfSms.size(); i++) {
                SmsObject.put("mobile_no", listsOfSms.get(i).getFrom());
                SmsObject.put("amount", listsOfSms.get(i).getAmount());
                SmsObject.put("trxn_id", listsOfSms.get(i).getTrXId());
                listsOfData.add(SmsObject);
            }
            mainBody.put("data", listsOfData);
            mainBody.put("token", getToken().getValue());
            Log.i("MY_DATA", mainBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        try {
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            SmsObject.put("mobile_no", "02983");
//            SmsObject.put("amount", "pass");
//            SmsObject.put("trxn_id", "12321");
//            listsOfData.add(SmsObject);
//            SmsObject.put("mobile_no", "243");
//            SmsObject.put("amount", "24443");
//            SmsObject.put("trxn_id", "4323423");
//            listsOfData.add(SmsObject);
//            mainBody.put("data", listsOfData);
//            final String mRequestBody = mainBody.toString();
//
//            Log.i("body", mRequestBody.toString());

//            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://api-bkash.aamarpay.com/api/trx/v1/bkashapi.php", new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.i("LOG_RESPONSE", response);
//                    Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("LOG_RESPONSE", error.toString());
//                }
//            }) {
//                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }
//
//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
//                        return null;
//                    }
//                }
//
//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                    String responseString = "";
//                    if (response != null) {
//                        responseString = String.valueOf(response.statusCode);
//                        Log.i("MY_RESPONSE", responseString);
//                    }
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }
//            };
//
//            requestQueue.add(stringRequest);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "error occured", Toast.LENGTH_SHORT).show();
//        }


        //---------------------
//
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, mainBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

//                        Toast.makeText(MainActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                        Toast.makeText(application, "Sent", Toast.LENGTH_SHORT).show();
                        Log.i("response", response.toString());
//                        UploadedData note = null;
//                        try {
//                            note = new UploadedData(SmsObject.getString("trxn_id"),SmsObject.getString("mobile_no"),SmsObject.getString("amount"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        noteViewModel.insert(note);

//                        try {
//                            if(response.getJSONObject("tokens").getString("accessToken") != null){
//
//                                String token = response.getJSONObject("tokens").getString("accessToken");
//                                String refreshToken = response.getJSONObject("tokens").getString("refreshToken");
//                                String userId = response.getJSONObject("user").getString("id");
//                                String phoneNumber = response.getJSONObject("user").getString("phoneNumber");
//                                String em = response.getJSONObject("user").getString("email");
//                                String userName = response.getJSONObject("user").getString("userName");
//
//                            }

//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;


                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                        Toast.makeText(application, obj.toString(), Toast.LENGTH_SHORT).show();

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

                if (error instanceof TimeoutError) {
                    Toast.makeText(application,
                            "Time out", Toast.LENGTH_LONG).show();
                }

            }

        });
//            {
        /**
         * Passing some request headers
         * */
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }

//            };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}