
====================================================================================
dependecy
====================================================================================

implementation 'com.squareup.retrofit2:retrofit:2.4.0'
implementation 'com.squareup.retrofit2:converter-gson:2.4.0'


====================================================================================
API json Format
====================================================================================
{
  "ip": "192.168.0.100",
  "ip_decimal": 1234567890,
  "country": "Bangladesh",
  "country_iso": "BD",
  "city": "Dhaka",
  "hostname": "192.168.0.100.awesome-isp.com"
}

link: use the link to convert json to POJO..
http://www.jsonschema2pojo.org



====================================================================================
MODEL : ServerResponse
====================================================================================

public class ServerResponse implements Serializable {
 
    @SerializedName("ip")
    private String ip;
    @SerializedName("ip_decimal")
    private Integer ipDecimal;
    @SerializedName("country")
    private String country;
    @SerializedName("country_iso")
    private String countryIso;
    @SerializedName("city")
    private String city;
 
    public String getIp() {
        return ip;
    }
 
    public void setIp(String ip) {
        this.ip = ip;
    }
 
    public Integer getIpDecimal() {
        return ipDecimal;
    }
 
    public void setIpDecimal(Integer ipDecimal) {
        this.ipDecimal = ipDecimal;
    }
 
    public String getCountry() {
        return country;
    }
 
    public void setCountry(String country) {
        this.country = country;
    }
 
    public String getCountryIso() {
        return countryIso;
    }
 
    public void setCountryIso(String countryIso) {
        this.countryIso = countryIso;
    }
 
    public String getCity() {
        return city;
    }
 
    public void setCity(String city) {
        this.city = city;
    }
}



====================================================================================
RetrofitApiClient:
singletone class
initialize retrofit 
====================================================================================
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
 
public class RetrofitApiClient {
 
        private static final String BASE_URL = "https://ifconfig.co<a data-mce-href="https://ifconfig.co" href="https://ifconfig.co"></a>";
        private static Retrofit retrofit = null;
 
        private static Gson gson = new GsonBuilder()
                .setLenient()
                .create();
 
 private RetrofitApiClient() {} // So that nobody can create an object with constructor
 
        public static Retrofit getClient() {
        if (retrofit == null) {
            synchronized (RetrofitApiClient.class) { //thread safe Singleton implementation, prevents recreation of object from different thread
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
                }
            }
        }
 
        return retrofit;
    }
 
}


/*
//if you want to include token in all routes
OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
      @Override
      public Response intercept(Chain chain) throws IOException {
        Request newRequest  = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer " + token)
            .build();
        return chain.proceed(newRequest);
      }
    }).build();

Retrofit retrofit = new Retrofit.Builder()
    .client(client)
    .baseUrl("")
    .addConverterFactory(GsonConverterFactory.create())
    .build();
*/




====================================================================================
ApiInterface
====================================================================================
import com.hellohasan.retrofitsimplegetrequest.ServerResponse;
 
import retrofit2.Call;
import retrofit2.http.GET;
 
public interface ApiInterface {
 
    @GET("/json") //Here, `json` is the PATH PARAMETER
    Call<ServerResponse> getMyIp();

    @GET("/api/users?") // ? is not necessary..url:  https://reqres.in/api/users?page=1
    Call<UserData> getUserData(@Query("page") int page);

 
    @GET("/retrofit_get_post/server_side_code.php")
    Call<ServerResponse> getJoke(@Query("user_id") String userId);


    //=======================================POST=========================================

    @Body 
    //– Sends Java objects as request body.

    @Field 
    //– send data as form-urlencoded. This requires a @FormUrlEncoded annotation attached with the method. The @Field parameter works only with a POST

    //for passing form data as apram, RequestBody is provided below
    @POST("somePostMethod")
    Call<ResponseBody> somePostMethod(@Body RequestBody body);

    /* requestBody for POST
RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("param1", param1)
        .addFormDataPart("param2", param2)
        .build();

apiInterface.somePostMethod(requestBody).enqueue(
    //onResponse onFailure methods
);
    */

//passing form data 
    @FormUrlEncoded
    @POST("/api/users?")
    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);

//passing json
    @POST("/retrofit_get_post/server_side_code.php")
    Call<ServerResponse> getUserValidity(@Body User userLoginCredential);

//passing json, with header content type
    @Headers({"Accept: application/json"})
    @POST("user/classes")
    Call<playlist> addToPlaylist(@Body PlaylistParm parm);

//you can use Headers as you want
    @Headers({
        "Accept: application/json",
        "User-Agent: Your-App-Name",
        "Cache-Control: max-age=640000"
    })

    //dynamic content type set up-----------------------------------------------
    @POST("user/classes")
    Call<ResponseModel> addToPlaylist(@Header("Content-Type") String content_type, @Body RequestModel req);

    mAPI.addToPlayList("application/json", playListParam);


    //====================================================================================



//=======================================TOKEN=========================================
    //bearer token passing GET
    //@Path("id")  can be replaced with Quesry, use Query
    @GET("api/Profiles/GetProfile?id={id}")
    Call<UserProfile> getUser(@Path("id") String id, @Header("Authorization") String authHeader);

//bearer token passing GET
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/Profiles/GetProfile")
    Call<UserProfile> getUser(@Query("id") String id, @Header("Authorization") String auth);
//====================================================================================


    //if you need to hit dynamic url, you can pass the url as a param
    @GET
    Call<WeatherResponse> getWeatherUpdate(@Url String dynamicUrl);

}

====================================================================================
Async vs sync code
====================================================================================

//====================================================================================
//sync code, run in background service or asynctask, otherwise app crashed...
//====================================================================================
TaskService taskService = ServiceGenerator.createService(TaskService.class);  
Call<List<Task>> call = taskService.getTasks();  
List<Task>> tasks = call.execute().body();  


