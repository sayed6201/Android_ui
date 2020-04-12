====================================================================================
Interface: 1
MyApiService has methods to be called for API data fetch..
====================================================================================
public interface MyApiService {

    void userValidityCheck(User userLoginCredential, ResponseCallback<String> callback);
    void getJokeFromServer(String userId, ResponseCallback<String> callback);
}


====================================================================================
Interface: 2
# ResponseCallback has methods 2 methods, they will notify activity after api call.
# T data is generic/dynamic type, string/int/object can be passed/
====================================================================================
public interface ResponseCallback<T> {
	//#T data is generic/dynamic type, string/int/object can be passed
    void onSuccess(T data);
    void onError(Throwable th);
}


====================================================================================
RetrofitApiInterface has all api routes..
====================================================================================
public interface RetrofitApiInterface {

    @POST("retrofit_get_post/server_side_code.php")
    Call<ServerResponse> getUserValidity(@Body User userLoginCredential);

    @GET("retrofit_get_post/server_side_code.php")
    Call<ServerResponse> getJoke(@Query("user_id") String userId);

}


====================================================================================
RetrofitApiClient

singletone for initializing retrofit, gson
====================================================================================
public class RetrofitApiClient {
 
        private static final String BASE_URL = "http://192.168.0.105/"; //address of your remote server. Here I used localhost
        private static Retrofit retrofit = null;
 
        private static Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        private RetrofitApiClient() {} // So that nobody can create an object with constructor
 
        public static synchronized Retrofit getClient() {
            if (retrofit==null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
            return retrofit;
        }
 
}


====================================================================================
NetworkCall
# this calss will call all the api
# implements MyApiService class, so all methods of MyApiService must be implemented
====================================================================================
public class NetworkCall implements MyApiService{

	//userValidityCheck , POST method
	//final ResponseCallback<String> userValidityCheckListener, is the interface that will notify the result to the Activity
    @Override
    public void userValidityCheck(User userLoginCredential, final ResponseCallback<String> userValidityCheckListener) {
        Logger.addLogAdapter(new AndroidLogAdapter());

        RetrofitApiInterface retrofitApiInterface = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<ServerResponse> call = retrofitApiInterface.getUserValidity(userLoginCredential);

        call.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                Logger.d("Network layer. User validity Raw response: " + response.raw());

                ServerResponse validity = response.body();
                if(validity!=null){
                    if(validity.isSuccess())
                        userValidityCheckListener.onSuccess(validity.getMessage());
                    else
                        userValidityCheckListener.onError(new Exception(validity.getMessage()));
                }
                else
                    userValidityCheckListener.onError(new Exception(response.message()));

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            	//onError taked Throeable as input,,
                userValidityCheckListener.onError(t);
            }
        });
    }

	//getJokeFromServer , GET method
	//final ResponseCallback<String> userValidityCheckListener, is the interface that will notify the result to the Activity
    @Override
    public void getJokeFromServer(String userId, final ResponseCallback<String> getJokeListener) {
        RetrofitApiInterface retrofitApiInterface = RetrofitApiClient.getClient().create(RetrofitApiInterface.class);
        Call<ServerResponse> call = retrofitApiInterface.getJoke(userId);

        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                Logger.d("Network layer. get Joke Raw response: " + response.raw());
                ServerResponse validity = response.body();
                if(validity!=null){
                    if(validity.isSuccess())
                        getJokeListener.onSuccess(validity.getMessage());
                    else
                        getJokeListener.onError(new Exception(validity.getMessage()));
                }
                else
                    getJokeListener.onError(new Exception(response.message()));
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                getJokeListener.onError(t);
            }
        });
    }
}



++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
====================================================================================
NetworkCall
# this calss will call all the api
# implements MyApiService class, so all methods of MyApiService must be implemented
====================================================================================
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

public class MainActivity extends AppCompatActivity {

    private EditText userIdEditText;
    private EditText passwordEditText;
    private EditText jokeUserIdEditText;
    private TextView jokeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.addLogAdapter(new AndroidLogAdapter());

        //Initialize the view like EditText, TextView
        userIdEditText = (EditText) findViewById(R.id.login_id);
        passwordEditText = (EditText) findViewById(R.id.login_password);
        jokeUserIdEditText = (EditText) findViewById(R.id.user_id_for_joke);
        jokeTextView = (TextView) findViewById(R.id.jokeTextView);

    }

    // Login button event
    public void buttonClickEvent(View view){

        if(view.getId()==R.id.login_button){
            String userId;
            String password;
            User user = new User();

            userId = userIdEditText.getText().toString();
            password = passwordEditText.getText().toString();

            user.setUserId(userId);
            user.setPassword(password);

            //call method of interface
            MyApiService myApiService = new NetworkCall();
            myApiService.userValidityCheck(user, new ResponseCallback<String>() {

            	//ResponseCallback interface methods notifies activity..
                @Override
                public void onSuccess(String msg) {
                    showToast(msg);
                    Logger.d("Activity: onSuccess of userValidity method is called");
                }

                //ResponseCallback interface methods notifies activity..
                @Override
                public void onError(Throwable th) {
                    showToast(th.getMessage());
                    Logger.d("Activity: onError of userValidity method is called");
                }
            }); //user credential and listener

        }
        else {
            String userId;

            userId = jokeUserIdEditText.getText().toString();

            //call method of interface
            MyApiService myApiService = new NetworkCall();
            myApiService.getJokeFromServer(userId, new ResponseCallback<String>() {

            	//ResponseCallback interface methods notifies activity..
                @Override
                public void onSuccess(String joke) {
                    jokeTextView.setText(joke);
                    Logger.d("Activity: onSuccess of getJoke method is called");
                }

				//ResponseCallback interface methods notifies activity..
                @Override
                public void onError(Throwable th) {
                    showToast(th.getMessage());
                    Logger.d("Activity: onError of getJoke method is called");
                }
            }); //user credential and listener

        }
        
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

}