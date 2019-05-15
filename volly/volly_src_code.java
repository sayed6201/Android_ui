
/*
------------------------------------------------------
simple GET request
------------------------------------------------------
*/
 public void getConsultationMainServiceCharges(String drId) {
        // Tag used to cancel the request

        String tag_json_obj = "json_array_req";
        String url ="http://103.86.197.83/api.appointment.ecure24.com/api/doctors/"+drId;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        pDialog.hide();
                        pDialog.dismiss();

                        try {

                            for (int i = 0; i < response.getJSONArray("consultationMainServiceCharges").length(); i++) {

                                String sn = response.getJSONArray("consultationMainServiceCharges").getJSONObject(i).getJSONObject("service").getString("serviceName");
                                String id = response.getJSONArray("consultationMainServiceCharges").getJSONObject(i).getString("id");

//                                tv.append(sn+" "+id);

                                serviceId.put(sn,id);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("response", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
                pDialog.dismiss();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);










/*
------------------------------------------------------
POST request with header DATA
------------------------------------------------------

-----------------------------------------------------
REQUEST STRUCTURE
-----------------------------------------------------
{
    "appointment_Schedule_Day_Slot_MappingId": "636711476987797174",
    "doctorId": "E000024",
    "visitedDate": "2019-03-19",
    "timeSlot": "18:00:00",
    "patientInfo": {
        "sex": "male",
        "patientName": "test by siddik",
        "age2": 35,
        "addressInfo": {
            "mobile": "01847140114"
        }
    },
    "consultationMainServiceCharges": "636711476987797168"
}
*/

        public void requestForAppointment(){
        // Post request
        String tag_json_obj = "json_obj_req";
        String url ="http://103.86.197.83/api.appointment.ecure24.com/api/appointments";

        JSONObject address = new JSONObject();
        JSONObject patientObj = new JSONObject();
        JSONObject rootObj = new JSONObject();
        try {
            address.put("mobile",mobile.getText().toString().trim());

            patientObj.put("sex",sexVar);
            patientObj.put("patientName",name.getText().toString().trim());
            patientObj.put("age2",age.getText().toString().trim());
            patientObj.put("addressInfo",address);


            rootObj.put("appointment_Schedule_Day_Slot_MappingId", slotId);
            rootObj.put("doctorId", drId);
            rootObj.put("visitedDate", visitDate);
            rootObj.put("timeSlot", timeSlot);
            rootObj.put("consultationMainServiceCharges", selectedServiceId);
            rootObj.put("patientInfo", patientObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("datatta",rootObj.toString());
//        tv.setText(rootObj.toString()+"");


            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, rootObj,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(AppointmentActivity.this);

//                            tv.setText(response+"");

                            try {
                                builder1.setMessage("Appointment Booked. Your serial No is "+response.getString("serialNo"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();

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

//                            tv.setText(res);

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(AppointmentActivity.this);
                            String msg = "";
                            for (int i=0;i<obj.getJSONArray("messages").length();i++ ){
                                msg = msg+obj.getJSONArray("messages").getString(i)+"\n";
                            }
//                            if(msg.equals("Sorry invalid time slot.")){
//                                msg = "This Slot is booked. try another one";
//                            }
                            builder1.setMessage(msg);
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

//                            builder1.setNegativeButton(
//                                    "No",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//                                            dialog.cancel();
//                                        }
//                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }

                }

            })
            {
                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }

            }
            ;

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }





/*
------------------------------------------------------
POST request with Error handling
------------------------------------------------------
*/

    public void requestForRetrieveLogInData(String userId, String refreshToken){
        // Post request
        String tag_json_obj = "json_obj_req";
        //"http://103.86.197.83/api.ecure.oauth/
        String url = ApiClass.ApiAuthUrl+"api/token";
        //api.auth.ecure24.com


        JSONObject patient = new JSONObject();

        try {
            patient.put("userId",userId);
            patient.put("password","pass");
            patient.put("grantType","refresh_token");
            patient.put("refreshToken",refreshToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, patient,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getJSONObject("tokens").getString("accessToken") != null){

                                String token = response.getJSONObject("tokens").getString("accessToken");
                                String refreshToken = response.getJSONObject("tokens").getString("refreshToken");
                                String userId = response.getJSONObject("user").getString("id");
                                String phoneNumber = response.getJSONObject("user").getString("phoneNumber");
                                String em = response.getJSONObject("user").getString("email");
                                String userName = response.getJSONObject("user").getString("userName");

                                LoggedpatientDetail loggedpatientDetail = new LoggedpatientDetail(token,userId,em,phoneNumber,userName);

                                SharedPreferences.Editor editor = getSharedPreferences(Datas.spName, MODE_PRIVATE).edit();
                                editor.putString("isLogged","true");
                                editor.putString("refresh_token", refreshToken);
                                editor.putString("logged_id", userId);
                                editor.apply();

                                Intent i = new Intent(SplashActivity.this, PatientProfileActivity.class);
                                i.putExtra("patientObj",loggedpatientDetail);
                                startActivity(i);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                        Toast.makeText(SplashActivity.this, obj.getJSONArray("messages").get(0)+"", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = getSharedPreferences(Datas.spName, MODE_PRIVATE).edit();
                        editor.putString("isLogged","false");
                        editor.apply();

                        dialogMessage("Your login Token expired. You Have to login.");


                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

                if (error instanceof TimeoutError) {
//                    Toast.makeText(SplashActivity.this,
//                            "Our server is busy, try later", Toast.LENGTH_LONG).show();
                    dialogMessage("Our server is currently busy, You can access our offline services");

                }


            }

        })
        {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }





/*
------------------------------------------------------
GET request with Access token
------------------------------------------------------
*/

    public void requestForLabs(final String accesstoken, final LoggedpatientDetail patientObj){
        // Post request
        String tag_json_obj = "json_obj_req";
        //"http://203.190.9.108/api.paitent.ecure24.com/
        String url = ApiClass.ApiPatientUrl+"api/Labs";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.hide();
                        pDialog.dismiss();

                        for(int i=0; i<response.length(); i++){
                            try {
                                LabReportModel labReportModel = (LabReportModel) gson.fromJson(response.getJSONObject(i).toString(),LabReportModel.class);
                                labLists.add(labReportModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(labLists.size()==0){
                            tv.setText("You have no record Yet");
                            emptyBox.setVisibility(View.VISIBLE);
                        }
                        patientLabAdapter = new PatientLabAdapter(ReceiptActivity.this,labLists,patientObj);
                        linearLayoutManager = new LinearLayoutManager(ReceiptActivity.this,LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(patientLabAdapter);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.hide();
                pDialog.dismiss();
                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

            }

        })
        {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + accesstoken);
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_obj);

    }