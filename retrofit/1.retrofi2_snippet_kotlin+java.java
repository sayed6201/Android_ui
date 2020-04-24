
====================================================================================
dependecy
====================================================================================

implementation 'com.squareup.retrofit2:retrofit:2.4.0'
implementation 'com.squareup.retrofit2:converter-gson:2.4.0'

//optionals
    implementation 'com.squareup.okhttp3:logging-interceptor:3.4.1'
    // implementation 'com.google.code.gson:gson:2.6.2'
    // implementation 'com.squareup.okhttp3:okhttp:3.4.1'







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


//=======================================SINGLETONE-MRE-API-CLIENT=========================================
    public class APIServiceProvider {

    private static APIServiceProvider apiServiceProvider;

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private HttpLoggingInterceptor loggingInterceptor;
    APIInterface apiInterface;

//declare constructor of singletone private
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
//=======================================MRE_APICLIENT_ENDS-HERE=========================================


   //=======================================KOTLIN=========================================
//Kotlin APi CLinet with interceptor example:

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY = "6e63c2317fbe963d76c3bdc2b785f6d1"
const val BASE_URL = "https://api.themoviedb.org/3/"

const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

// https://api.themoviedb.org/3/movie/popular?api_key=6e63c2317fbe963d76c3bdc2b785f6d1&page=1
// https://api.themoviedb.org/3/movie/299534?api_key=6e63c2317fbe963d76c3bdc2b785f6d1
// https://image.tmdb.org/t/p/w342/or06FN3Dka5tukK1e9sl16pB3iy.jpg

object ApiClient {


    fun getClient(): ApiInterface {

        val requestInterceptor = Interceptor { chain ->
            // Interceptor take only one argument which is a lambda function so parenthesis can be omitted

            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)   //explicitly return a value from whit @ annotation. lambda always returns the value of the last expression implicitly
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            //adding retrofit as calladapter
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

    }
}

/*

object ServiceBuilder {
    private val client = OkHttpClient
        .Builder()
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(TmdbEndpoints::class.java)

    fun buildService(): TmdbEndpoints {
        return retrofit
    }
}
*/
   //=======================================KOTLIN-ENDS-HERE=========================================







====================================================================================
ApiInterface
====================================================================================
import com.hellohasan.retrofitsimplegetrequest.ServerResponse;
 
import retrofit2.Call;
import retrofit2.http.GET;
 
public interface ApiInterface {
 
 //=======================================GET=========================================
    @GET("/json") //Here, `json` is the PATH PARAMETER
    Call<ServerResponse> getMyIp();

    @GET("/api/users?") // ? is not necessary..url:  https://reqres.in/api/users?page=1
    Call<UserData> getUserData(@Query("page") int page);

 
    @GET("/retrofit_get_post/server_side_code.php")
    Call<ServerResponse> getJoke(@Query("user_id") String userId);

    //path perameters
    //@path is required
    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id:Int): Single<MovieDetails> //Single<MovieDetails> rx java


    @GET(ToDoAppRestAPI.getToDoItem+"{authorEmailId}/")
    Call<List<ToDoItem>> getToDoList(@Path(value = "authorEmailId") String authorEmailId, @Header(value = "token") String token);


    //=======================================GET-END=========================================


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


    @POST(ToDoAppRestAPI.addToDoItem)
    Call<ToDoItem> addToDoItem(@Header(value = "token") String token, @Body ToDoItem toDoItem);


    //======================================POST-END==============================================



//=======================================TOKEN=========================================
    //bearer token passing GET
    //@Path("id")  can be replaced with Query, use Query
    @GET("api/Profiles/GetProfile?id={id}")
    Call<UserProfile> getUser(@Path("id") String id, @Header("Authorization") String authHeader);

    @POST(ToDoAppRestAPI.addToDoItem)
    Call<ToDoItem> addToDoItem(@Header(value = "token") String token, @Body ToDoItem toDoItem);


    @HTTP(method = "DELETE", path = ToDoAppRestAPI.deleteToDo, hasBody = true)
    Call<ResponseBody> deleteToDo(@Header(value = "token") String token, @Body ToDoItem toDoItem);

    @PUT(ToDoAppRestAPI.modifyToDoUrl)
    Call<ToDoItem> modifyToDoItem(@Header(value = "token") String token, @Body ModifyToDoPayloadBean modifyToDoPayloadBean);

     @GET(ToDoAppRestAPI.getToDoItem+"{authorEmailId}/")
    Call<List<ToDoItem>> getToDoList(@Path(value = "authorEmailId") String authorEmailId, @Header(value = "token") String token);



kotlin:
    //path perameters
    //@path is required
    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id:Int): Single<MovieDetails> //rxjava

//bearer token passing GET
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("api/Profiles/GetProfile")
    Call<UserProfile> getUser(@Query("id") String id, @Header("Authorization") String auth);

//========================================TOKEN-END============================================




    //Delete Reuest
    @HTTP(method = "DELETE", path = ToDoAppRestAPI.deleteToDo, hasBody = true)
    Call<ResponseBody> deleteToDo(@Header(value = "token") String token, @Body ToDoItem toDoItem);


    //PUT Request
    @PUT(ToDoAppRestAPI.modifyToDoUrl)
    Call<ToDoItem> modifyToDoItem(@Header(value = "token") String token, @Body ModifyToDoPayloadBean modifyToDoPayloadBean);


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