//====================================================================================
//Async code, runs in seperate threade
//====================================================================================
TaskService taskService = ServiceGenerator.createService(TaskService.class);  
Call<List<Task>> call = taskService.getTasks();  
call.enqueue(new Callback<List<Task>>() {  
    @Override
    public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
        if (response.isSuccessful()) {
            // tasks available
        } else {
            // error response, no access to resource?
        }
    }

    @Override
    public void onFailure(Call<List<Task>> call, Throwable t) {
        // something went completely south (like no internet connection)
        Log.d("Error", t.getMessage());
    }
}

//====================================================================================
//Get Raw HTTP Response
//====================================================================================
call.enqueue(new Callback<List<Task>>() {  
    @Override
    public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
        // get raw response
        Response raw = response.raw();
    }

    @Override
    public void onFailure(Call<List<Task>> call, Throwable t) {}
}


====================================================================================
GET REQUEST
====================================================================================
public void showMyIp(View view) {
 
    progressBar.setVisibility(View.VISIBLE); //network call will start. So, show progress bar
 
    ApiInterface apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
 
    Call<ServerResponse> call = apiInterface.getMyIp();
    call.enqueue(new Callback<ServerResponse>() {
        @Override
        public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
            progressBar.setVisibility(View.GONE); //network call success. So hide progress bar
 
            ServerResponse serverResponse = response.body();
 
            if (response.code()==200 && serverResponse!=null) { //response code 200 means server call successful
                //data found. So place the data into TextView
                ipAddressTextView.setText(serverResponse.getIp());
                cityTextView.setText(serverResponse.getCity());
                countryTextView.setText(serverResponse.getCountry());
            } else {
                //somehow data not found. So error message showing in first TextView
                ipAddressTextView.setText(response.message());
                cityTextView.setText("");
                countryTextView.setText("");
            }
        }
 
        @Override
        public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
            progressBar.setVisibility(View.GONE); //network call failed. So hide progress bar
 
            //network call failed due to disconnect internet connection or server error
            ipAddressTextView.setText(t.getMessage());
            cityTextView.setText("");
            countryTextView.setText("");
        }
    });
}


====================================================================================
GET REQUEST WITH PARAMETERS
====================================================================================
public void getData() {
        if(Dataholder.getInstance().getCurrentPageNumber() > Dataholder.getInstance().getTotalPageNumber() ){
            Log.d("MSG","ALL data Loaded, getData returned");
            return;
        }

        isRefreshing = true;
        pbLayout.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);

        Call<UserData> call = apiInterface.getUserData(Dataholder.getInstance().getCurrentPageNumber());
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(@NonNull Call<UserData> call, @NonNull Response<UserData> response) {

                pbLayout.setVisibility(View.GONE);
                isRefreshing = false;
                UserData UserData = response.body();


                if (response.code() == 200 && UserData != null) {

                    Log.d("MSG", response.message());
                    Log.d("USER_DATA", "getPage: " + UserData.getPage().toString() + " -getTotalPage: " + UserData.getTotalPages().toString() + " - getTotal: " + UserData.getTotal().toString());
                    Log.d("USER_LIST", "Length: " + UserData.getData().size() + " -1st: " + UserData.getData().get(1).getAvatar());

                    Dataholder.getInstance().setCurrentPageNumber(UserData.getPage()+1);
                    Dataholder.getInstance().setTotalPageNumber(UserData.getTotalPages());

                    Log.d("DATA_HOLDER","hasmore "+Dataholder.getInstance().hasMoreData());

                    adapterRecyclerView.setListData(UserData.getData());

                    adapterRecyclerView.setAdData(UserData.getAd());

                } else {
                    Log.d("MSG", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserData> call, @NonNull Throwable t) {

                pbLayout.setVisibility(View.GONE);
                isRefreshing = false;
                Log.d("ERR", t.getMessage());
            }
        });
    }



====================================================================================
Dynamic URL GET REQUEST
====================================================================================
private void getWeather(String weatherUpdateUrl) {
 
    Call<WeathereResponse> call = apiInterface.getWeatherUpdate(weatherUpdateUrl);
 
    call.enqueue(new Callback<WeatherResponse>() {
        @Override
        public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
             WeatherResponse weatherResponse = response.body();
            // do something
        }
 
        @Override
        public void onFailure(Call<WeatherResponse> call, Throwable t) {
            Log.e(TAG, t.toString());
        }
    });
}    



====================================================================================
POST REQUEST with Object param
====================================================================================

//json formate for parameter
{
    //if you dont use @SerializedName("user_id")
    "userId":"hasan",
    "password":"123"
}
//correct form..
{
    "user_id":"hasan",
    "password":"123"
}
//POJO for json param to be used as POST body
public class User {
 
        @SerializedName("user_id")
        private String userId;
        @SerializedName("password")
        private String password;
 
        public User(){}
 
        public void setUserId(String userId) {
            this.userId = userId;
        }
 
        public void setPassword(String password) {
            this.password = password;
        }
 
}


private void checkUserValidity(User userCredential){
 
    Call<ServerResponse> call = apiInterface.getUserValidity(userCredential);
 
    call.enqueue(new Callback<ServerResponse>() {
 
        @Override
        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
 
            ServerResponse validity = response.body();
            Toast.makeText(getApplicationContext(), validity.getMessage(), Toast.LENGTH_LONG).show();
        }
 
        @Override
        public void onFailure(Call call, Throwable t) {
            Log.e(TAG, t.toString());
        }
    });
}


====================================================================================
POST REQUEST with Form data
====================================================================================
RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("param1", param1)
        .addFormDataPart("param2", param2)
        .build();

apiInterface.somePostMethod(requestBody).enqueue(
    //onResponse onFailure methods
);